package com.paul.restsecurity.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paul.restsecurity.domain.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {
	Role findByName(String roleName);
}
