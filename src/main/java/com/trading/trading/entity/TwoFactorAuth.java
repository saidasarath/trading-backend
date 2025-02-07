package com.trading.trading.entity;

import com.trading.trading.domain.verificationType;

import lombok.Data;

@Data
public class TwoFactorAuth {
    private boolean isEnable=false;
    private verificationType sendTo;
}
