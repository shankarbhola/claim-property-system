package com.app.claims.exception;

import com.app.claims.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerClass extends RuntimeException{

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<?> resourceNotFound(ResourceNotFound resourceNotFound, WebRequest webRequest){
        ErrorDto dto = new ErrorDto(resourceNotFound.getMessage(),new Date(),webRequest.getDescription(false));
        return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> GlobalExceptionHandler(Exception exception, WebRequest webRequest){
        ErrorDto dto = new ErrorDto(exception.getMessage(),new Date(), webRequest.getDescription(false));
        return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        return new ResponseEntity<>(Collections.singletonMap("message", errorMessage), HttpStatus.BAD_REQUEST);
    }

}