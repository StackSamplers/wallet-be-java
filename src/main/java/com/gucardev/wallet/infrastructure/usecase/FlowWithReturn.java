package com.gucardev.wallet.infrastructure.usecase;

// 2. Flow interface with return value
public interface FlowWithReturn<R> {
    R execute();
}
