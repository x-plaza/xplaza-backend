package com.backend.xplaza.repository;

import com.backend.xplaza.model.CustomerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CustomerUserRepository extends JpaRepository<CustomerDetails, Long> {
    @Query(value = "select email from customers where customer_id = ?1", nativeQuery = true)
    String getUsername(Long id);

    @Query(value = "select * from customers where email = ?1", nativeQuery = true)
    CustomerDetails findCustomerByUsername(String username);

    @Modifying
    @Transactional
    @Query(value = "update customers set password=?1, salt=?2 where email=?3", nativeQuery = true)
    void changePassword(String new_password, String salt, String user_name);
}
