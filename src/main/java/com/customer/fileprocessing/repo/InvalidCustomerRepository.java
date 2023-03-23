package com.customer.fileprocessing.repo;

import com.customer.fileprocessing.entity.InvalidCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidCustomerRepository extends JpaRepository<InvalidCustomer, Long> {
}
