package com.paul.restsecurity.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.paul.restsecurity.domain.AppUser;
import com.paul.restsecurity.domain.Role;
import com.paul.restsecurity.repo.AppUserRepo;
import com.paul.restsecurity.repo.RoleRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class UserServiceImpl implements UserService {
	private final AppUserRepo appUserRepo;
	private final RoleRepo roleRepo;
	private final PasswordEncoder passwordEncoder;

	@Override
	public AppUser saveUser(AppUser appUser) {
		log.debug("test");
		appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
		return appUserRepo.save(appUser);
	}

	@Override
	public Role saveRole(Role role) {
		return roleRepo.save(role);
	}

	@Override
	public void addRoleToUser(String username, String roleName) {
		log.info("add {} to {}", roleName, username);
		AppUser appUser = appUserRepo.findByUsername(username);
		Role role = roleRepo.findByName(roleName);
		appUser.getRoles().add(role);
	}

	@Override
	public AppUser getUser(String username) {
		return appUserRepo.findByUsername(username);
	}

	@Override
	public List<AppUser> getUsers() {

		return appUserRepo.findAll();
	}

}
