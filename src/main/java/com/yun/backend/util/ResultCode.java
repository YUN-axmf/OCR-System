package com.yun.backend.util;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS(200, "成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "无效令牌"),
    FORBIDDEN(403, "权限不足"),
    FAILED(500, "服务器错误"),
    BUSINESS_ERROR(600, "业务异常");

    private final Integer code;
    private final String message;

    private ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}