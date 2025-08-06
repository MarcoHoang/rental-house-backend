package com.codegym.service.impl;

import com.codegym.dto.response.RentalDTO;
import com.codegym.entity.House;
import com.codegym.entity.Rental;
import com.codegym.entity.User;
import com.codegym.repository.HouseRepository;
import com.codegym.repository.RentalRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.RentalService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return rentalRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bản ghi thuê nhà với ID: " + id));
    }

    @Override
    @Transactional
    public RentalDTO create(RentalDTO dto) {
        User renter = userRepository.findById(dto.getRenterId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người thuê với ID: " + dto.getRenterId()));
        House house = houseRepository.findById(dto.getHouseId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy nhà với ID: " + dto.getHouseId()));

        if (house.getStatus() != House.Status.AVAILABLE) {
            throw new IllegalStateException("Nhà này hiện không có sẵn để cho thuê.");
        }
        boolean isOverlapping = rentalRepository.existsOverlappingRental(
                house.getId(), dto.getStartDate(), dto.getEndDate());
        if (isOverlapping) {
            throw new IllegalArgumentException("Nhà đã được thuê trong khoảng thời gian này. Vui lòng chọn ngày khác.");
        }

        Rental rental = Rental.builder()
                .house(house)
                .renter(renter)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(Rental.Status.SCHEDULED) // Trạng thái ban đầu khi đặt lịch
                .build();

        return convertToDTO(rentalRepository.save(rental));
    }

    @Override
    @Transactional
    public RentalDTO update(Long id, RentalDTO dto) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy bản ghi thuê nhà để cập nhật với ID: " + id));

        if(rental.getStatus() != Rental.Status.SCHEDULED) {
            throw new IllegalStateException("Không thể cập nhật lịch thuê đã/đang diễn ra hoặc đã hoàn thành.");
        }

        rental.setStartDate(dto.getStartDate());
        rental.setEndDate(dto.getEndDate());

        return convertToDTO(rentalRepository.save(rental));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!rentalRepository.existsById(id)) {
            throw new EntityNotFoundException("Không thể hủy. Lịch thuê với ID " + id + " không tồn tại.");
        }
        rentalRepository.deleteById(id);
    }

    @Override
    @Transactional
    public RentalDTO checkin(Long id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy lịch thuê để check-in với ID: " + id));

        if (rental.getStatus() != Rental.Status.SCHEDULED) {
            throw new IllegalStateException("Không thể check-in. Trạng thái hiện tại: " + rental.getStatus());
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
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy lịch thuê để check-out với ID: " + id));

        if (rental.getStatus() != Rental.Status.CHECKED_IN) {
            throw new IllegalStateException("Không thể check-out. Trạng thái hiện tại: " + rental.getStatus());
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
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("Không tìm thấy người dùng với ID: " + userId);
        }
        return rentalRepository.findByRenterIdOrderByStartDateDesc(userId)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalDTO> getHouseRenterRentals(Long houseRenterId) {
        if (!userRepository.existsById(houseRenterId)) {
            throw new EntityNotFoundException("Không tìm thấy chủ nhà với ID: " + houseRenterId);
        }
        return rentalRepository.findByHouse_HouseRenter_IdOrderByStartDateDesc(houseRenterId)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Double> getHouseRenterIncome(Long houseRenterId) {
        List<Rental> rentals = rentalRepository.findByHouse_HouseRenter_IdAndStatus(houseRenterId, Rental.Status.CHECKED_OUT);

        return rentals.stream()
                .collect(Collectors.groupingBy(
                        rental -> YearMonth.from(rental.getEndDate()).toString(), // Nhóm theo "YYYY-MM"
                        Collectors.summingDouble(rental -> rental.getTotalPrice() != null ? rental.getTotalPrice() : 0)
                ));
    }
}