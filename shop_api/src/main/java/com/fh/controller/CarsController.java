package com.fh.controller;

import com.fh.model.vo.ProductDetailsVo;
import com.fh.service.CarsService;
import com.fh.service.ProductService;
import com.fh.util.login.LoginAnnotation;
import com.fh.util.noLogin.NoLogin;
import com.fh.util.response.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("cars")
////exposedHeaders 用户代理将允许客户端根据实际响应访问的响应标头列表，而不是“简单”标头
@CrossOrigin(maxAge = 3600,origins = "http://localhost:8083",exposedHeaders = "NOLOGIN")
public class CarsController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CarsService carsService;
    @PostMapping("productDetails")
    public ResponseServer productDetails(Integer id){
       ProductDetailsVo productDetailsVo= productService.queryProductDetailsById(id);
       return ResponseServer.success(productDetailsVo);
    }
    @NoLogin
    @PostMapping("addCars")
    public ResponseServer addCars(HttpServletRequest request,Integer id, Integer count){
        String phone = (String) request.getAttribute("phone");
        return carsService.addCars(phone, id, count);
    }
    @NoLogin
    @PostMapping("toshopCart")
    public ResponseServer toShopCar(){
        return ResponseServer.success();
    }
    @NoLogin
    @GetMapping("showCars")
    public ResponseServer showCars(HttpServletRequest request){
        String phone = (String) request.getAttribute("phone");
        Map<String,Object> map=carsService.showCars(phone);
        return ResponseServer.success(
                map);
    }
    @NoLogin
    @GetMapping("showOrder")
    public ResponseServer showOrder(HttpServletRequest request){
        String phone = (String) request.getAttribute("phone");
        Map<String,Object> map=carsService.showCars(phone);
        return ResponseServer.success(map);
    }

    @NoLogin
    @PostMapping("addProductNum")
    public ResponseServer addProductNum(HttpServletRequest request,Integer id){
        String phone = (String) request.getAttribute("phone");

        return carsService.addProductNum(phone,id);
    }
    @NoLogin
    @PostMapping("deleteProductNum")
    public ResponseServer deleteProductNum(HttpServletRequest request,Integer id){
        String phone = (String) request.getAttribute("phone");

        return carsService.deleteProductNum(phone,id);
    }
    @NoLogin
    @DeleteMapping("deleteCarsProduct")
    public ResponseServer deleteCarsProduct(HttpServletRequest request,String ids){
        String phone = (String) request.getAttribute("phone");

        return carsService.deleteCarsProduct(phone,ids);
    }
}
