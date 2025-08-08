package com.codegym.repository;

import com.codegym.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    // (QUAN TRỌNG) Dùng trong RentalServiceImpl để kiểm tra lịch thuê có bị trùng không
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Rental r " +
            "WHERE r.house.id = :houseId " +
            "AND r.id <> :rentalIdToExclude " + // Loại trừ chính lịch đang xét (khi update)
            "AND r.startDate < :endDate " +
            "AND r.endDate > :startDate")
    boolean existsOverlappingRentalExcludingId(@Param("houseId") Long houseId,
                                               @Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate,
                                               @Param("rentalIdToExclude") Long rentalIdToExclude);

    // Dùng khi tạo mới
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Rental r " +
            "WHERE r.house.id = :houseId " +
            "AND r.startDate < :endDate " +
            "AND r.endDate > :startDate")
    boolean existsOverlappingRental(@Param("houseId") Long houseId,
                                    @Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate);


    // Dùng trong RentalServiceImpl để lấy lịch sử thuê của một người dùng
    List<Rental> findByRenterIdOrderByStartDateDesc(Long renterId);

    // Dùng trong RentalServiceImpl để lấy lịch thuê của các nhà thuộc một chủ nhà
    List<Rental> findByHouse_HouseRenter_IdOrderByStartDateDesc(Long houseRenterId);

    // Dùng trong RentalServiceImpl để thống kê thu nhập
    List<Rental> findByHouse_HouseRenter_IdAndStatus(Long houseRenterId, Rental.Status status);

    // (GỢI Ý) Dùng trong ReviewServiceImpl để kiểm tra người dùng đã thuê và trả phòng chưa
    boolean existsByRenterIdAndHouseIdAndStatus(Long renterId, Long houseId, Rental.Status status);

}