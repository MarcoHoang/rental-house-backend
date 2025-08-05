package com.codegym.repository;

import com.codegym.entity.HouseRenter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseRenterRepository extends JpaRepository<HouseRenter, Long> {
}
