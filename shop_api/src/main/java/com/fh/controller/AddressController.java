package com.fh.controller;

import com.fh.model.po.Address;
import com.fh.service.AddressService;
import com.fh.util.login.LoginAnnotation;
import com.fh.util.noLogin.NoLogin;
import com.fh.util.response.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("address")
@CrossOrigin(maxAge = 3600,origins = "http://localhost:8083")
public class AddressController {
    @Autowired
    private AddressService addressService;
    @Autowired
    private RedisTemplate redisTemplate;
    @GetMapping
    @NoLogin
    public ResponseServer queryAddress(){
        List<Address> list= addressService.queryAddress();
        return ResponseServer.success(list);
    }
    @PutMapping
    @NoLogin
    public ResponseServer saveOrUpdate(Address address){
        addressService.saveOrUpdate(address);
        return ResponseServer.success();
    }
    @DeleteMapping
    @NoLogin
    public ResponseServer deleteAddress(String id){
        addressService.deleteAddress(id);
        return ResponseServer.success();
    }
    @GetMapping("queryAddressById")
    @NoLogin
    public ResponseServer queryAddressById(String id){
        Address address=addressService.queryAddressById(id);
        return ResponseServer.success(address);
    }

}
