package com.mervyn.enums;

import lombok.Getter;

/**
 * @author cs-mervyn
 * @version 1.0
 * @date 2023/8/22 17:10
 */
@Getter
public enum ResponseCode {
    /* 数据操作错误定义*/
    SUCCESS(10000, "success"),
    REQUEST_PARAM_INVALID(20000, "请求参数不合法"),
    NO_PERMISSION(403, "你没得权限"),
    NO_AUTH(401, "你能不能先登录一下"),
    NOT_FOUND(404, "未找到该资源!"),
    INTERNAL_SERVER_ERROR(500, "服务器跑路了"),
    REENTER_PASSWORD_NOT_EQUAL_PASSWORD(40001, "密码与确认密码不相同"),
    USER_ALREADY_EXISTED(40002, "该账号已存在"),
    ACCOUNT_OR_PASSWORD_ERROR(40003, "账号或密码错误"),
    NEED_LOGIN(3009, "登录已失效，请重新登录"),
    UNAUTHORIZED(3008, "未授权的访问"),
    TOKEN_EXPIRED(3007, "Token过期"),
    TOKEN_MALFORMED(3006, "Token非法"),
    TOKEN_SIGNATURE_ERROR(3005, "Token签名错误"),
    TOKEN_ILLEGAL_ARGUMENT(3004, "Token内容解析异常"),
    TOKEN_UNSUPPORTED(3003, "Token异常"),
    TAG_ALREADY_EXISTED(50001, "该标签已存在"),

    ;

    /**
     * 错误码
     */
    private Integer errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    ResponseCode(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
