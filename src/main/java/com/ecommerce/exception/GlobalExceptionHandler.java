package com.ecommerce.exception;

import com.ecommerce.dtos.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // handler resource not found Exception

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){

        logger.info("Exception Handler Invoked !!");
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message(ex.getMessage()).status(HttpStatus.NOT_FOUND).success(true).build();
        return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
    }


    // MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){

        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors(); // multiple Error Find in List

        Map<String, Object> response = new HashMap<>(); // List Error is retrive one-by-one and store in the form of key and value in map

        allErrors.stream().forEach(objectError -> {        // object error (Obj)--> one by one is retrived key And value (message, field)
            String message = objectError.getDefaultMessage(); // object --> String format error gives(i.e message)
            String field = ((FieldError) objectError).getField();// object --> key format error gives(i.e field) but before typecasting takes field Error
            response.put(field,message);
        });
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // handle Bad Api exception
    @ExceptionHandler(BadApiRequestException.class)
    public ResponseEntity<ApiResponseMessage> handleBadApiRequest(BadApiRequestException ex){

        logger.info("Bad Api Request!!");
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message(ex.getMessage()).status(HttpStatus.BAD_REQUEST).success(false).build();
        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }


}
