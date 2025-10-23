package com.kiki.service.impl;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.google.common.eventbus.EventBus;
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

    @Value("${alipay.notify_url}")
    private String notifyUrl;
    @Value("${alipay.return_url}")
    private String returnUrl;

    @Resource
    EventBus eventBus;


    @Resource
    IOrderDao orderDao;

    @Resource
    ProductRPC productRPC;

    @Resource
    private AlipayClient alipayClient;

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

            PayOrder toPayOrder = doPrepayOrder(shopCartReq.getProductId(),unpaidOrder.getProductName(),unpaidOrder.getOrderId(),unpaidOrder.getTotalAmount());

            return PayOrderRes.builder()
                        .orderId(unpaidOrder.getOrderId())
                        .payUrl(toPayOrder.getPayUrl())
                        .build();

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
        PayOrder payOrder = doPrepayOrder(shopCartReq.getProductId(),productVO.getProductName(),orderId,productVO.getPrice());



        return PayOrderRes.builder()
                        .orderId(orderId)
                        .orderStatusEnum(Constants.OrderStatusEnum.CREATE)
                        .userId(shopCartReq.getUserId())
                        .payUrl(payOrder.getPayUrl())
                        .build();

    }



    private PayOrder doPrepayOrder(String productId, String productName, String orderId, BigDecimal totalAmount) throws AlipayApiException {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(notifyUrl);
        request.setReturnUrl(returnUrl);

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderId);
        bizContent.put("total_amount", totalAmount.toString());
        bizContent.put("subject", productName);
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());

        String form = alipayClient.pageExecute(request).getBody();


        PayOrder payOrder = new PayOrder();
        payOrder.setOrderId(orderId);
        payOrder.setPayUrl(form);
        payOrder.setStatus(Constants.OrderStatusEnum.PAY_WAIT.getCode());

        // todo 根据OrderId 更新订单
        orderDao.updateOrderPayInfo(payOrder);

        return payOrder;


    }


/*
 * 将支付成功的订单 状态设置为  success
 */
    @Override
    public void changeOrderPaySuccess(String orderId) {
        PayOrder payOrderReq = new PayOrder();
        payOrderReq.setOrderId(orderId);
        payOrderReq.setStatus(Constants.OrderStatusEnum.PAY_SUCCESS.getCode());

        orderDao.changeOrderPaySuccess(payOrderReq);

        eventBus.post(JSON.toJSONString(payOrderReq));

    }


    /*
     * 查询已经产生URL但是一分钟未支付的订单
     */

    @Override
    public List<String> queryNoPayNotifyOrder() {
        return orderDao.queryNoPayNotifyOrder();
    }


/*
 * 查询超时（>30min）的订单
 */
    @Override
    public List<String> queryTimeoutCloseOrderList() {
        return orderDao.queryTimeoutCloseOrderList();
    }


/*
 * 将订单状态设置为超时关闭
 */
    @Override
    public boolean changeOrderClose(String orderId) {
        return orderDao.changeOrderClose(orderId);
    }

    
}
