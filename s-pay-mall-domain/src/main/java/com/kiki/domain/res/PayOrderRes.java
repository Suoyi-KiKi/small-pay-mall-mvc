package com.kiki.domain.res;

import com.kiki.common.constants.Constants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PayOrderRes {

    private String userId;
    private String orderId;
    private String payUrl;
    private Constants.OrderStatusEnum orderStatusEnum;

}
