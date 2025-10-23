package com.kiki.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kiki.common.constants.Constants;
import com.kiki.common.response.Response;
import com.kiki.service.ILoginService;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RestController
@CrossOrigin("*") //用于开启 CORS 跨域支持，并允许来自“任意域名”的浏览器请求访问被标注的接口（
@RequestMapping("api/v1/login/")
public class LoginController {

    @Resource
    private ILoginService loginService;

    @GetMapping("weixin_qrcode_ticket")
    public Response<String> getWeixinQrCodeTicket( ) {
        try{
            String qrCodeTicTicket = loginService.getWeixinQrCodeTicket();
            log.info("得到ticket:{}" ,qrCodeTicTicket);

            return Response.<String>builder()
            .code(Constants.ResposeCode.SUCCESS.getCode())
            .info(Constants.ResposeCode.SUCCESS.getInfo())
            .data(qrCodeTicTicket)
            .build();
        }catch(Exception e){
            log.error("得到ticket失败" ,e);
            return Response.<String>builder()
            .code(Constants.ResposeCode.UN_ERROR.getCode())
            .info(Constants.ResposeCode.UN_ERROR.getInfo())
            .build();
        }
        
    }



    @GetMapping("check_login")
    public Response<String> getMethodName(@RequestParam String ticket) {
        try{
            String openId = loginService.checkLogin(ticket);
            log.info("登录结果 ticket:{} openid:{}",ticket,openId);
            if(StringUtils.isNotBlank(openId)){
                return Response.<String>builder()
                .code(Constants.ResposeCode.SUCCESS.getCode())
                .info(Constants.ResposeCode.SUCCESS.getInfo())
                .data(openId)
                .build();
            }else{
                return Response.<String>builder()
                .code(Constants.ResposeCode.NO_LOGIN.getCode())
                .info(Constants.ResposeCode.NO_LOGIN.getInfo())
                .build();
            }
        }catch(Exception e){
            log.error("扫码登录失败" ,e);
            return Response.<String>builder()
            .code(Constants.ResposeCode.UN_ERROR.getCode())
            .info(Constants.ResposeCode.UN_ERROR.getInfo())
            .build();

        }
    }
    
    
    
    
}
