package com.backend.xplaza.repository;

import com.backend.xplaza.model.CustomerLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerLoginRepository extends JpaRepository<CustomerLogin, Long> {
    @Query(value = "select customer_id, concat(first_name,' ', last_name) as customer_name, email " +
            "from customers where email = ?1", nativeQuery = true)
    CustomerLogin findCustomerByUsername(String username);
}
