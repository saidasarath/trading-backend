package com.trading.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.trading.entity.TwoFactorOTP;

public interface TwoFactorOtpRepository extends JpaRepository<TwoFactorOTP, String> {
    TwoFactorOTP findByUserId(Long userid);
    
}
