package com.kiki.service;

import com.kiki.domain.req.ShopCartReq;
import com.kiki.domain.res.PayOrderRes;

public interface IOrderService {
    PayOrderRes createOrder(ShopCartReq shopCartReq) throws Exception;
}
