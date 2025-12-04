package com.tcs.registration_login.userauthentication.security;

import com.tcs.registration_login.userauthentication.model.User;
import com.tcs.registration_login.userauthentication.repository.UserRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
   private final UserRepository userRepository;

   public CustomUserDetailsService(UserRepository userRepository){
       this.userRepository = userRepository;
   }
   @Override
   public UserDetails loadUserByUsername(String username){
       User user =  userRepository.findByUsername(username)
               .orElseThrow(()->new UsernameNotFoundException("User not found"));

       return new org.springframework.security.core.userdetails.User(
               user.getUsername(),
               user.getPassword(),
               List.of(new SimpleGrantedAuthority(user.getRole()))
       );

   }
}
