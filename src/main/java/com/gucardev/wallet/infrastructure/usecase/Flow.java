package com.gucardev.wallet.infrastructure.usecase;

// 1. Base Flow interface without params and return (void)
public interface Flow<T> {
    void execute();
}

