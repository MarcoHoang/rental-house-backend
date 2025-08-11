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

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Rental r " +
            "WHERE r.house.id = :houseId " +
            "AND r.id <> :rentalIdToExclude " +
            "AND r.startDate < :endDate " +
            "AND r.endDate > :startDate")
    boolean existsOverlappingRentalExcludingId(@Param("houseId") Long houseId,
                                               @Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate,
                                               @Param("rentalIdToExclude") Long rentalIdToExclude);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Rental r " +
            "WHERE r.house.id = :houseId " +
            "AND r.startDate < :endDate " +
            "AND r.endDate > :startDate")
    boolean existsOverlappingRental(@Param("houseId") Long houseId,
                                    @Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate);


    List<Rental> findByRenterIdOrderByStartDateDesc(Long renterId);

    List<Rental> findByHouse_HouseRenter_IdOrderByStartDateDesc(Long houseRenterId);

    List<Rental> findByHouse_HouseRenter_IdAndStatus(Long houseRenterId, Rental.Status status);

    boolean existsByRenterIdAndHouseIdAndStatus(Long renterId, Long houseId, Rental.Status status);

    @Query("SELECT SUM(r.totalPrice) FROM Rental r WHERE r.renter.id = :renterId")
    Double sumTotalPriceByRenterId(@Param("renterId") Long renterId);

}