package com.trading.trading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.trading.trading.domain.verificationType;
import com.trading.trading.entity.User;
import com.trading.trading.entity.VerificationCode;
import com.trading.trading.services.EmailService;
import com.trading.trading.services.UserService;
import com.trading.trading.services.VerificationCodeSerivce;

import jakarta.mail.MessagingException;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private VerificationCodeSerivce verificationCodeService;
    @GetMapping("/api//user/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt){
        User user = userService.findUserProfileByJwt(jwt);
        
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
    @PostMapping("/api/users/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOtp(@RequestHeader("Authorization") String jwt, @PathVariable verificationType verificationType){
        User user = userService.findUserByEmail(jwt);
        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());
        if(verificationCode  == null){
            verificationCode = verificationCodeService.sendVerificationCode(user, verificationType);
        }
        if(verificationCode.equals(verificationType.EMAIL)){
            try {
                emailService.sendverificationEmail(user.getEmail(), verificationCode.getOtp());
            } catch (MessagingException e) {
                return new ResponseEntity<String>("Failed to send verification email", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<String>("verification otp sent successfully", HttpStatus.OK);
    }
    @PatchMapping("/api/users/enable-two-factor/verify-opt/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication (
    @PathVariable String otp,    
    @RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserByEmail(jwt);
        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());
        String sendTo = verificationCode.getVerificationType().equals(verificationType.EMAIL) ? verificationCode.getEmail() : verificationCode.getMobile();
        boolean isVerified =  verificationCode.getOtp().equals(otp);
        if(isVerified){
            User updatedUser = userService.enableTwoFactorAuthentication(verificationCode.getVerificationType(), sendTo, user);
            verificationCodeService.deleteVerificationCode(verificationCode);
            return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
        }
        throw new Exception("Invalid OTP");
    }
    
}
