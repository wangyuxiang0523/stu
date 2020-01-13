package com.fh.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fh.dao.OrderDao;
import com.fh.dao.OrderInfoDao;
import com.fh.dao.PayLogDao;
import com.fh.dao.ProductDao;
import com.fh.model.Cars;
import com.fh.model.PayLog;
import com.fh.model.po.Order;
import com.fh.model.po.OrderInfo;
import com.fh.model.po.VipPo;
import com.fh.model.vo.ProductDetailsVo;
import com.fh.service.OrderService;
import com.fh.util.response.ResponseServer;
import com.fh.util.response.ServerEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderInfoDao orderInfoDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private PayLogDao payLogDao;
    @Override
    public ResponseServer createOrder(Integer addressId, String phone) throws Exception {

        List<Cars> cars = redisTemplate.opsForHash().values("order_"+phone);
        if(cars.isEmpty()){
            return ResponseServer.error(ServerEnum.CARS_ORDER_NULL);
        }
        VipPo vipPo =(VipPo) redisTemplate.opsForValue().get("vip_" + phone);
        BigDecimal totalPrice=new BigDecimal(0.00);
        Integer count = new Integer(0);
        //生成全局的订单号
        String orderId = IdWorker.getIdStr();
        //生成支付单号
        String outTradeNo = IdWorker.getIdStr();
        for (Cars cars1:cars){
            count++;
            totalPrice = totalPrice.add(cars1.getSubTotal());
            Integer id = cars1.getId();
            ProductDetailsVo productDetailsVo = productDao.queryProductDetailsById(id);

            if(productDetailsVo.getStock() >= cars1.getCount() ){
                Integer jian = productDao.jian(cars1.getCount(),cars1.getId());
                if(jian.equals(0)){
                    throw new Exception(cars1.getName()+"库存不够");
                }else {
                    OrderInfo orderInfo = new OrderInfo();
                    orderInfo.setProductId(cars1.getId());
                    orderInfo.setCount(cars1.getCount());
                    orderInfo.setImg(cars1.getImg());
                    orderInfo.setProductName(cars1.getName());
                    orderInfo.setTotalPrice(cars1.getSubTotal());
                    orderInfo.setProductPrice(cars1.getPrice());
                    orderInfo.setCreateTime(new Date());
                    orderInfo.setOrderId(orderId);
                    orderInfoDao.insert(orderInfo);
                }
            }else {
                throw new Exception(cars1.getName()+"库存不够");
            }
        }
        Order order=new Order();
        order.setId(orderId);
        order.setAddressId(addressId);
        order.setCreateTime(new Date());
        order.setPayType(1);
        order.setStatus(ServerEnum.ORDER_STATUS_NOPAY.getCode());
        order.setVipId(vipPo.getId());
        order.setPayTime(null);
        order.setTotalPrice(totalPrice);
        orderDao.insert(order);

        //生成待支付记录
        PayLog payLog= new PayLog();
        payLog.setCreateTime(new Date());
        payLog.setPayType(1);
        payLog.setPayStatus(ServerEnum.ORDER_STATUS_NOPAY.getCode());
        payLog.setPayTime(null);
        payLog.setPayMoney(totalPrice);
        payLog.setOutTradeNo(outTradeNo);
        payLog.setVipId(vipPo.getId());
        payLog.setOrderId(orderId);
        payLog.setTransactionId(null);
        payLogDao.insert(payLog);
        redisTemplate.opsForHash().put("pay_order"+phone,outTradeNo,payLog);
        redisTemplate.delete("order_"+phone);
        Map<String,Object>map=new HashMap<>();
        map.put("count",count);
        map.put("subTotal",totalPrice);
        map.put("orderId",orderId);
        map.put("outTradeNo",outTradeNo);
        return ResponseServer.success(map);
    }
}
