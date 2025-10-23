package com.kiki.service;

import java.util.List;

import com.kiki.domain.req.ShopCartReq;
import com.kiki.domain.res.PayOrderRes;

public interface IOrderService {
    PayOrderRes createOrder(ShopCartReq shopCartReq) throws Exception;

    void changeOrderPaySuccess(String orderId);

    List<String> queryNoPayNotifyOrder();

    List<String> queryTimeoutCloseOrderList();

    boolean changeOrderClose(String orderId);

}
