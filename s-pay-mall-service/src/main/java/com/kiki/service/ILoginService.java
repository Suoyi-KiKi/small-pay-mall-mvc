package com.kiki.service;


public interface ILoginService {
    String getWeixinQrCodeTicket() throws Exception;
    String checkLogin(String ticket);
    void savaLoginStatus(String ticket,String openid) throws Exception;
}
