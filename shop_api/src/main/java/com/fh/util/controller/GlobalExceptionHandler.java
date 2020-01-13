package com.fh.util.controller;

import com.fh.util.exception.AuthenticateException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticateException.class)
    public void authenticateException(AuthenticateException e, HttpServletRequest request, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8083");
        response.setHeader("NOLOGIN",e.getCode().toString());
            response.setHeader("Access-Control-Expose-Headers", "NOLOGIN");

    }

    @ExceptionHandler(Exception.class)
    public void exceptionHandler(Exception e,HttpServletRequest request, HttpServletResponse response){
            response.setHeader("EXCEPTION",e.getMessage());

    }
}
