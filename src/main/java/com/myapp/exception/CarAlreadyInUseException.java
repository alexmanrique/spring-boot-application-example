package com.myapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "This car is already in use by another driver.")
public class CarAlreadyInUseException extends Exception
{
    static final long serialVersionUID = -2387516993335229948L;

    public CarAlreadyInUseException(String message)
    {
        super(message);
    }
}