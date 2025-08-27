package com.codegym.repository;

import com.codegym.entity.PasswordResetOtp;
import com.codegym.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, Long> {
    Optional<PasswordResetOtp> findByOtp(String otp);

    @Modifying
    @Transactional
    @Query("DELETE FROM PasswordResetOtp o WHERE o.user = :user")
    void deleteByUser(User user);
}
