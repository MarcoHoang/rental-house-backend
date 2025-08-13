package com.codegym.repository;

import com.codegym.entity.House;
import com.codegym.entity.HouseImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HouseImageRepository extends JpaRepository<HouseImage, Long> {
    List<HouseImage> findByHouse(House house);
    void deleteByHouse(House house);
}
