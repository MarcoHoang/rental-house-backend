package com.codegym.repository;

import com.codegym.entity.RoleName;
import com.codegym.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // Dashboard statistics methods
    long countByRoleName(RoleName roleName);
    
    // Google OAuth methods
    Optional<User> findByGoogleAccountId(String googleAccountId);
    Optional<User> findByFacebookAccountId(String facebookAccountId);

    // Search methods
    @Query("SELECT u FROM User u WHERE " +
           "(:keyword IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(u.address) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:role IS NULL OR u.role.name = :role) " +
           "AND (:active IS NULL OR u.active = :active)")
    List<User> findByKeywordAndFilters(@Param("keyword") String keyword, 
                                      @Param("role") String role, 
                                      @Param("active") Boolean active);

    @Query("SELECT u FROM User u WHERE " +
           "(:role IS NULL OR u.role.name = :role) " +
           "AND (:active IS NULL OR u.active = :active)")
    List<User> findByFilters(@Param("role") String role, 
                            @Param("active") Boolean active);
}
