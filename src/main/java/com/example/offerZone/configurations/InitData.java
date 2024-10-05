package com.example.offerZone.configurations;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.offerZone.models.Roles;
import com.example.offerZone.models.User;
import com.example.offerZone.repositories.RolesRepository;
import com.example.offerZone.repositories.UserRepository;

@Configuration
public class InitData {

    @Autowired
    private RolesRepository rolesRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserRepository userRepository;

    @Bean
    public CommandLineRunner initRoles() {
        return args -> {
            long rolesCount = rolesRepository.count();
            if (rolesCount <= 0) {
                Roles userRole = new Roles();
                userRole.setRole("ROLE_USER");  // It's common to prefix roles with "ROLE_"
                Roles adminRole = new Roles();
                adminRole.setRole("ROLE_ADMIN");
          
                rolesRepository.save(userRole);
                rolesRepository.save(adminRole);
                
                
                //Creating Admin
                User newAdmin = new User();
                newAdmin.setFirstName("sibu");
                newAdmin.setLastName("admin");
                newAdmin.setEmail("sibu@gmail.com");
                newAdmin.setPassword(passwordEncoder.encode("sibu"));
                newAdmin.setCreatedAt(LocalDateTime.now());
                newAdmin.setNumber("1234567890");
                userRepository.save(newAdmin);
                
                try {
        			Roles role1 = rolesRepository.findByName("ROLE_USER");
        			Roles role2 = rolesRepository.findByName("ROLE_ADMIN");
        			Set<Roles> roles = new HashSet<>();
        			roles.add(role1);
        			roles.add(role2);
        			
        			User newUser = userRepository.findByEmail(newAdmin.getEmail());
        			newUser.setRoles(roles);
        			userRepository.save(newUser);
        		} catch (Exception e) {
        			throw new Exception(e);
        		}
                System.out.println("init initialized");
            }
        };
    }
}
