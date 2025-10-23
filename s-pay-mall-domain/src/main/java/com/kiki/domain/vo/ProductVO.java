package com.kiki.domain.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductVO {
        /** 商品ID */
        private String productId;
        /** 商品名称 */
        private String productName;
        /** 商品描述 */
        private String productDesc;
        /** 商品价格 */
        private BigDecimal price;
    
}
