package com.fh.controller;

import com.fh.service.TypeService;
import com.fh.util.noLogin.NoLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("type")
@CrossOrigin(maxAge = 3600,origins = "http://localhost:8083")
public class TypeController {
    @Autowired
    private TypeService typeService;
    @GetMapping
    @NoLogin
    public List<Map<String,Object>> queryType(){
        return typeService.queryTypeList();
    }
}
