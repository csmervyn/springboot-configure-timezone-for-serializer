package com.mervyn.exception;

import com.mervyn.enums.ResponseCode;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import java.io.Serial;

/**
 * @author cs-mervyn
 * @version 1.0
 * @date 2023/8/24 12:44
 */
@Setter
@Getter
public class BusinessException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = -3499333411807040979L;
    protected Integer errorCode;
    protected String errorMsg;

    public BusinessException() {
    }

    public BusinessException(ResponseCode responseCode) {
        this.errorCode = responseCode.getErrorCode();
        this.errorMsg = responseCode.getErrorMsg();
    }

    public BusinessException(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}

