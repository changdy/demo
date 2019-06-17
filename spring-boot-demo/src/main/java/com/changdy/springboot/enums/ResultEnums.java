package com.changdy.springboot.enums;

import com.changdy.springboot.consts.HttpExceptionCode;
import lombok.Getter;
import lombok.ToString;

import static com.changdy.springboot.consts.HttpExceptionCode.PARAMETER_ERROR;

@Getter
@ToString
public enum ResultEnums {
    UNKNOWN_ERROR(HttpExceptionCode.UNKNOWN_ERROR, "系统错误"),
    SUCCEED(HttpExceptionCode.SUCCEED, "success"),
    MISSING_PARAMETER(PARAMETER_ERROR, "缺少参数:{0}"),
    METHOD_NOT_SUPPORTED(PARAMETER_ERROR, "请求类型不支持"),
    REQUEST_BODY_ERROR(PARAMETER_ERROR, "body 缺少或转换失败");
    private Integer code;
    private String msg;

    ResultEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}