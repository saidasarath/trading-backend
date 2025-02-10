package com.trading.trading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trading.trading.config.JwtProvider;
import com.trading.trading.entity.TwoFactorOTP;
import com.trading.trading.entity.User;
import com.trading.trading.repository.UserRepository;
import com.trading.trading.response.AuthResponse;
import com.trading.trading.services.EmailService;
import com.trading.trading.services.TwoFactorOtpService;
import com.trading.trading.utils.OtpUtils;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TwoFactorOtpService twoFactorOtpService;
    @Autowired
    private EmailService emailService;
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> Register( @RequestBody User user) throws Exception{ 
       
        
        User isEmailexist=userRepository.findByEmail(user.getEmail());
        if(isEmailexist!=null){
            throw new Exception("email is already used with another account");
        }
        User newuser=new User();
        newuser.setFullname(user.getFullname());
        newuser.setPassword(user.getPassword());
        newuser.setEmail(user.getEmail());
        userRepository.save(newuser);
        
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = JwtProvider.generateToken(auth);
        AuthResponse authResponse=new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setStatus(true);
        authResponse.setMessage("User registered successfully");
        return new ResponseEntity<>(authResponse,HttpStatus.CREATED);
    }
    
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login( @RequestBody User user) throws Exception{ 
       
        String userName=user.getEmail();
        String password=user.getPassword();
        
        
        Authentication auth = authenticate(userName,password);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = JwtProvider.generateToken(auth);
        User authUser=userRepository.findByEmail(userName);
        if(user.getTwoFactorAuth().isEnable()){
           AuthResponse authResponse=new AuthResponse();
           authResponse.setMessage("Two factor authentication is enabled"); 
           authResponse.setTwoFactorAuthEnabled(true);
           String otp = OtpUtils.generateOTP();
           TwoFactorOTP oldTwoFactorOTP=twoFactorOtpService.findByUser(authUser.getId());
           if(oldTwoFactorOTP!=null){
               twoFactorOtpService.deleteTwoFactorOTP(oldTwoFactorOTP);
           }
              TwoFactorOTP newtwoFactorOTP=twoFactorOtpService.createTwoFactorOTP(authUser, otp, jwt);
              emailService.sendverificationEmail(userName, otp);
              
              authResponse.setSession(newtwoFactorOTP.getId());

              return new ResponseEntity<>(authResponse,HttpStatus.ACCEPTED);
        }
        AuthResponse authResponse=new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setStatus(true);
        authResponse.setMessage("login successfully");
        return new ResponseEntity<>(authResponse,HttpStatus.ACCEPTED);
    }
    private Authentication authenticate(String userName, String password) throws Exception {
        User user=userRepository.findByEmail(userName);
        if(user==null){
            throw new Exception("User not found");
        }
        if(!user.getPassword().equals(password)){
            throw new Exception("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userName,password );
    }
    @PostMapping("/two-factor/otp/{otp}")
    public ResponseEntity<AuthResponse> verifySigingOtp(@PathVariable String otp,@RequestBody String id) throws Exception{
        TwoFactorOTP twoFactorOTP1=twoFactorOtpService.findById(id);
        if(twoFactorOtpService.verifyTwoFactorOTP(twoFactorOTP1, otp)){
            AuthResponse authResponse=new AuthResponse();
            authResponse.setMessage("two factor authentication success");
            authResponse.setTwoFactorAuthEnabled(true);
            authResponse.setJwt(twoFactorOTP1.getJwt());
            return new ResponseEntity<>(authResponse,HttpStatus.ACCEPTED);
        }
        throw new Exception("Invalid OTP");
    }
}
 