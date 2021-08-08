package com.paul.restsecurity;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.paul.restsecurity.domain.AppUser;
import com.paul.restsecurity.domain.Role;
import com.paul.restsecurity.service.UserService;

@SpringBootApplication
public class SpringRestSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRestSecurityApplication.class, args);
	}
/*
	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {
			userService.saveRole(new Role(null, "ROLE_USER"));
			userService.saveRole(new Role(null, "ROLE_ADMIN"));
			userService.saveRole(new Role(null, "ROLE_MANGER"));
			userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

			userService.saveUser(new AppUser(null, "John Travolta", "john", "1234", new ArrayList<Role>()));
			userService.saveUser(new AppUser(null, "Will Smith", "will", "1234", new ArrayList<Role>()));
			userService.saveUser(new AppUser(null, "Jim Carry", "jim", "1234", new ArrayList<Role>()));
			userService.saveUser(new AppUser(null, "Arnold Schwarzenegger", "arnold", "1234", new ArrayList<Role>()));

			userService.addRoleToUser("john", "ROLE_USER");
			userService.addRoleToUser("will", "ROLE_ADMIN");
			userService.addRoleToUser("jim", "ROLE_MANGER");
			userService.addRoleToUser("arnold", "ROLE_USER");
			userService.addRoleToUser("arnold", "ROLE_ADMIN");
			userService.addRoleToUser("arnold", "ROLE_MANGER");
			userService.addRoleToUser("arnold", "ROLE_SUPER_ADMIN");
		};

	}
*/
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
