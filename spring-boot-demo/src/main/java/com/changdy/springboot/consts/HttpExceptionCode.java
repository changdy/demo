package com.changdy.springboot.consts;

/**
 * Created by Changdy on 2019/6/17.
 */
public interface HttpExceptionCode {
    // 未知异常放这里
    int UNKNOWN_ERROR = -1;
    // 成功
    int SUCCEED = 0;
    // 权限问题
    int AUTHORITY_ERROR = 1;
    //参数异常,包含确少或者转换失败
    int PARAMETER_ERROR = 51;
    // 业务部分异常放这里
    int BUSINESS_ERROR = 101;
    // 接口成功,但是需要提示部分信息
    int BUSINESS_CONTINUE = 102;
}