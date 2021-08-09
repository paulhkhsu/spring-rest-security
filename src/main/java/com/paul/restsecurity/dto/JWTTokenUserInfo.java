package com.paul.restsecurity.dto;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString

public class JWTTokenUserInfo {
	String username;
	String[] roles;
}
