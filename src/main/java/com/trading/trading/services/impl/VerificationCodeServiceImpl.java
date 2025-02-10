package com.trading.trading.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.trading.trading.domain.verificationType;
import com.trading.trading.entity.User;
import com.trading.trading.entity.VerificationCode;
import com.trading.trading.repository.VerificationCodeRepository;
import com.trading.trading.services.VerificationCodeSerivce;
import com.trading.trading.utils.OtpUtils;

public class VerificationCodeServiceImpl implements VerificationCodeSerivce{

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;
    @Override
    public VerificationCode sendVerificationCode(User user, verificationType verificationType) {
       VerificationCode verificationCode = new VerificationCode();
       verificationCode.setOtp(OtpUtils.generateOTP());
       verificationCode.setVerificationType(verificationType);
       verificationCode.setUser(user);
       return  verificationCodeRepository.save(verificationCode);
    }

    @Override
    public VerificationCode getVerificationCodeById(Long id) {
        Optional<VerificationCode> verificationCode = verificationCodeRepository.findById(id);
        if(verificationCode.isPresent()){
            return verificationCode.get();
        }
        throw new RuntimeException("Verification code not found");
    }


    @Override
    public void deleteVerificationCode(VerificationCode verificationCode) {
        verificationCodeRepository.delete(verificationCode);
    }

    

    @Override
    public VerificationCode getVerificationCodeByUser(Long userId) {
        return verificationCodeRepository.findByUserId(userId);
    }

  
    
}
