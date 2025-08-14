package com.codegym.repository;

import com.codegym.entity.RoleName;
import com.codegym.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<User> findByPhone(String phone);

    boolean existsByUsername(String username);

    boolean existsByPhone(String phone);

    Page<User> findByRole_NameNot(RoleName roleName, Pageable pageable);

    Page<User> findByRole_NameNotIn(List<RoleName> roles, Pageable pageable);

    List<User> findByRole_Name(RoleName roleName);
}
