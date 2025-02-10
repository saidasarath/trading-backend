package com.trading.trading.services;

import com.trading.trading.domain.verificationType;
import com.trading.trading.entity.User;

public interface UserService {
   public User findUserProfileByJwt(String jwt);
   public User findUserByEmail(String email);
   public User findUserById(Long id);

   public User enableTwoFactorAuthentication(verificationType verificationType,String sendTo, User user); 
   User updatePassword(User user, String password);
}
