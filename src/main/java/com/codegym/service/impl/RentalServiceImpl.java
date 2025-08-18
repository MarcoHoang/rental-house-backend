package com.codegym.service.impl;

import com.codegym.dto.response.RentalDTO;
import com.codegym.entity.House;
import com.codegym.entity.Rental;
import com.codegym.entity.User;
import com.codegym.exception.AppException;
import com.codegym.exception.ResourceNotFoundException;
import com.codegym.repository.HouseRepository;
import com.codegym.repository.RentalRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.RentalService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.context.SecurityContextHolder;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;
    private final HouseRepository houseRepository;
    private final UserRepository userRepository;

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
                .renterId(rental.getRenter().getId())
                .startDate(rental.getStartDate())
                .endDate(rental.getEndDate())
                .status(rental.getStatus())
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

        if (house.getStatus() != House.Status.AVAILABLE) {
            throw new AppException(StatusCode.HOUSE_NOT_AVAILABLE);
        }
        boolean isOverlapping = rentalRepository.existsOverlappingRental(
                house.getId(), dto.getStartDate(), dto.getEndDate());
        if (isOverlapping) {
            throw new AppException(StatusCode.RENTAL_PERIOD_OVERLAP);
        }

        Rental rental = Rental.builder()
                .house(house)
                .renter(renter)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(Rental.Status.SCHEDULED)
                .build();

        return convertToDTO(rentalRepository.save(rental));
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
}