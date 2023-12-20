package com.mervyn.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.mervyn.enums.ResponseCode;
import com.mervyn.response.Result;
import java.util.stream.Collectors;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author cs-mervyn
 * @version 1.0
 * @date 2023/8/24 12:59
 */
@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(BAD_REQUEST)
    public Result businessExceptionHandler(BusinessException exception) {
        return Result.exceptionToResult(exception);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        String errorMessage = result.getFieldErrors().stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.joining(";"));
        return Result.handleResponseCodeWithData(ResponseCode.REQUEST_PARAM_INVALID, errorMessage);
    }
}
