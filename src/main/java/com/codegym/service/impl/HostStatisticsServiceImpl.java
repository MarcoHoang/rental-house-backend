package com.codegym.service.impl;

import com.codegym.dto.response.HostStatisticsDTO;
import com.codegym.entity.House;
import com.codegym.entity.Rental;
import com.codegym.entity.User;
import com.codegym.repository.HouseRepository;
import com.codegym.repository.RentalRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.HostStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class HostStatisticsServiceImpl implements HostStatisticsService {

    private final RentalRepository rentalRepository;
    private final HouseRepository houseRepository;
    private final UserRepository userRepository;

    @Override
    public HostStatisticsDTO getHostStatistics(Long hostId, String period) {
        try {
            System.out.println("HostStatisticsServiceImpl.getHostStatistics - Starting for hostId: " + hostId + ", period: " + period);
            
            // Validate host exists
            User host = userRepository.findById(hostId)
                    .orElseThrow(() -> new RuntimeException("Host not found with ID: " + hostId));
            System.out.println("HostStatisticsServiceImpl.getHostStatistics - Host found: " + host.getEmail());

            // Get date range based on period
            DateRange dateRange = getDateRange(period);
            System.out.println("HostStatisticsServiceImpl.getHostStatistics - Date range: " + dateRange.startDate + " to " + dateRange.endDate);
            
            // Get all houses of the host
            List<House> hostHouses = houseRepository.findByHostId(hostId);
            if (hostHouses == null) {
                hostHouses = new ArrayList<>();
            }
            System.out.println("HostStatisticsServiceImpl.getHostStatistics - Found " + hostHouses.size() + " houses");
            
            // Get rentals in the period
            List<Rental> periodRentals = rentalRepository.findByHouseHostIdAndDateRange(
                    hostId, dateRange.startDate, dateRange.endDate);
            if (periodRentals == null) {
                periodRentals = new ArrayList<>();
            }
            System.out.println("HostStatisticsServiceImpl.getHostStatistics - Found " + periodRentals.size() + " rentals in period");
            
            // Get previous period rentals for comparison
            DateRange previousRange = getPreviousPeriod(dateRange);
            List<Rental> previousRentals = rentalRepository.findByHouseHostIdAndDateRange(
                    hostId, previousRange.startDate, previousRange.endDate);
            if (previousRentals == null) {
                previousRentals = new ArrayList<>();
            }
            System.out.println("HostStatisticsServiceImpl.getHostStatistics - Found " + previousRentals.size() + " rentals in previous period");
            
            HostStatisticsDTO result = buildStatistics(hostId, period, hostHouses, periodRentals, previousRentals, dateRange);
            System.out.println("HostStatisticsServiceImpl.getHostStatistics - Built statistics successfully");
            return result;
        } catch (Exception e) {
            System.err.println("HostStatisticsServiceImpl.getHostStatistics - Error for hostId " + hostId + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error in getHostStatistics for hostId " + hostId + ": " + e.getMessage(), e);
        }
    }

    @Override
    public HostStatisticsDTO getCurrentHostStatistics(String period) {
        try {
            System.out.println("HostStatisticsServiceImpl.getCurrentHostStatistics - Starting with period: " + period);
            
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            System.out.println("HostStatisticsServiceImpl.getCurrentHostStatistics - Username from auth: " + username);
            
            if (username == null || username.isEmpty()) {
                throw new RuntimeException("Authentication username is null or empty");
            }
            
            // Try to find user by username first, then by email if not found
            User currentUser = userRepository.findByUsername(username)
                    .orElseGet(() -> userRepository.findByEmail(username)
                            .orElseThrow(() -> new RuntimeException("User not found with username/email: " + username)));
            
            System.out.println("HostStatisticsServiceImpl.getCurrentHostStatistics - Found user: " + currentUser.getId() + ", email: " + currentUser.getEmail());
            
            if (currentUser == null) {
                throw new RuntimeException("Current user is null after lookup");
            }
            
            return getHostStatistics(currentUser.getId(), period);
        } catch (Exception e) {
            System.err.println("HostStatisticsServiceImpl.getCurrentHostStatistics - Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error in getCurrentHostStatistics: " + e.getMessage(), e);
        }
    }

    private DateRange getDateRange(String period) {
        LocalDate now = LocalDate.now();
        LocalDate startDate;
        LocalDate endDate = now;

        switch (period) {
            case "current_month":
                startDate = now.withDayOfMonth(1);
                break;
            case "last_month":
                YearMonth lastMonth = YearMonth.now().minusMonths(1);
                startDate = lastMonth.atDay(1);
                endDate = lastMonth.atEndOfMonth();
                break;
            case "last_3_months":
                startDate = now.minusMonths(3).withDayOfMonth(1);
                break;
            case "last_6_months":
                startDate = now.minusMonths(6).withDayOfMonth(1);
                break;
            case "current_year":
                startDate = now.withDayOfYear(1);
                break;
            default:
                startDate = now.withDayOfMonth(1);
        }

        return new DateRange(startDate, endDate);
    }

    private DateRange getPreviousPeriod(DateRange currentRange) {
        long daysDiff = java.time.temporal.ChronoUnit.DAYS.between(currentRange.startDate, currentRange.endDate);
        LocalDate previousStart = currentRange.startDate.minusDays(daysDiff + 1);
        LocalDate previousEnd = currentRange.startDate.minusDays(1);
        return new DateRange(previousStart, previousEnd);
    }

    private HostStatisticsDTO buildStatistics(Long hostId, String period, List<House> houses,
                                           List<Rental> currentRentals, List<Rental> previousRentals,
                                           DateRange dateRange) {
        
        // Calculate current period stats
        BigDecimal totalRevenue = currentRentals.stream()
                .map(rental -> rental.getTotalPrice() != null ? BigDecimal.valueOf(rental.getTotalPrice()) : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        Integer totalRentals = currentRentals.size();
        Integer totalHouses = houses.size(); // Số nhà đã đăng
        
        // Calculate previous period stats for comparison
        BigDecimal previousRevenue = previousRentals.stream()
                .map(rental -> rental.getTotalPrice() != null ? BigDecimal.valueOf(rental.getTotalPrice()) : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        Integer previousRentalsCount = previousRentals.size();
        
        // Calculate changes
        BigDecimal revenueChange = totalRevenue.subtract(previousRevenue);
        Double revenueChangePercentage = previousRevenue.compareTo(BigDecimal.ZERO) > 0 
                ? revenueChange.divide(previousRevenue, 4, RoundingMode.HALF_UP).doubleValue() * 100
                : 0.0;
        
        Integer rentalChange = totalRentals - previousRentalsCount;
        Double rentalChangePercentage = previousRentalsCount > 0 
                ? (double) rentalChange / previousRentalsCount * 100
                : 0.0;
        
        // Calculate tax and fees (10% each)
        BigDecimal taxAmount = totalRevenue.multiply(new BigDecimal("0.10"));
        BigDecimal platformFee = totalRevenue.multiply(new BigDecimal("0.10"));
        BigDecimal totalDeductions = taxAmount.add(platformFee);
        BigDecimal netRevenue = totalRevenue.subtract(totalDeductions);
        
        // Calculate occupancy rate
        Double occupancyRate = calculateOccupancyRate(houses, dateRange);
        
        // Get top houses and least rented houses
        List<HostStatisticsDTO.HouseRentalStats> topHouses = getTopHouses(currentRentals);
        List<HostStatisticsDTO.HouseRentalStats> leastRentedHouses = getLeastRentedHouses(currentRentals);
        
        // Get monthly trend
        List<HostStatisticsDTO.MonthlyRevenue> monthlyTrend = getMonthlyTrend(currentRentals, dateRange);
        
        return HostStatisticsDTO.builder()
                .hostId(hostId)
                .period(period)
                .totalHouses(totalHouses) // Thêm số nhà đã đăng
                .totalRentals(totalRentals)
                .totalRevenue(totalRevenue)
                .netRevenue(netRevenue)
                .occupancyRate(occupancyRate)
                .revenueChange(revenueChange)
                .revenueChangePercentage(revenueChangePercentage)
                .rentalChange(rentalChange)
                .rentalChangePercentage(rentalChangePercentage)
                .topHouses(topHouses)
                .leastRentedHouses(leastRentedHouses)
                .monthlyTrend(monthlyTrend)
                .taxAmount(taxAmount)
                .platformFee(platformFee)
                .totalDeductions(totalDeductions)
                .build();
    }

    private Double calculateOccupancyRate(List<House> houses, DateRange dateRange) {
        if (houses.isEmpty()) return 0.0;
        
        long totalDays = java.time.temporal.ChronoUnit.DAYS.between(dateRange.startDate, dateRange.endDate) + 1;
        long totalHouseDays = houses.size() * totalDays;
        
        // This is a simplified calculation. In reality, you'd need to consider
        // actual availability dates and booking periods
        return Math.min(85.0, 85.0); // Mock value for now
    }

    private List<HostStatisticsDTO.HouseRentalStats> getTopHouses(List<Rental> rentals) {
        if (rentals == null || rentals.isEmpty()) {
            return new ArrayList<>();
        }
        
        Map<Long, List<Rental>> houseRentals = rentals.stream()
                .filter(rental -> rental.getHouse() != null) // Filter out rentals with null house
                .collect(Collectors.groupingBy(rental -> rental.getHouse().getId()));
        
        return houseRentals.entrySet().stream()
                .map(entry -> {
                    Long houseId = entry.getKey();
                    List<Rental> houseRentalList = entry.getValue();
                    
                    if (houseRentalList.isEmpty()) {
                        return null;
                    }
                    
                    House house = houseRentalList.get(0).getHouse();
                    if (house == null) {
                        return null;
                    }
                    
                    BigDecimal totalRevenue = houseRentalList.stream()
                            .map(rental -> rental.getTotalPrice() != null ? BigDecimal.valueOf(rental.getTotalPrice()) : BigDecimal.ZERO)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    BigDecimal averageRevenue = houseRentalList.size() > 0 
                            ? totalRevenue.divide(new BigDecimal(houseRentalList.size()), 2, RoundingMode.HALF_UP)
                            : BigDecimal.ZERO;
                    
                    return HostStatisticsDTO.HouseRentalStats.builder()
                            .houseId(houseId)
                            .houseTitle(house.getTitle() != null ? house.getTitle() : "Unknown")
                            .address(house.getAddress() != null ? house.getAddress() : "Unknown")
                            .rentalCount(houseRentalList.size())
                            .totalRevenue(totalRevenue)
                            .averageRevenue(averageRevenue)
                            .build();
                })
                .filter(Objects::nonNull) // Filter out null entries
                .sorted((h1, h2) -> h2.getRentalCount().compareTo(h1.getRentalCount()))
                .limit(5)
                .collect(Collectors.toList());
    }

    private List<HostStatisticsDTO.HouseRentalStats> getLeastRentedHouses(List<Rental> rentals) {
        if (rentals == null || rentals.isEmpty()) {
            return new ArrayList<>();
        }
        
        Map<Long, List<Rental>> houseRentals = rentals.stream()
                .filter(rental -> rental.getHouse() != null) // Filter out rentals with null house
                .collect(Collectors.groupingBy(rental -> rental.getHouse().getId()));
        
        return houseRentals.entrySet().stream()
                .map(entry -> {
                    Long houseId = entry.getKey();
                    List<Rental> houseRentalList = entry.getValue();
                    
                    if (houseRentalList.isEmpty()) {
                        return null;
                    }
                    
                    House house = houseRentalList.get(0).getHouse();
                    if (house == null) {
                        return null;
                    }
                    
                    BigDecimal totalRevenue = houseRentalList.stream()
                            .map(rental -> rental.getTotalPrice() != null ? BigDecimal.valueOf(rental.getTotalPrice()) : BigDecimal.ZERO)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    BigDecimal averageRevenue = houseRentalList.size() > 0 
                            ? totalRevenue.divide(new BigDecimal(houseRentalList.size()), 2, RoundingMode.HALF_UP)
                            : BigDecimal.ZERO;
                    
                    return HostStatisticsDTO.HouseRentalStats.builder()
                            .houseId(houseId)
                            .houseTitle(house.getTitle() != null ? house.getTitle() : "Unknown")
                            .address(house.getAddress() != null ? house.getAddress() : "Unknown")
                            .rentalCount(houseRentalList.size())
                            .totalRevenue(totalRevenue)
                            .averageRevenue(averageRevenue)
                            .build();
                })
                .filter(Objects::nonNull) // Filter out null entries
                .sorted(Comparator.comparing(HostStatisticsDTO.HouseRentalStats::getRentalCount))
                .limit(3)
                .collect(Collectors.toList());
    }

    private List<HostStatisticsDTO.MonthlyRevenue> getMonthlyTrend(List<Rental> rentals, DateRange dateRange) {
        if (rentals == null || rentals.isEmpty()) {
            return new ArrayList<>();
        }
        
        Map<String, List<Rental>> monthlyRentals = rentals.stream()
                .filter(rental -> rental.getStartDate() != null) // Filter out rentals with null startDate
                .collect(Collectors.groupingBy(rental -> {
                    LocalDateTime rentalDate = rental.getStartDate();
                    return rentalDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
                }));
        
        return monthlyRentals.entrySet().stream()
                .map(entry -> {
                    String monthKey = entry.getKey();
                    List<Rental> monthRentals = entry.getValue();
                    
                    if (monthRentals.isEmpty()) {
                        return null;
                    }
                    
                    BigDecimal monthRevenue = monthRentals.stream()
                            .map(rental -> rental.getTotalPrice() != null ? BigDecimal.valueOf(rental.getTotalPrice()) : BigDecimal.ZERO)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    String[] parts = monthKey.split("-");
                    if (parts.length < 2) {
                        return null;
                    }
                    
                    String month = parts[1];
                    String year = parts[0];
                    
                    return HostStatisticsDTO.MonthlyRevenue.builder()
                            .month(month)
                            .year(year)
                            .revenue(monthRevenue)
                            .rentalCount(monthRentals.size())
                            .build();
                })
                .filter(Objects::nonNull) // Filter out null entries
                .sorted(Comparator.comparing(HostStatisticsDTO.MonthlyRevenue::getYear)
                        .thenComparing(HostStatisticsDTO.MonthlyRevenue::getMonth))
                .collect(Collectors.toList());
    }

    private static class DateRange {
        final LocalDate startDate;
        final LocalDate endDate;

        DateRange(LocalDate startDate, LocalDate endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }
    }
}
