package com.trading.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trading.trading.entity.VerificationCode;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode,Long> {
    public VerificationCode findByUserId(Long userId);
    
}
