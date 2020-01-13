package com.fh.model.vo;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

public class PayLogVo {
    private String  outTradeNo;//支付单号

    private String  orderId;//支付单号

    private Integer  vipId;//购买人id

    private String  transactionId;//微信支付订单号

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//创建时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;//支付时间

    private BigDecimal payMoney ;//支付金额

    private Integer  payType;//支付方式

    private Integer  payStatus;//支付状态

    private String payStatusInfo;

    private String payTypeInfo;

}
