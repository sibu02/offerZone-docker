package com.example.offerZone.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.offerZone.models.Roles;
import com.example.offerZone.models.User;
import com.example.offerZone.repositories.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
		Set<GrantedAuthority> authorities = new HashSet<>();
		for(Roles role : user.getRoles()) {
			GrantedAuthority authority = new SimpleGrantedAuthority(role.getRole());
			authorities.add(authority); 
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),authorities);
	}

}
