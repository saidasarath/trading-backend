package com.trading.trading.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.trading.trading.entity.User;
import com.trading.trading.repository.UserRepository;
@Service
public class CustomeUserService implements UserDetailsService {
    @Autowired
 private UserRepository userRepository ;
 public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
     User user=userRepository.findByEmail(email);
     if(user==null) {
         throw new UsernameNotFoundException("User not found");
     }
     List<GrantedAuthority> authorityList =  new ArrayList<>();
     return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),authorityList);
 }
}
