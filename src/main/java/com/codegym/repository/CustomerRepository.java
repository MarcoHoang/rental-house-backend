package com.codegym.repository;

import com.codegym.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<User, Long> {
}
