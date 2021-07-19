package com.backend.xplaza.repository;

import com.backend.xplaza.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    @Query(value = "select * from confirmation_tokens where confirmation_token = ?1", nativeQuery = true)
    ConfirmationToken findByConfirmationToken(String confirmation_token);
}
