package com.gucardev.wallet.infrastructure.websocket.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum WebSocketAction {
    DO_SOMETHING_1(1, "DO_SOMETHING_1"),
    DO_SOMETHING_2(2, "DO_SOMETHING_2"),
    DO_SOMETHING_3(3, "DO_SOMETHING_3"),

    ERROR(99, "ERROR");

    private final int code;
    private final String desc;

    private static final Map<Integer, WebSocketAction> ACTIONS_BY_CODE =
            Arrays.stream(values())
                    .collect(Collectors.toMap(action -> action.code, Function.identity()));

    public static WebSocketAction fromCode(int code) {
        return ACTIONS_BY_CODE.getOrDefault(code, ERROR);
    }
}