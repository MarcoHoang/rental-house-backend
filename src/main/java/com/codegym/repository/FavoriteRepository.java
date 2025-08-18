package com.codegym.repository;

import com.codegym.entity.Favorite;
import com.codegym.entity.House;
import com.codegym.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query("SELECT f FROM Favorite f WHERE f.user.id = :userId AND f.house.id = :houseId")
    Optional<Favorite> findByUserIdAndHouseId(@Param("userId") Long userId, @Param("houseId") Long houseId);

    @Query("SELECT f.house.id FROM Favorite f WHERE f.user.id = :userId")
    List<Long> findHouseIdsByUserId(@Param("userId") Long userId);

    @Query("SELECT f FROM Favorite f WHERE f.user.id = :userId")
    List<Favorite> findByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(f) FROM Favorite f WHERE f.house.id = :houseId")
    Long countByHouseId(@Param("houseId") Long houseId);

    boolean existsByUserIdAndHouseId(Long userId, Long houseId);
    
    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.user.id = :userId AND f.house.id = :houseId")
    void deleteByUserIdAndHouseId(@Param("userId") Long userId, @Param("houseId") Long houseId);
}
