package com.mervyn.response;


import com.mervyn.enums.ResponseCode;
import com.mervyn.exception.BusinessException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Getter;
import lombok.Setter;

import static com.mervyn.enums.ResponseCode.SUCCESS;


/**
 * @author cs-mervyn
 * @version 1.0
 * @date 2023/8/22 17:05
 */
@Getter
@Setter
@SuppressFBWarnings(value = {"PI_DO_NOT_REUSE_PUBLIC_IDENTIFIERS_CLASS_NAMES"}, justification = "Do not reuse public identifiers from the Java Standard Library")
public class Result<T> {
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 提示信息
     */
    private String message;
    /**
     * 数据
     */
    private T data;

    public Result() {

    }

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    public static Result success() {
        Result result = new Result();
        result.setCode(SUCCESS.getErrorCode());
        result.setMessage(SUCCESS.getErrorMsg());
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(SUCCESS.getErrorCode());
        result.setMessage(SUCCESS.getErrorMsg());
        result.setData(data);
        return result;
    }

    public static Result exceptionToResult(BusinessException de) {
        Result result = new Result();
        result.setCode(de.getErrorCode());
        result.setMessage(de.getErrorMsg());
        return result;
    }

    public static Result handleResponseCodeWithData(ResponseCode responseCode, Object data) {
        Result result = new Result();
        result.setMessage(responseCode.getErrorMsg());
        result.setCode(responseCode.getErrorCode());
        result.setData(data);
        return result;
    }

}

