package com.codegym.repository;

import com.codegym.entity.Host;
import com.codegym.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HostRepository extends JpaRepository<Host, Long> {
    @Query("SELECT h FROM Host h WHERE h.user.id = :userId")
    @EntityGraph(attributePaths = {"user"})
    Optional<Host> findByUser(@Param("userId") Long userId);
    
    @Override
    @EntityGraph(attributePaths = {"user"})
    Optional<Host> findById(Long id);
    
    @Override
    @EntityGraph(attributePaths = {"user"})
    Page<Host> findAll(Pageable pageable);

    @Query("SELECT h FROM Host h WHERE " +
           "(:keyword IS NULL OR LOWER(h.user.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(h.user.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(h.user.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(h.user.address) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:active IS NULL OR h.user.active = :active)")
    @EntityGraph(attributePaths = {"user"})
    Page<Host> findByKeywordAndActive(@Param("keyword") String keyword,
                                      @Param("active") Boolean active,
                                      Pageable pageable);

    @Query("SELECT h FROM Host h WHERE " +
           "(:active IS NULL OR h.user.active = :active)")
    @EntityGraph(attributePaths = {"user"})
    Page<Host> findByActiveOnly(@Param("active") Boolean active,
                                Pageable pageable);
}