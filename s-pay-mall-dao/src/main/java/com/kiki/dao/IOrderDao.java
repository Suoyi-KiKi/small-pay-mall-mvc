package com.kiki.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kiki.domain.po.PayOrder;

@Mapper
public interface IOrderDao {

    /*
     * 插入订单
     */
    void insert(PayOrder payOrder);

    /*
     * 查询订单
     */
    PayOrder queryUnPayOrder(PayOrder payOrder);

    /*
     * 将alipay返回的url插入到数据库中
     */
    void updateOrderPayInfo(PayOrder payOrder);

    void changeOrderPaySuccess(PayOrder order);

    List<String> queryNoPayNotifyOrder();

    List<String> queryTimeoutCloseOrderList();

    boolean changeOrderClose(String orderId);


}
