package com.kiki.service.rpc;

import org.springframework.stereotype.Service;

import com.kiki.domain.vo.ProductVO;

import java.math.BigDecimal;

@Service
public class ProductRPC {

    public ProductVO queryProductByProductId(String productId){
        ProductVO productVO = new ProductVO();
        productVO.setProductId(productId);
        productVO.setProductName("测试商品");
        productVO.setProductDesc("这是一个测试商品");
        productVO.setPrice(new BigDecimal("1.68"));
        return productVO;
    }

}
