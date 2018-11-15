package com.netheos.api.repository;

import org.springframework.data.repository.CrudRepository;

import com.netheos.api.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

}
