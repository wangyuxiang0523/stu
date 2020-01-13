package com.fh.controller;

import com.fh.service.OrderService;
import com.fh.util.login.LoginAnnotation;
import com.fh.util.noLogin.NoLogin;
import com.fh.util.response.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("order")
@CrossOrigin(maxAge = 3600,origins = "http://localhost:8083",exposedHeaders = "NOLOGIN")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping
    @NoLogin
    public ResponseServer createOrder(Integer addressId, HttpServletRequest request)  {
        String phone = (String) request.getAttribute("phone");
        try {
            return orderService.createOrder(addressId,phone);
        } catch (Exception e) {
           Exception e1= (Exception) e;
            return ResponseServer.error(333,e1.getMessage());
        }
    }
}
