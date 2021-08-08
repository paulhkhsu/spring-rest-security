package com.paul.restsecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.paul.restsecurity.filter.CustomAuthenticationFilter;
import com.paul.restsecurity.filter.CustomAuthorzationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final UserDetailsService userDetailsService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// change behave of login path, you need to do
		// http.authorizeRequests().antMatchers(HttpMethod.POST,
		// "/api/login").permitAll()
		CustomAuthenticationFilter customerAuthenticationFilter = new CustomAuthenticationFilter(
				authenticationManagerBean());
		customerAuthenticationFilter.setFilterProcessesUrl("/api/login");
		// end of change behave
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// order is important after following
		// defined in
		// UsernamePasswordAuthenticationFilter->AbstractAuthenticationProcessingFilter
		// for spring default, no need
		// http.authorizeRequests().antMatchers(HttpMethod.POST, "/login").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/login/**").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/token/refresh/**").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user/**").hasAnyAuthority("ROLE_USER");
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user/save/**").hasAnyAuthority("ROLE_ADMIN");

		// http.authorizeRequests().anyRequest().permitAll(); // don't do this for
		// production
		http.authorizeRequests().anyRequest().authenticated();
		// http.addFilter(new
		// CustomerAuthenticationFilter(authenticationManagerBean()));
		http.addFilter(customerAuthenticationFilter);
		http.addFilterBefore(new CustomAuthorzationFilter(), UsernamePasswordAuthenticationFilter.class);
		// http.addFilterBefore(customAuthorzationFilter,
		// UsernamePasswordAuthenticationFilter.class); // can't autowired

	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/api/login/**").antMatchers("/api/token/refresh/**");

	}
}
