package com.trading.trading.services;

import com.trading.trading.entity.TwoFactorOTP;
import com.trading.trading.entity.User;

public interface TwoFactorOtpService {
    TwoFactorOTP createTwoFactorOTP(User user,String otp,String jwt);
    TwoFactorOTP findByUser(Long userid);
    TwoFactorOTP findById(String id);
    boolean verifyTwoFactorOTP(TwoFactorOTP twoFactorOTP,String otp);
    void deleteTwoFactorOTP(TwoFactorOTP twoFactorOTP);
}
