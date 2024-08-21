package com.storage.repository;

import com.storage.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    @Query("select count(a) > 0" +
            " from Admin a" +
            " join a.account ac" +
            " where ac.accountId = :accountId")
    boolean existsByAccountId(Long accountId);

    @Query("select a from Admin a" +
            " join fetch a.account ac" +
            " where ac.accountId = :accountId")
    Optional<Admin> findByAccountId(Long accountId);

    Optional<Admin> findByAdminIdAndRefreshToken(Long id, String refreshToken);

    @Query("select a from Admin a" +
            " join fetch a.account ac" +
            " where ac.email = :email")
    Optional<Admin> findByEmail(String email);

    @Query("SELECT a.account.email FROM Admin a WHERE a.adminId = :id")
    Optional<String> findEmailById(Long id);
}
