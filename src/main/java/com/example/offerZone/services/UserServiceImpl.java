package com.example.offerZone.services;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.offerZone.models.Address;
import com.example.offerZone.models.User;
import com.example.offerZone.exception.UserException;
import com.example.offerZone.repositories.AddressRepository;
import com.example.offerZone.repositories.UserRepository;
import com.example.offerZone.utility.JwtUtil;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Override
	public User findUserById(Long userId) throws UserException {
		User user = userRepository.findUserById(userId);
		if(user == null) {
			throw new UserException("User Not Exits");
		}
		return user;
	}
	
	@Override
	public User findUserByJwt(String jwt) throws UserException {
		String username = jwtUtil.extractEmail(jwt);
		User user = userRepository.findByEmail(username);
		if(user == null) {
			throw new UserException("User Not Exits");
		}
		return user;
	}
	
	@Override
	public List<User> findAllUser() {
		return userRepository.findAll();
	}

	@Override
	public User updateUser(Long userId, User req) {
		User user = userRepository.findUserById(userId);
		user.setFirstName(req.getFirstName());
		user.setLastName(req.getLastName());
		user.setNumber(req.getNumber());
		return userRepository.save(user);
	}

	@Override
	public String deleteUser(Long userId) throws UserException {
		User user = userRepository.findUserById(userId);
		if(user == null) {
			throw new UserException("User Doesn't Exist");
		}
		userRepository.delete(user);
		return "User Successfully Deleted With Id " + userId; 
	}
	
	@Override
	public Address saveNewAddress(Address address,User user)throws UserException{
		address.setUser(user);
		Address savedAddress = addressRepository.save(address);
		return savedAddress;
	}
}
