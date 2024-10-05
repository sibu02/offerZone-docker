package com.example.offerZone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.offerZone.models.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
	
	
}
