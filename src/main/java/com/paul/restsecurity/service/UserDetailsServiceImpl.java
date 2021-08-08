package com.paul.restsecurity.service;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.paul.restsecurity.domain.AppUser;
import com.paul.restsecurity.repo.AppUserRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private final AppUserRepo appUserRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		log.debug("Auth user {} ", username);
		AppUser appUser = appUserRepo.findByUsername(username);
		if (appUser == null) {
			log.error("User is not found");
			throw new UsernameNotFoundException("User is not found");
		} else {
			log.info("{} found in DB", username);
		}
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		appUser.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
		return new User(appUser.getUsername(), appUser.getPassword(), authorities);
	}

}
