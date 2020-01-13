package com.fh.service.impl;

import com.fh.dao.ProductDao;
import com.fh.model.Cars;
import com.fh.model.vo.ProductDetailsVo;
import com.fh.service.CarsService;
import com.fh.util.response.ResponseServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CarsServiceImpl implements CarsService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ProductDao productDao;

    @Override
    public ResponseServer addCars(String phone, Integer id, Integer count) {
        //根据商品id查出 商品信息
          ProductDetailsVo productDetailsVo= productDao.queryProductDetailsById(id);
          Cars cars=new Cars();
          //加入购物车的商品数量
          //判断购物车是否已经存在 有的话 原有数量上加
          if(redisTemplate.opsForHash().hasKey("order_"+phone,"cars_"+id)){
              Cars c =(Cars) redisTemplate.opsForHash().get( "order_"+phone, "cars_" + id);
              cars.setCount(c.getCount()+count);
          }else{
              cars.setCount(count);
          }
          //防止 精度丢失
          BigDecimal bigDecimal=BigDecimal.valueOf(0.00);
          BigDecimal count1 =new BigDecimal(cars.getCount());
          //商品数量X价格 算出 小计金额
          BigDecimal subTotal = bigDecimal.add(productDetailsVo.getPrice().multiply(count1));
          cars.setSubTotal(subTotal);
          cars.setId(productDetailsVo.getId());
          cars.setPrice(productDetailsVo.getPrice());
          cars.setImg(productDetailsVo.getImg());
          cars.setName(productDetailsVo.getName());
     /*     redisTemplate.opsForHash().put(phone,"product_"+id,cars);*/
          redisTemplate.opsForHash().put("order_"+phone,"cars_"+id,cars);
        Long size = redisTemplate.opsForHash().size("order_"+phone);
        return ResponseServer.success(size);
    }

    @Override
    public Map<String, Object> showCars(String phone) {
       /* String carId =(String) redisTemplate.opsForValue().get(phone);*/
        List<Cars> carsList = redisTemplate.opsForHash().values("order_"+phone);
        BigDecimal bigDecimal= new BigDecimal(0.00);
        for (Cars cars:carsList) {
            bigDecimal=bigDecimal.add(cars.getSubTotal());
        }
        Map<String,Object> map =new HashMap<>();
        map.put("total",bigDecimal);
        map.put("carList",carsList);
        return map;
    }

    @Override
    public ResponseServer addProductNum(String phone, Integer id) {
        Cars cars =(Cars) redisTemplate.opsForHash().get("order_"+phone,"product_"+id);
        cars.setCount(cars.getCount()+1);
        cars.setSubTotal(cars.getPrice().multiply(new BigDecimal(cars.getCount())));
        redisTemplate.opsForHash().put("order_"+phone,"cars_"+id,cars);
        return ResponseServer.success();
    }

    @Override
    public ResponseServer deleteProductNum(String phone, Integer id) {
        Cars cars =(Cars) redisTemplate.opsForHash().get("order_"+phone, "cars_"+id);
        cars.setCount(cars.getCount()-1);
        cars.setSubTotal(cars.getPrice().multiply(new BigDecimal(cars.getCount())));
        redisTemplate.opsForHash().put("order_"+phone,"cars_"+id,cars);
        return ResponseServer.success();
    }

    @Override
    public ResponseServer deleteCarsProduct(String phone, String ids) {
        String[] split = ids.split(",");
        for (int i=0;i<split.length;i++){
            redisTemplate.opsForHash().delete("order_"+phone,"cars_"+split[i]);
        }
        return ResponseServer.success();
    }

    @Override
    public Map<String, Object> showOrder(String phone) {
        List<Cars> carsList = redisTemplate.opsForHash().values(phone);
        BigDecimal bigDecimal= new BigDecimal(0.00);
        for (Cars cars:carsList) {
            bigDecimal=bigDecimal.add(cars.getSubTotal());
        }
        Map<String,Object> map =new HashMap<>();
        map.put("total",bigDecimal);
        map.put("carList",carsList);
        return map;
    }
}
