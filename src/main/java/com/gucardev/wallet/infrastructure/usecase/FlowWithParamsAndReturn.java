package com.gucardev.wallet.infrastructure.usecase;

// 4. Flow interface with both params and return value
public interface FlowWithParamsAndReturn<P, R> {
    R execute(P params);
}
