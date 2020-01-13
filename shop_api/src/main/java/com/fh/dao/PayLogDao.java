package com.fh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.model.PayLog;
import com.fh.model.po.Order;
import com.fh.model.po.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PayLogDao extends BaseMapper<PayLog> {
    PayLog selectListByVipId(String id);

   List<OrderInfo> queryOrderInfo(String orderId);

    List<Order> selectOrderByVipId(Integer id);
}
