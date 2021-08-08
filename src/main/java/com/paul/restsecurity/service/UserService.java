package com.paul.restsecurity.service;

import java.util.List;

import com.paul.restsecurity.domain.AppUser;
import com.paul.restsecurity.domain.Role;


public interface UserService {
	AppUser saveUser(AppUser appUser);

	Role saveRole(Role role);

	void addRoleToUser(String username, String role);

	AppUser getUser(String username);

	List<AppUser> getUsers();

}
