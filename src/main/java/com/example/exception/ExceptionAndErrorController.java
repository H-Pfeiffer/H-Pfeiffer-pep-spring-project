package com.example.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAndErrorController {
    
    @ExceptionHandler(ConstraintViolationException.class) // i.e. violating uniqueness constraint
    @ResponseStatus(HttpStatus.CONFLICT) // CONFLICT returns a 409 status code response
    public @ResponseBody String usernameAlreadyExistsException(ConstraintViolationException ex){ return ex.getMessage(); }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // BAD_REQUEST returns a 400 client_error response
    public @ResponseBody String invalidUserInputException(IllegalArgumentException ex) { return ex.getMessage(); }
}
