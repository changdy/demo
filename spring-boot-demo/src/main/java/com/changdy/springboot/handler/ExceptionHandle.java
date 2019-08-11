package com.changdy.springboot.handler;


import com.changdy.springboot.enums.ResultEnums;
import com.changdy.springboot.exception.ResponseException;
import com.changdy.springboot.model.ResponseResult;
import com.changdy.springboot.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static com.changdy.springboot.consts.HttpExceptionCode.PARAMETER_ERROR;

@Slf4j
@ResponseBody
@ControllerAdvice
public class ExceptionHandle {


    @ExceptionHandler(ResponseException.class)
    public ResponseResult handle(ResponseException exception) {
        log.info(exception.getMessage());
        return ResultUtil.error(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult handle(Exception e) {
        log.error("未被捕捉异常:", e);
        return ResultUtil.error(ResultEnums.UNKNOWN_ERROR.getCode(), ResultEnums.UNKNOWN_ERROR.getMsg() + "[" + e.getClass().getName() + ": " + e.getLocalizedMessage() + "]");
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult handle(MethodArgumentNotValidException e) {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        StringBuilder errorMsg = new StringBuilder();
        for (ObjectError error : errors) {
            if (error instanceof FieldError) {
                errorMsg.append(((FieldError) error).getField());
            } else {
                errorMsg.append(error.getObjectName());
            }
            errorMsg.append(":").append(error.getDefaultMessage()).append(",");
        }
        return ResultUtil.error(PARAMETER_ERROR, errorMsg.toString());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseResult handle(MissingServletRequestParameterException e) {
        log.error(ResultEnums.MISSING_PARAMETER.getMsg(), e);
        return ResultUtil.error(ResultEnums.MISSING_PARAMETER, e.getParameterName());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseResult handle(HttpMessageNotReadableException e) {
        log.error(ResultEnums.REQUEST_BODY_ERROR.getMsg(), e);
        return ResultUtil.error(ResultEnums.REQUEST_BODY_ERROR);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseResult handle(HttpRequestMethodNotSupportedException e) {
        return ResultUtil.error(ResultEnums.METHOD_NOT_SUPPORTED);
    }
}
