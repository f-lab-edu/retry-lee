package com.storage.repository;

import com.storage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u" +
            " join fetch u.account a" +
            " where a.email = :email")
    Optional<User> findByEmail(String email);

    Optional<User> findByUserIdAndRefreshToken(Long id, String refreshToken);
}
