package com.gucardev.wallet.infrastructure.usecase;

// 3. UseCase interface with params but no return (void)
public interface UseCaseWithParams<P> {
    void execute(P params);
}
