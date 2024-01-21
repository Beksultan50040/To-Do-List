package com.example.todolist.exceptionHandler;

import com.example.todolist.exceptions.ApiError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = { ApiError.class })
    public ResponseEntity<String> appExceptionHandler(ApiError e){
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage());
        }

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return ResponseEntity.ok(apiError.getErrors().get(0));
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<String> missingArgument(MissingServletRequestParameterException e){
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Map<String, String> handleNotValidArgumentException(MethodArgumentNotValidException e){

        log.error(e.getMessage());

        Map<String, String> errorMap = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });

        return errorMap;
    }

    @ExceptionHandler({ HttpMessageNotReadableException.class })
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
