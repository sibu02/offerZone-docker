package com.example.offerZone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.offerZone.models.Roles;

@Repository
public interface RolesRepository extends JpaRepository<Roles,Long>{

	Roles findById(int id);
	Roles findByName(String name);
	boolean existsByName(String name);
}
