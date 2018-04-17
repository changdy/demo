package com.changdy.springboot.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ResultEnums {
    UNKNOWN_ERROR(-1, "系统错误"),
    SUCCESS(0, "success");

    private Integer code;
    private String msg;

    ResultEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}