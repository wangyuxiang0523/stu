package com.fh.service;

import com.fh.util.response.ResponseServer;

public interface PayService {
    ResponseServer createPayQRCode(String phone, String outTradeNo);

    ResponseServer queryPayStatus(String phone, String outTradeNo);

    ResponseServer queryPayLog(String phone);
}
