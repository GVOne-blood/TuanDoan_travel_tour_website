package com.javaadvancedg9.JavaAdvancedG9.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(Exception e, WebRequest request){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());

        String message = e.getMessage();
        if (e instanceof MethodArgumentNotValidException && (message.contains("[") && message.contains("]")) ) {
            message = message.substring(message.lastIndexOf('['), message.lastIndexOf(']'));}
        else if (e instanceof HttpMessageNotReadableException  && (message.contains("[") && message.contains("]"))) {

            message = message.substring(message.indexOf('[') + 1, message.indexOf(']'));}
        errorResponse.setMessage(message); // lấy ra lỗi ở dạng text
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase()); // lấy ra lỗi ở dạng text (bad-request)
        errorResponse.setTimestamp( new Date());
        errorResponse.setPath(request.getDescription(false ).replace("uri=", "")); // lấy ra đường dẫn request
        return errorResponse;
    }

    // handle exception when some field in param is not valid
    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus (HttpStatus.BAD_GATEWAY)
    public ErrorResponse handleParamException(Exception e, WebRequest request){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_GATEWAY.value());

        String message = e.getMessage();
        //message.substring(message.lastIndexOf('.') + 1);
        errorResponse.setMessage(message); // lấy ra lỗi ở dạng text
        errorResponse.setError(HttpStatus.BAD_GATEWAY.getReasonPhrase()); // lấy ra lỗi ở dạng text (bad-getaway)
        errorResponse.setTimestamp( new Date());
        errorResponse.setPath(request.getDescription(false ).replace("url=", "")); // lấy ra đường dẫn request
        return errorResponse;
    }

}
