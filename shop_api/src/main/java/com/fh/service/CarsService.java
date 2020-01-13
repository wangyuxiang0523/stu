package com.fh.service;

import com.fh.util.response.ResponseServer;

import java.util.Map;

public interface CarsService {
    ResponseServer addCars(String phone, Integer id, Integer count);

    Map<String,Object> showCars(String phone);

    ResponseServer addProductNum(String phone, Integer id);

    ResponseServer deleteProductNum(String phone, Integer id);

    ResponseServer deleteCarsProduct(String phone, String ids);

    Map<String,Object> showOrder(String phone);
}
