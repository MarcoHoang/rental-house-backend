package com.codegym.repository;

import com.codegym.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HouseRepository extends JpaRepository<House, Long> {
    List<House> findByHostId(Long hostId);
}
