package com.doan.nhansu.admin.service;

import com.doan.nhansu.admin.entity.User;
import com.doan.nhansu.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {

  private final UserRepository userRepository;

  public UserDetailsService userDetailsService() {
      return new UserDetailsService() {
          @Override
          public UserDetails loadUserByUsername(String username) {
              User u = new User();
              u.setEmail("289294");
              return u;
          }
      };
  }
}
