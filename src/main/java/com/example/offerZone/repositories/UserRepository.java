package com.example.offerZone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.offerZone.models.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
	
	User findUserById(long userId);
	
	User findByEmail(String email);

}
