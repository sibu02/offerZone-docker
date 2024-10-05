package com.example.offerZone.services;



import java.util.List;

import com.example.offerZone.exception.UserException;
import com.example.offerZone.models.Address;
import com.example.offerZone.models.User;

public interface UserService {
	
	public List<User> findAllUser();
	
	public User updateUser(Long userId,User req);
	
	public User findUserById(Long userId)throws UserException;
	
	public User findUserByJwt(String jwt)throws UserException;
	
	public String deleteUser(Long userId) throws UserException;
	
	public Address saveNewAddress(Address address,User user)throws UserException;
}
