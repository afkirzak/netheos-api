package com.netheos.api.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.netheos.api.model.Role;
import com.netheos.api.model.User;
import com.netheos.api.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.httpBasic().and().authorizeRequests()
			.antMatchers("/public/**").hasRole("USER")
			.antMatchers("/private/**").hasRole("ADMIN")
			.and().csrf().disable().headers().frameOptions().disable();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		
		List<User> users = userService.findAll();
		for (User user : users) {
			String[] roles = user.getRoles().stream().map(Role::getRoleName).toArray(size -> new String[size]);
			auth.inMemoryAuthentication().withUser(user.getUsername()).password("{noop}" + user.getPassword())
					.roles(roles);
		}
	}
}
