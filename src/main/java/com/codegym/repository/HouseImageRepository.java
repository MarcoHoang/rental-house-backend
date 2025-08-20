package com.codegym.repository;

import com.codegym.entity.House;
import com.codegym.entity.HouseImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HouseImageRepository extends JpaRepository<HouseImage, Long> {
    List<HouseImage> findByHouse(House house);
    void deleteByHouse(House house);

    List<HouseImage> findByHouseOrderBySortOrderAsc(House house);

    @Query("SELECT MAX(hi.sortOrder) FROM HouseImage hi WHERE hi.house = :house")
    Integer findMaxSortOrderByHouse(@Param("house") House house);
}
