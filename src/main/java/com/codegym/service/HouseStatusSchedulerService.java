package com.codegym.service;

import com.codegym.entity.House;
import com.codegym.entity.Rental;
import com.codegym.repository.HouseRepository;
import com.codegym.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HouseStatusSchedulerService {

    private final RentalRepository rentalRepository;
    private final HouseRepository houseRepository;

    /**
     * Tính toán trạng thái hiển thị của nhà dựa trên điều kiện:
     * thời gian bắt đầu <= thời gian hiện tại + 2 tiếng <= thời gian trả nhà
     */
    @Transactional(readOnly = true)
    public String calculateDisplayStatus(House house) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime checkTime = currentTime.plusHours(2); // Thời gian hiện tại + 2 tiếng
        
        // Tìm tất cả các rental có trạng thái SCHEDULED cho nhà này
        List<Rental> activeRentals = rentalRepository.findByStatusAndStartDateLessThanEqual(
            Rental.Status.SCHEDULED, checkTime);
        
        // Lọc các rental thuộc về nhà này
        List<Rental> houseRentals = activeRentals.stream()
            .filter(rental -> rental.getHouse().getId().equals(house.getId()))
            .toList();
        
        // Kiểm tra xem có rental nào thỏa mãn điều kiện không
        for (Rental rental : houseRentals) {
            // Điều kiện: thời gian bắt đầu <= thời gian hiện tại + 2 tiếng <= thời gian trả nhà
            if ((rental.getStartDate().isBefore(checkTime) || rental.getStartDate().isEqual(checkTime)) &&
                (checkTime.isBefore(rental.getEndDate()) || checkTime.isEqual(rental.getEndDate()))) {
                return "RENTED"; // Trả về "Đã thuê" nếu thỏa mãn điều kiện
            }
        }
        
        // Nếu không có rental nào thỏa mãn điều kiện, trả về trạng thái gốc của nhà
        return house.getStatus().name();
    }

    /**
     * Kiểm tra xem nhà có đang được thuê trong khoảng thời gian hiện tại + 2 tiếng không
     */
    @Transactional(readOnly = true)
    public boolean isHouseCurrentlyRented(House house) {
        String displayStatus = calculateDisplayStatus(house);
        return "RENTED".equals(displayStatus);
    }
}
