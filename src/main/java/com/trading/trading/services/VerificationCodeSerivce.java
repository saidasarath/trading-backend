package com.trading.trading.services;

import com.trading.trading.domain.verificationType;
import com.trading.trading.entity.User;
import com.trading.trading.entity.VerificationCode;

public interface VerificationCodeSerivce {
    VerificationCode sendVerificationCode(User user,verificationType verificationType);
    VerificationCode getVerificationCodeById(Long id);
    VerificationCode getVerificationCodeByUser(Long userId);
    void deleteVerificationCode(VerificationCode verificationCode);
}
