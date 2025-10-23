package com.kiki.service.impl;


import java.util.Date;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import com.kiki.common.constants.Constants;
import com.kiki.dao.IOrderDao;
import com.kiki.domain.po.PayOrder;
import com.kiki.domain.req.ShopCartReq;
import com.kiki.domain.res.PayOrderRes;
import com.kiki.domain.vo.ProductVO;
import com.kiki.service.IOrderService;
import com.kiki.service.rpc.ProductRPC;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements IOrderService{

    @Resource
    IOrderDao orderDao;

    @Resource
    ProductRPC productRPC;

    @Override
    public PayOrderRes createOrder(ShopCartReq shopCartReq) throws Exception {
        // 查询当前用户是否存在未支付订单或掉单订单
        PayOrder payOrderReq = new PayOrder();
        payOrderReq.setUserId(shopCartReq.getUserId());
        payOrderReq.setProductId(shopCartReq.getProductId());

        PayOrder unpaidOrder = orderDao.queryUnPayOrder(payOrderReq);

        //如果存在订单,就返回订单
        if(unpaidOrder != null && Constants.OrderStatusEnum.PAY_WAIT.getCode().equals(unpaidOrder.getStatus())){ 
            log.info("订单已经存在,支付单已经创建,状态未支付:userId:{} productId:{} orderId:{}",unpaidOrder.getUserId(),unpaidOrder.getProductId(),unpaidOrder.getOrderId());
            return PayOrderRes.builder()
                .orderId(unpaidOrder.getOrderId())
                .payUrl(unpaidOrder.getPayUrl())
                .build();
        }else if(unpaidOrder != null && Constants.OrderStatusEnum.CREATE.getCode().equals(unpaidOrder.getStatus())){
            log.info("订单已经存在,支付单未创建:userId:{} productId:{} orderId:{}",unpaidOrder.getUserId(),unpaidOrder.getProductId(),unpaidOrder.getOrderId());
            // todo 创建支付单


        }

        //如果不存在订单则创建订单
        ProductVO productVO = productRPC.queryProductByProductId(shopCartReq.getUserId());
        String orderId = RandomStringUtils.randomNumeric(16);

        orderDao.insert(PayOrder.builder()
                            .userId(shopCartReq.getUserId())
                            .productId(shopCartReq.getProductId())
                            .productName(productVO.getProductName())
                            .orderId(orderId)
                            .totalAmount(productVO.getPrice())
                            .orderTime(new Date())
                            .status(Constants.OrderStatusEnum.CREATE.getCode())
                            .build() 
                        );


        //创建支付单并返回



        return PayOrderRes.builder()
                        .orderId(orderId)
                        .orderStatusEnum(Constants.OrderStatusEnum.CREATE)
                        .userId(shopCartReq.getUserId())
                        .payUrl("todo")
                        .build();

    }
    
}
