package com.paul.restsecurity.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paul.restsecurity.domain.AppUser;

public interface AppUserRepo extends JpaRepository<AppUser, Long> {
	AppUser findByUsername(String username);

}
