package com.codegym.repository;

import com.codegym.entity.Host;
import com.codegym.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;

public interface HostRepository extends JpaRepository<Host, Long> {
    Optional<Host> findByUser(User user);

    @EntityGraph(attributePaths = {"user"})
    Optional<Host> findByUserId(Long userId);
}