package com.gucardev.wallet.infrastructure.usecase;

// 3. Flow interface with params but no return (void)
public interface FlowWithParams<P> {
    void execute(P params);
}
