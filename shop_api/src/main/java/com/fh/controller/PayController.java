package com.fh.controller;

import com.fh.service.PayService;
import com.fh.util.login.LoginAnnotation;
import com.fh.util.noLogin.NoLogin;
import com.fh.util.response.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("pay")
@CrossOrigin(maxAge = 3600,origins = "http://localhost:8083")
public class PayController {
    @Autowired
    private PayService payService;
    @GetMapping
    @NoLogin
    public ResponseServer createPayQRCode(HttpServletRequest request,String outTradeNo){
        String phone = (String) request.getAttribute("phone");
        return payService.createPayQRCode(phone,outTradeNo);
    }
    @PostMapping
    @NoLogin
    public ResponseServer queryPayStatus(HttpServletRequest request,String outTradeNo){
        String phone = (String) request.getAttribute("phone");
        return payService.queryPayStatus(phone,outTradeNo);
    }
    @GetMapping("showPayLog")
    @NoLogin
    public ResponseServer queryPayLog(HttpServletRequest request){
        String phone = (String) request.getAttribute("phone");
        return payService.queryPayLog(phone);
    }
}
