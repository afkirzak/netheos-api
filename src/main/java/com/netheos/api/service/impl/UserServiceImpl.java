package com.netheos.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netheos.api.model.User;
import com.netheos.api.repository.UserRepository;
import com.netheos.api.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public List<User> findAll() {
		
		Iterable<User> itUser = userRepository.findAll();
		List<User> list = new ArrayList<User>();
		itUser.forEach(list::add);
		return list;
	}
}
