package com.gucardev.wallet.infrastructure.usecase;

// 4. UseCase interface with both params and return value
public interface UseCaseWithParamsAndReturn<P, R> {
    R execute(P params);
}
