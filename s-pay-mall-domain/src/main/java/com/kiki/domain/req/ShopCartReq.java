package com.kiki.domain.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/*
 * 购物车
 */


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopCartReq {

    private String userId;

    private String productId;

}
