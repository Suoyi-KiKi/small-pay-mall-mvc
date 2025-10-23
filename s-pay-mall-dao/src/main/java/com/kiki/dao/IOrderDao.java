package com.kiki.dao;

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
}
