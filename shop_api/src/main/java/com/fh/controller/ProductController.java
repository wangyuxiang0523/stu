package com.fh.controller;

import com.fh.model.vo.ProductVo;
import com.fh.service.ProductService;
import com.fh.util.noLogin.NoLogin;
import com.fh.util.response.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("product")
// maxAge 准备响应前的缓存持续的最大时间（以秒为单位）
@CrossOrigin(maxAge = 3600,origins = "http://localhost:8083")
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping
    @NoLogin
    public Map<String,Object> queryProductPage(String id){
        Map <String,Object> map= new HashMap<>();
        map.put("code","200");
        map.put("message","success");
        List<ProductVo>list=productService.queryProductList(id);
        map.put("data",list);
        return map;
    }
    @GetMapping("hot")
    @NoLogin
    public ResponseServer queryProductHot(){
        Map<String,Object> map= new HashMap<>();
        List<ProductVo> list= productService.queryProductHotList();
        return ResponseServer.success(list);
    }
    @PostMapping("queryAllProductData")
    @NoLogin
    public ResponseServer queryAllProductData(){
        return ResponseServer.success();
    }
    @PostMapping("queryProductDataByPage")
    @NoLogin
    public ResponseServer queryProductDataByPage(Integer limit,Integer curr){
        try {
            //处理参数
            Map map=new HashMap();
            map.put("curr",curr);//返回值 用的
            map.put("size",limit);// mybatis、 映射文件中的变量 取数据的条数
            map.put("startNumber",(curr-1)*limit);//mybatis、 映射文件中的变量 开始下标
            Map pageData = productService.queryProductPageData(map);
            return  ResponseServer.success(pageData);
        }catch (Exception e){
            return ResponseServer.error();
        }
    };


}
