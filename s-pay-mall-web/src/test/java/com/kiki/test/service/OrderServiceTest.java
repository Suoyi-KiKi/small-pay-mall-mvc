package com.kiki.test.service;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.kiki.domain.req.ShopCartReq;
import com.kiki.domain.res.PayOrderRes;
import com.kiki.service.IOrderService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class OrderServiceTest {

    @Resource
    IOrderService service;

    @Test
    public void test() throws Exception{
        ShopCartReq shopCartReq = new ShopCartReq();
        shopCartReq.setProductId("10001");
        shopCartReq.setUserId("kiki");

        PayOrderRes order = service.createOrder(shopCartReq);

        log.info(order.toString());
    }
    
}