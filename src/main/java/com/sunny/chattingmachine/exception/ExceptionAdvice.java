package com.sunny.chattingmachine.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity handleBaseException(BaseException exception) {
        log.error("BaseException errorMessage() : {}", exception.getExceptionType().getErrorMessage());
        log.error("BaseException errorCode() : {}", exception.getExceptionType().getErrorCode());
        return new ResponseEntity(new ExceptionDto(exception.getExceptionType().getErrorCode()),
                exception.getExceptionType().getHttpStatus());
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity handleValidException(BindException exception){
        log.error("ValidException thrown : {}", exception.getMessage());
        return new ResponseEntity(new ExceptionDto(2000), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity httpMessageNotReadableException(HttpMessageNotReadableException exception){
        log.error("Json parsing exception thrown {}", exception.getMessage());
        return new ResponseEntity(new ExceptionDto(3000), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleAccountException(Exception exception){
        exception.printStackTrace();
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }


    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    static class ExceptionDto {
        private Integer errorCode;
    }
}
