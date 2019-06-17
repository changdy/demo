package com.changdy.springboot.util;


import com.changdy.springboot.enums.ResultEnums;
import com.changdy.springboot.model.ResponseResult;

import java.text.MessageFormat;

public class ResultUtil {

    public static <T> ResponseResult<T> success(T object) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(ResultEnums.SUCCEED.getCode());
        responseResult.setMsg(ResultEnums.SUCCEED.getMsg());
        responseResult.setData(object);
        return responseResult;
    }

    public static ResponseResult success() {
        return success(null);
    }

    public static <T> ResponseResult<T> error(ResultEnums resultEnums) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(resultEnums.getCode());
        responseResult.setMsg(resultEnums.getMsg());
        return responseResult;
    }

    public static <T> ResponseResult<T> error(ResultEnums resultEnums, Object... obj) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(resultEnums.getCode());
        responseResult.setMsg(MessageFormat.format(resultEnums.getMsg(), obj));
        return responseResult;
    }


    public static ResponseResult error(Integer code, String msg) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(code);
        responseResult.setMsg(msg);
        return responseResult;
    }

    public static <T> ResponseResult<T> error() {
        return error(ResultEnums.UNKNOWN_ERROR);
    }
}