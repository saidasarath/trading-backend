package com.trading.trading.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.trading.trading.config.JwtProvider;
import com.trading.trading.domain.verificationType;
import com.trading.trading.entity.TwoFactorAuth;
import com.trading.trading.entity.User;
import com.trading.trading.repository.UserRepository;
import com.trading.trading.services.UserService;
@RestController
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public User findUserProfileByJwt(String jwt) {
        String email=JwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);
        if(user == null) throw new RuntimeException("User not found");
        return user;
    }
    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if(user == null) throw new RuntimeException("User not found");
        return user;
    }
    @Override
    public User findUserById(Long id) {
        Optional<User> user=userRepository.findById(id);
       
        if(user == null) throw new RuntimeException("User not found");
        return user.get();
    }
    @Override
    public User enableTwoFactorAuthentication(verificationType verificationType,String sendTo,User user) {
        TwoFactorAuth twoFactorAuth=new TwoFactorAuth();
        twoFactorAuth.setEnable(true);
        twoFactorAuth.setSendTo(verificationType);
        user.setTwoFactorAuth(twoFactorAuth);
        return userRepository.save(user);
    }
    @Override
    public User updatePassword(User user, String password) {
        user.setPassword(password);
        return userRepository.save(user);
    }
}

