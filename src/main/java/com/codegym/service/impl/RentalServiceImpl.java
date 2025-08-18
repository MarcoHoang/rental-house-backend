package com.codegym.service.impl;

import com.codegym.dto.response.RentalDTO;
import com.codegym.dto.request.CreateRentalRequest;
import com.codegym.entity.House;
import com.codegym.entity.Rental;
import com.codegym.entity.User;
import com.codegym.entity.Notification;
import com.codegym.exception.AppException;
import com.codegym.exception.ResourceNotFoundException;
import com.codegym.repository.HouseRepository;
import com.codegym.repository.RentalRepository;
import com.codegym.repository.UserRepository;
import com.codegym.repository.NotificationRepository;
import com.codegym.service.RentalService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;
    private final HouseRepository houseRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, userId));
    }

    private House findHouseByIdOrThrow(Long houseId) {
        return houseRepository.findById(houseId)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.HOUSE_NOT_FOUND, houseId));
    }

    private Rental findRentalByIdOrThrow(Long rentalId) {
        return rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.RENTAL_NOT_FOUND, rentalId));
    }

    private RentalDTO convertToDTO(Rental rental) {
        return RentalDTO.builder()
                .id(rental.getId())
                .houseId(rental.getHouse().getId())
                .houseTitle(rental.getHouse().getTitle())
                .houseAddress(rental.getHouse().getAddress())
                .renterId(rental.getRenter().getId())
                .renterName(rental.getRenter().getFullName())
                .startDate(rental.getStartDate())
                .endDate(rental.getEndDate())
                .status(rental.getStatus())
                .totalPrice(rental.getTotalPrice())
                .guestCount(rental.getGuestCount())
                .messageToHost(rental.getMessageToHost())
                .rejectReason(rental.getRejectReason())
                .approvedAt(rental.getApprovedAt())
                .rejectedAt(rental.getRejectedAt())
                .createdAt(rental.getCreatedAt())
                .updatedAt(rental.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalDTO> findAll() {
        return rentalRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RentalDTO findById(Long id) {
        Rental rental = findRentalByIdOrThrow(id);
        return convertToDTO(rental);
    }

    @Override
    @Transactional
    public RentalDTO create(RentalDTO dto) {
        User renter = findUserByIdOrThrow(dto.getRenterId());
        House house = findHouseByIdOrThrow(dto.getHouseId());

        // Validation: Kiểm tra nhà có khả dụng không
        if (house.getStatus() != House.Status.AVAILABLE) {
            throw new AppException(StatusCode.HOUSE_NOT_AVAILABLE);
        }

        // Validation: Không cho phép chủ nhà thuê nhà của chính mình
        if (house.getHost().getId().equals(renter.getId())) {
            throw new AppException(StatusCode.CANNOT_RENT_OWN_HOUSE);
        }

        // Validation: Kiểm tra ngày bắt đầu phải trong tương lai
        if (dto.getStartDate().isBefore(LocalDateTime.now())) {
            throw new AppException(StatusCode.INVALID_START_DATE);
        }

        // Validation: Kiểm tra thời gian thuê tối thiểu (ít nhất 1 ngày)
        long daysBetween = java.time.Duration.between(dto.getStartDate(), dto.getEndDate()).toDays();
        if (daysBetween < 1) {
            throw new AppException(StatusCode.MINIMUM_RENTAL_PERIOD);
        }

        // Validation: Kiểm tra trùng lịch
        boolean isOverlapping = rentalRepository.existsOverlappingRental(
                house.getId(), dto.getStartDate(), dto.getEndDate());
        if (isOverlapping) {
            throw new AppException(StatusCode.RENTAL_PERIOD_OVERLAP);
        }

        // Tính toán tổng tiền
        double totalPrice = calculateTotalPrice(house.getPrice(), dto.getStartDate(), dto.getEndDate());

        Rental rental = Rental.builder()
                .house(house)
                .renter(renter)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(Rental.Status.PENDING)  // Thay đổi từ SCHEDULED thành PENDING
                .totalPrice(totalPrice)
                .build();

        Rental savedRental = rentalRepository.save(rental);
        createNotificationForHost(house.getHost(), savedRental);

        return convertToDTO(savedRental);
    }

    // Helper method để tính tổng tiền
    private double calculateTotalPrice(Double monthlyPrice, LocalDateTime startDate, LocalDateTime endDate) {
        if (monthlyPrice == null) return 0.0;
        
        long hoursBetween = java.time.Duration.between(startDate, endDate).toHours();
        // Tính theo giờ (giá tháng / 30 ngày / 24 giờ)
        double hourlyPrice = monthlyPrice / 30.0 / 24.0;
        return Math.ceil(hoursBetween * hourlyPrice);
    }

    @Override
    @Transactional
    public RentalDTO update(Long id, RentalDTO dto) {
        Rental rental = findRentalByIdOrThrow(id);

        if(rental.getStatus() != Rental.Status.SCHEDULED) {
            throw new AppException(StatusCode.CANNOT_UPDATE_ACTIVE_RENTAL);
        }

        rental.setStartDate(dto.getStartDate());
        rental.setEndDate(dto.getEndDate());

        return convertToDTO(rentalRepository.save(rental));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Rental rental = findRentalByIdOrThrow(id);
        rentalRepository.delete(rental);
    }

    @Override
    @Transactional
    public RentalDTO checkin(Long id) {
        Rental rental = findRentalByIdOrThrow(id);

        if (rental.getStatus() != Rental.Status.SCHEDULED) {
            throw new AppException(StatusCode.INVALID_CHECKIN_STATUS);
        }

        rental.setStatus(Rental.Status.CHECKED_IN);

        House house = rental.getHouse();
        house.setStatus(House.Status.RENTED);
        houseRepository.save(house);

        return convertToDTO(rentalRepository.save(rental));
    }

    @Override
    @Transactional
    public RentalDTO checkout(Long id) {
        Rental rental = findRentalByIdOrThrow(id);

        if (rental.getStatus() != Rental.Status.CHECKED_IN) {
            throw new AppException(StatusCode.INVALID_CHECKOUT_STATUS);
        }

        rental.setStatus(Rental.Status.CHECKED_OUT);

        House house = rental.getHouse();
        house.setStatus(House.Status.AVAILABLE);
        houseRepository.save(house);

        return convertToDTO(rentalRepository.save(rental));
    }

    @Override
    @Transactional
    public RentalDTO cancel(Long id) {
        Rental rental = findRentalByIdOrThrow(id);

        // Cho phép hủy khi đang ở trạng thái PENDING hoặc SCHEDULED
        if (rental.getStatus() != Rental.Status.PENDING && rental.getStatus() != Rental.Status.SCHEDULED) {
            throw new AppException(StatusCode.INVALID_CANCEL_STATUS);
        }

        // Kiểm tra quyền: chỉ renter mới được cancel
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, currentUserEmail));
        
        if (!rental.getRenter().getId().equals(currentUser.getId())) {
            throw new AppException(StatusCode.ACCESS_DENIED);
        }

        // Nếu đang SCHEDULED, kiểm tra thời gian (không cho phép cancel trong vòng 1 ngày trước)
        if (rental.getStatus() == Rental.Status.SCHEDULED) {
            long hoursUntilStart = java.time.Duration.between(LocalDateTime.now(), rental.getStartDate()).toHours();
            if (hoursUntilStart < 24) {
                throw new AppException(StatusCode.CANNOT_CANCEL_WITHIN_24H);
            }
        }

        rental.setStatus(Rental.Status.CANCELED);

        // Nếu nhà đang ở trạng thái RENTED, chuyển về AVAILABLE
        House house = rental.getHouse();
        if (house.getStatus() == House.Status.RENTED) {
            house.setStatus(House.Status.AVAILABLE);
            houseRepository.save(house);
        }

        return convertToDTO(rentalRepository.save(rental));
    }


    @Override
    @Transactional(readOnly = true)
    public List<RentalDTO> getUserRentals(Long userId) {
        findUserByIdOrThrow(userId);
        return rentalRepository.findByRenterIdOrderByStartDateDesc(userId)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalDTO> getCurrentUserRentals() {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, currentUserEmail));
        
        return rentalRepository.findByRenterIdOrderByStartDateDesc(currentUser.getId())
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalDTO> getHostRentals(Long hostId) {
        findUserByIdOrThrow(hostId);
        return rentalRepository.findByHouse_Host_IdOrderByStartDateDesc(hostId)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Double> getHostIncome(Long hostId) {
        findUserByIdOrThrow(hostId);
        List<Rental> rentals = rentalRepository.findByHouse_Host_IdAndStatus(hostId, Rental.Status.CHECKED_OUT);

        return rentals.stream()
                .collect(Collectors.groupingBy(
                        rental -> YearMonth.from(rental.getEndDate()).toString(),
                        Collectors.summingDouble(rental -> rental.getTotalPrice() != null ? rental.getTotalPrice() : 0)
                ));
    }

    private void createNotificationForHost(User host, Rental rental) {
        Notification notification = Notification.builder()
                .receiver(host)
                .content("Bạn có một đơn thuê mới từ " + rental.getRenter().getFullName() + " cho nhà " + rental.getHouse().getTitle())
                .type(Notification.Type.RENTAL_REQUEST)
                .rental(rental)
                .build();
        notificationRepository.save(notification);
    }

    // New methods for rental request workflow
    @Override
    @Transactional
    public RentalDTO createRequest(CreateRentalRequest request) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, currentUserEmail));
        
        House house = findHouseByIdOrThrow(request.getHouseId());

        // Validation: Kiểm tra nhà có khả dụng không
        if (house.getStatus() != House.Status.AVAILABLE) {
            throw new AppException(StatusCode.HOUSE_NOT_AVAILABLE);
        }

        // Validation: Không cho phép chủ nhà thuê nhà của chính mình
        if (house.getHost().getId().equals(currentUser.getId())) {
            throw new AppException(StatusCode.CANNOT_RENT_OWN_HOUSE);
        }

        // Validation: Kiểm tra ngày bắt đầu phải lớn hơn 2 tiếng so với hiện tại
        LocalDateTime minimumStartTime = LocalDateTime.now().plusHours(2);
        if (request.getStartDate().isBefore(minimumStartTime)) {
            throw new AppException(StatusCode.INVALID_START_DATE);
        }

        // Validation: Kiểm tra thời gian thuê tối thiểu (ít nhất 2 tiếng)
        long hoursBetween = java.time.Duration.between(request.getStartDate(), request.getEndDate()).toHours();
        if (hoursBetween < 2) {
            throw new AppException(StatusCode.MINIMUM_RENTAL_PERIOD);
        }

        // Validation: Kiểm tra trùng lịch
        boolean isOverlapping = rentalRepository.existsOverlappingRental(
                house.getId(), request.getStartDate(), request.getEndDate());
        if (isOverlapping) {
            throw new AppException(StatusCode.RENTAL_PERIOD_OVERLAP);
        }

        // Tính toán tổng tiền
        double totalPrice = calculateTotalPrice(house.getPrice(), request.getStartDate(), request.getEndDate());

        // Set giá trị mặc định cho guestCount nếu null
        Integer guestCount = request.getGuestCount() != null ? request.getGuestCount() : 1;

        Rental rental = Rental.builder()
                .house(house)
                .renter(currentUser)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(Rental.Status.PENDING)
                .totalPrice(totalPrice)
                .guestCount(guestCount)
                .messageToHost(request.getMessageToHost())
                .build();

        Rental savedRental = rentalRepository.save(rental);
        createNotificationForHost(house.getHost(), savedRental);

        return convertToDTO(savedRental);
    }

    private void createNotificationForUser(User user, Rental rental, Notification.Type type, String message) {
        Notification notification = Notification.builder()
                .receiver(user)
                .content(message)
                .type(type)
                .rental(rental)
                .build();
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public RentalDTO approveRequest(Long rentalId) {
        Rental rental = findRentalByIdOrThrow(rentalId);
        
        if (rental.getStatus() != Rental.Status.PENDING) {
            throw new AppException(StatusCode.INVALID_APPROVE_STATUS);
        }

        // Kiểm tra quyền: chỉ host mới được approve
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, currentUserEmail));
        
        if (!rental.getHouse().getHost().getId().equals(currentUser.getId())) {
            throw new AppException(StatusCode.ACCESS_DENIED);
        }

        rental.setStatus(Rental.Status.APPROVED);
        rental.setApprovedAt(LocalDateTime.now());
        rental.setApprovedBy(currentUser);

        // Tự động chuyển sang SCHEDULED sau khi approve
        rental.setStatus(Rental.Status.SCHEDULED);

        Rental savedRental = rentalRepository.save(rental);
        
        // Gửi notification cho user
        createNotificationForUser(
            savedRental.getRenter(), 
            savedRental, 
            Notification.Type.RENTAL_APPROVED,
            "Yêu cầu thuê nhà của bạn đã được chấp nhận bởi " + savedRental.getHouse().getHost().getFullName()
        );

        return convertToDTO(savedRental);
    }

    @Override
    @Transactional
    public RentalDTO rejectRequest(Long rentalId, String reason) {
        Rental rental = findRentalByIdOrThrow(rentalId);
        
        if (rental.getStatus() != Rental.Status.PENDING) {
            throw new AppException(StatusCode.INVALID_REJECT_STATUS);
        }

        // Kiểm tra quyền: chỉ host mới được reject
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, currentUserEmail));
        
        if (!rental.getHouse().getHost().getId().equals(currentUser.getId())) {
            throw new AppException(StatusCode.ACCESS_DENIED);
        }

        rental.setStatus(Rental.Status.REJECTED);
        rental.setRejectedAt(LocalDateTime.now());
        rental.setRejectedBy(currentUser);
        rental.setRejectReason(reason);

        Rental savedRental = rentalRepository.save(rental);
        
        // Gửi notification cho user
        createNotificationForUser(
            savedRental.getRenter(), 
            savedRental, 
            Notification.Type.RENTAL_REJECTED,
            "Yêu cầu thuê nhà của bạn đã bị từ chối. Lý do: " + reason
        );

        return convertToDTO(savedRental);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalDTO> getHostPendingRequests(Long hostId) {
        findUserByIdOrThrow(hostId);
        return rentalRepository.findByHouse_Host_IdAndStatus(hostId, Rental.Status.PENDING)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalDTO> getHostRequestsByStatus(Long hostId, List<Rental.Status> statuses) {
        findUserByIdOrThrow(hostId);
        return rentalRepository.findByHouse_Host_IdAndStatusIn(hostId, statuses)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalDTO> getUserRequestsByStatus(Long userId, List<Rental.Status> statuses) {
        findUserByIdOrThrow(userId);
        return rentalRepository.findByRenterIdAndStatusIn(userId, statuses)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Long getHostPendingRequestsCount(Long hostId) {
        findUserByIdOrThrow(hostId);
        return rentalRepository.countPendingRequestsByHost(hostId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsOverlappingRental(Long houseId, LocalDateTime startDate, LocalDateTime endDate) {
        return rentalRepository.existsOverlappingRental(houseId, startDate, endDate);
    }

    // Dashboard statistics methods
    @Override
    @Transactional(readOnly = true)
    public long countAllRentals() {
        return rentalRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countRentalsByStatus(String status) {
        try {
            Rental.Status rentalStatus = Rental.Status.valueOf(status.toUpperCase());
            return rentalRepository.countByStatus(rentalStatus);
        } catch (IllegalArgumentException e) {
            return 0;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public double calculateTotalRevenue() {
        Double total = rentalRepository.sumTotalPriceByStatus(Rental.Status.CHECKED_OUT);
        return total != null ? total : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public double calculateMonthlyRevenue() {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        Double monthly = rentalRepository.sumTotalPriceByStatusAndDateAfter(Rental.Status.CHECKED_OUT, startOfMonth);
        return monthly != null ? monthly : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getRecentRentalsForDashboard(int limit) {
        return rentalRepository.findTop5ByOrderByCreatedAtDesc().stream()
                .map(rental -> {
                    Map<String, Object> rentalData = new HashMap<>();
                    rentalData.put("id", rental.getId());
                    rentalData.put("houseTitle", rental.getHouse().getTitle());
                    rentalData.put("renterName", rental.getRenter().getFullName());
                    rentalData.put("totalPrice", rental.getTotalPrice());
                    rentalData.put("status", rental.getStatus());
                    rentalData.put("startDate", rental.getStartDate());
                    rentalData.put("endDate", rental.getEndDate());
                    rentalData.put("createdAt", rental.getCreatedAt());
                    return rentalData;
                })
                .collect(Collectors.toList());
    }

}