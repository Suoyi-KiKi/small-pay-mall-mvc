package com.kiki.domain.res;

import lombok.Data;

@Data
public class WeixinQrCodeRes {
    private String ticket;
    private Long expire_seconds;
    private String url;
    private Integer errcode;  // 错误码，0表示成功
    private String errmsg;    // 错误信息
}
