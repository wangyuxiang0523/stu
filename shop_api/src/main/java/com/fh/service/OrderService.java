package com.fh.service;

import com.fh.util.response.ResponseServer;

public interface OrderService {
    ResponseServer createOrder(Integer addressId, String phone) throws Exception;
}
