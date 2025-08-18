package com.codegym.repository;

import com.codegym.entity.Rental;
import com.codegym.entity.User;
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

    List<Rental> findByHouse_Host_IdOrderByStartDateDesc(Long hostId);

    List<Rental> findByHouse_Host_IdAndStatus(Long hostId, Rental.Status status);

    List<Rental> findByHouse_Host_IdAndStatusIn(Long hostId, List<Rental.Status> statuses);

    List<Rental> findByRenterIdAndStatusIn(Long renterId, List<Rental.Status> statuses);

    boolean existsByRenterIdAndHouseIdAndStatus(Long renterId, Long houseId, Rental.Status status);

    @Query("SELECT COUNT(r) FROM Rental r WHERE r.house.host.id = :hostId AND r.status = 'PENDING'")
    Long countPendingRequestsByHost(@Param("hostId") Long hostId);

    @Query("SELECT SUM(r.totalPrice) FROM Rental r WHERE r.renter.id = :renterId")
    Double sumTotalPriceByRenterId(@Param("renterId") Long renterId);

    @Query("SELECT SUM(r.totalPrice) FROM Rental r WHERE r.house.host.id = :hostUserId")
    Double sumTotalPriceByHost(@Param("hostUserId") Long hostUserId);

    // Dashboard statistics methods
    long countByStatus(Rental.Status status);
    
    @Query("SELECT SUM(r.totalPrice) FROM Rental r WHERE r.status = :status")
    Double sumTotalPriceByStatus(@Param("status") Rental.Status status);
    
    @Query("SELECT SUM(r.totalPrice) FROM Rental r WHERE r.status = :status AND r.createdAt >= :startDate")
    Double sumTotalPriceByStatusAndDateAfter(@Param("status") Rental.Status status, @Param("startDate") LocalDateTime startDate);
    
    List<Rental> findTop5ByOrderByCreatedAtDesc();

}