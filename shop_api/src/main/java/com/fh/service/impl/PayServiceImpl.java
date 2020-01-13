package com.fh.service.impl;

import com.fh.dao.LoginDao;
import com.fh.dao.OrderDao;
import com.fh.dao.PayLogDao;
import com.fh.model.PayLog;
import com.fh.model.po.Order;
import com.fh.model.po.OrderInfo;
import com.fh.model.po.VipPo;
import com.fh.service.PayService;
import com.fh.util.response.ResponseServer;
import com.fh.util.response.ServerEnum;
import com.fh.util.wxPayUtil.DateUtil;
import com.fh.util.wxPayUtil.MyWxConfig;
import com.github.wxpay.sdk.WXPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PayServiceImpl implements PayService {
    @Autowired
    private PayLogDao payLogDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private LoginDao loginDao;
    @Override
    public ResponseServer createPayQRCode(String phone, String outTradeNo) {
        MyWxConfig myWxConfig =new MyWxConfig();
        //根据手机号查出未支付的订单信息
        PayLog o = (PayLog) redisTemplate.opsForHash().get("pay_order" + phone, outTradeNo);
        if(o==null){
            ResponseServer.error(ServerEnum.NO_ORDER_PAY);
        }
        try {
            //调微信接口
            WXPay wxPay=new WXPay(myWxConfig);
            Map<String,String> map = new HashMap<String, String>();
            map.put("body","一给我力giao");
            map.put("out_trade_no",outTradeNo);
            map.put("fee_type","CNY");
            Date date=org.apache.commons.lang3.time.DateUtils.addMinutes(new Date(),2);
            map.put("time_expire",DateUtil.getYyyyMMddhhmmss(date,DateUtil.yyyyMMddhhmmss));
            /*int payMoney =BigDecimalUtil.mul(o.getTotalPrice()+",","100").intValue();*/
            int payMoney=1;
            map.put("total_fee",payMoney+"");
            map.put("trade_type","NATIVE");
            map.put("notify_url","http://www.example.com/wxpay/notify");

            Map<String, String> stringStringMap = wxPay.unifiedOrder(map);
            String returnCode = stringStringMap.get("return_code");
            String returnMsg = stringStringMap.get("return_msg");
            if(!"SUCCESS".equalsIgnoreCase(returnCode)){
                return ResponseServer.error(5000,returnMsg);
            }
            String resultCode = stringStringMap.get("result_code");
            String errCodeDes = stringStringMap.get("err_code_des");
            if(!"SUCCESS".equalsIgnoreCase(resultCode)){
                return ResponseServer.error(5000,errCodeDes);
            }
            String codeUrl = stringStringMap.get("code_url");
            Map<String,Object> map1 = new HashMap<String,Object>();
            map1.put("codeUrl",codeUrl);
            map1.put("outTradeNO",outTradeNo);
            map1.put("payMoney",o.getPayMoney());
            return ResponseServer.success(map1);
        } catch (Exception e) {
            return ResponseServer.error(ServerEnum.CREATE_PAY_QRCODE_ERROR);
        }



    }

    @Override
    public ResponseServer queryPayStatus(String phone, String outTradeNo) {
        MyWxConfig myWxConfig = new MyWxConfig();
        PayLog o =(PayLog) redisTemplate.opsForHash().get("pay_order" + phone, outTradeNo);
        int count = 0;
        while (true){
            count++;
            try {
                WXPay wxPay =new WXPay(myWxConfig);
                //定义存放参数的Map
                Map<String,String> data = new HashMap<>();
                data.put("out_trade_no",outTradeNo);
                Map<String, String> responsResult = wxPay.orderQuery(data);
                String returnCode = responsResult.get("return_code");
                String returnMessage = responsResult.get("return_msg");
                if(!"SUCCESS".equalsIgnoreCase(returnCode)){
                    return ResponseServer.error(5000,returnMessage);
                }
                //验证业务是否成功
                String resultCode = responsResult.get("result_code");
                //错误描述
                String errCodeDes = responsResult.get("err_code_des");
                if(!"SUCCESS".equalsIgnoreCase(resultCode)){
                    return ResponseServer.error(5000,errCodeDes);
                }
                String tradeState = responsResult.get("trade_state");
                if("SUCCESS".equalsIgnoreCase(tradeState)){
                    //更新支付记录表记录
                    PayLog payLog = new PayLog();
                    payLog.setPayTime(new Date());
                    String transactionId = responsResult.get("transaction_id");
                    payLog.setTransactionId(transactionId);
                    payLog.setOutTradeNo(outTradeNo);
                    payLog.setPayStatus(ServerEnum.ORDER_STATUS_SUCCESS.getCode());
                    payLogDao.updateById(payLog);
                    //更新订单记录
                    Order order =new Order();
                    order.setId(o.getOrderId());
                    order.setPayTime(new Date());
                    order.setStatus(ServerEnum.ORDER_STATUS_SUCCESS.getCode());
                    orderDao.updateById(order);
                   redisTemplate.opsForHash().delete("pay_order"+phone,outTradeNo);
                   return ResponseServer.success();
                }
                Thread.sleep(3000L);
                if (count>50){
                    return ResponseServer.error(ServerEnum.ORDER_PAY_TIMEOUT);
                }
            } catch (Exception e) {
               return ResponseServer.error(ServerEnum.CREATE_PAY_QRCODE_ERROR);
            }
        }
    }
    @Override
    public ResponseServer queryPayLog(String phone) {

         Map<String,Object> map =new HashMap<>();
         VipPo vipPo=loginDao.queryIdByPhone(phone);
         List<Order> order =payLogDao.selectOrderByVipId(vipPo.getId());
         List<PayLog> list =new ArrayList<>();
         List<OrderInfo> orderInfos= new ArrayList<>();
        Integer count =0;
        for (int i=0;i<order.size();i++){
            count++;
            List<OrderInfo> orderInfo =payLogDao.queryOrderInfo(order.get(i).getId());
            orderInfos.addAll(orderInfo);
            PayLog payLogs = payLogDao.selectListByVipId(order.get(i).getId());
            list.add(payLogs);
        }
       /* map1.put("orderInfo",orderInfo);*/
        map.put("orderInfo",orderInfos);
        map.put("payLogs",list);
        map.put("count",count);
        map.put("order",order);
        return ResponseServer.success(map);
    }
}
