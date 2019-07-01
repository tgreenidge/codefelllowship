package com.tgreenidge.codefellowship.codefellowship;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;


public interface AppUserRepository extends CrudRepository<AppUser, Long> {
    AppUser findByUsername(String username);
    AppUser findById(long id);
    List findAll();
}
