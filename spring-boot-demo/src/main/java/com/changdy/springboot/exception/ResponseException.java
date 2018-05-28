package com.changdy.springboot.exception;


import com.changdy.springboot.enums.ResultEnums;

import java.text.MessageFormat;

/**
 * Created by Changdy on 2017/12/2.
 */
public class ResponseException extends RuntimeException {
    private Integer code;

    //只允许通过枚举类初始化code
    public ResponseException(ResultEnums resultEnums) {
        super(resultEnums.getMsg());
        this.code = resultEnums.getCode();
    }

    // 增加格式化
    public ResponseException(ResultEnums resultEnums, Object ... arguments) {
        super(MessageFormat.format(resultEnums.getMsg(), arguments));
        this.code = resultEnums.getCode();
    }

    public Integer getCode() {
        return code;
    }
}
