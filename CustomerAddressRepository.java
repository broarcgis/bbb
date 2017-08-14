package com.klikpeta.ordispatch.repository;

import com.klikpeta.ordispatch.domain.CustomerAddress;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CustomerAddress entity.
 */
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress,Long> {

    List<CustomerAddress> findAllByCustomerId(Long customerId);
}
