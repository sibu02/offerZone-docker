package com.example.offerZone.controllers;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.offerZone.exception.UserException;
import com.example.offerZone.models.Roles;
import com.example.offerZone.models.User;
import com.example.offerZone.repositories.RolesRepository;
import com.example.offerZone.repositories.UserRepository;
import com.example.offerZone.request.LoginRequest;
import com.example.offerZone.response.AuthResponse;
import com.example.offerZone.services.MyUserDetailsService;
import com.example.offerZone.utility.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RolesRepository rolesRepository; 
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private MyUserDetailsService myuserDetailsService;
	
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user)throws Exception{
		String email = user.getEmail();
		String password = user.getPassword();
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		String number = user.getNumber();
		User isEmailExist = userRepo.findByEmail(email);;
		
		if(isEmailExist != null) {
			throw new Exception("Email alredy exist");
		}
		User newUser = new User();
		newUser.setEmail(email);
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setPassword(passwordEncoder.encode(password));
		newUser.setNumber(number);
		newUser.setCreatedAt(LocalDateTime.now());
		
		try {
			Roles role = rolesRepository.findByName("ROLE_USER");
			Set<Roles> roles = new HashSet<>();
			roles.add(role);
			newUser.setRoles(roles);
			userRepo.save(newUser);
		} catch (Exception e) {
			throw new Exception("error");
		}
		
		UserDetails userDetails = myuserDetailsService.loadUserByUsername(email);
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtUtil.generateToken(authentication);
		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("SignUp Success");
		
		return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.CREATED);
	}
	
	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest) throws UserException{
		String username = loginRequest.getEmail();
		String password = loginRequest.getPassword();
		Authentication authentication = authenticate(username,password);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtUtil.generateToken(authentication);
		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("Signin Success");
		return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.CREATED);
	}

	private Authentication authenticate(String username, String password) {
		UserDetails userDetails = myuserDetailsService.loadUserByUsername(username);
		if(userDetails == null) {
			throw new BadCredentialsException("Invalid Credential");
		}
		
		if(!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid Credential");
		}
		return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
	}
}
