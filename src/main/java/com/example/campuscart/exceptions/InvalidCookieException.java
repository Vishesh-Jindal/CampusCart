package com.example.campuscart.exceptions;

public class InvalidCookieException extends RuntimeException{
    public InvalidCookieException(String message){
        super(message);
    }
}
