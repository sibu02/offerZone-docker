package com.example.offerZone.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.offerZone.exception.UserException;
import com.example.offerZone.models.Address;
import com.example.offerZone.models.User;
import com.example.offerZone.services.UserService;
import com.example.offerZone.utility.JwtUtil;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private JwtUtil jwtUtil;
	
    @Autowired
    private UserService userService;
    
    @GetMapping("/profile")
    public ResponseEntity<User> getUserByToken(@RequestHeader("Authorization") String authHead) throws UserException {
    	try {
    		if(authHead != null && authHead.startsWith("Bearer ")) {
        		String jwt = authHead.substring(7);
        		boolean isExpired = jwtUtil.isTokenExpired(jwt);
        		if(!isExpired) {
        			User user = userService.findUserByJwt(jwt);
                    return new ResponseEntity<>(user, HttpStatus.OK);
        		}else {
        			return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
        		}
        	}else {
        		return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        	}
    	}
    	catch(UserException e) {
    		return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	
        
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User user) throws UserException {
        User updatedUser = userService.updateUser(userId, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) throws UserException {
        String response = userService.deleteUser(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/profile/newAddress")
    public ResponseEntity<Address> saveAddress(@RequestHeader("Authorization") String authHead,@RequestBody Address address)throws UserException{
    	if(authHead != null && authHead.startsWith("Bearer ")) {
    		String jwt = authHead.substring(7);
    		User user = userService.findUserByJwt(jwt);
    		Address savedAddress = userService.saveNewAddress(address, user);
    		return new ResponseEntity<>(savedAddress,HttpStatus.OK);
    	}
    	return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
    }
    
    @GetMapping("/getAllUser")
    public ResponseEntity<List<User>> getUserById() throws UserException {
        List<User> allUsers = userService.findAllUser();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }
    
}
