package com.kiki.service.impl;

import java.util.HashMap;

import javax.annotation.Resource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.kiki.domain.req.WeixinQrCodeReq;
import com.kiki.domain.res.WeixinQrCodeRes;
import com.kiki.domain.res.WeixinTemplateRes;
import com.kiki.domain.res.WeixinTokenRes;
import com.kiki.domain.vo.WeixinTemplateMessageVO;
import com.kiki.service.ILoginService;
import com.kiki.service.weixin.IweixinApiService;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;

@Service
public class LoginServiceImpl implements ILoginService {
    @Autowired
    private IweixinApiService iweixinApiService;

    @Value("${weixin.config.app-id}")
    private String appid;

    @Value("${weixin.config.app-secret}")
    private String appsecret;

    @Value("${weixin.config.template_id}")
    private String template_id;

    
    @Resource
    RedisTemplate<String,String> redisTemplate;
    


    @Override
    public String getWeixinQrCodeTicket() throws Exception {

        String token = redisTemplate.opsForValue().get(appid);
        
        //1. 获取 accesToken
        if(null == token){
            System.out.println(appid);
            System.out.println(appsecret);

            Call<WeixinTokenRes> call = iweixinApiService.getAccessToken("client_credential", appid, appsecret);
            WeixinTokenRes weixinTokenRes = call.execute().body();
            
            if (weixinTokenRes == null) {
                throw new Exception("获取微信AccessToken失败：响应为空");
            }
            token = weixinTokenRes.getAccess_token();

            redisTemplate.opsForValue().set(appid, token,2,TimeUnit.HOURS);
        }

        //2 获取ticket
        WeixinQrCodeReq weixinQrCodeReq = WeixinQrCodeReq.builder()
                                                            .expire_seconds(2592000)
                                                            .action_name(WeixinQrCodeReq.ActionName.QR_SCENE.getCode())
                                                            .action_info(WeixinQrCodeReq.ActionInfo.builder()
                                                                                .scene(WeixinQrCodeReq.ActionInfo.Scene.builder()
                                                                                            .scene_id(100601).build())
                                                                                .build())
                                                            .build();   
                                                
        Call<WeixinQrCodeRes> weixinQrCodeCall = iweixinApiService.creatQrCode(token, weixinQrCodeReq);


        WeixinQrCodeRes weixinQrCodeRes = weixinQrCodeCall.execute().body();
        
        if (weixinQrCodeRes == null) {
            throw new Exception("获取微信二维码失败：响应为空");
        }
        
        // 检查微信API是否返回错误
        if (weixinQrCodeRes.getErrcode() != null && weixinQrCodeRes.getErrcode() != 0) {
            throw new Exception("微信API错误: " + weixinQrCodeRes.getErrmsg() + " (错误码: " + weixinQrCodeRes.getErrcode() + ")");
        }

        return weixinQrCodeRes.getTicket();
    }

    @Override
    public String checkLogin(String ticket) {
        return redisTemplate.opsForValue().get(ticket);
    }

    @Override
    public void savaLoginStatus(String ticket, String openid) throws Exception {
        redisTemplate.opsForValue().set(ticket, openid,2,TimeUnit.HOURS);


        String token = redisTemplate.opsForValue().get(appid);
        
        //1. 获取 accesToken
        if(null == token){
            System.out.println(appid);
            System.out.println(appsecret);

            Call<WeixinTokenRes> call = iweixinApiService.getAccessToken("client_credential", appid, appsecret);
            WeixinTokenRes weixinTokenRes = call.execute().body();
            
            if (weixinTokenRes == null) {
                throw new Exception("获取微信AccessToken失败：响应为空");
            }
            token = weixinTokenRes.getAccess_token();

            redisTemplate.opsForValue().set(appid, token,2,TimeUnit.HOURS);
        }


        Map<String, Map<String, String>> data = new HashMap<>();
        WeixinTemplateMessageVO.put(data, WeixinTemplateMessageVO.TemplateKey.USER, openid);

        WeixinTemplateMessageVO weixinTemplateMessageDTO = new WeixinTemplateMessageVO(openid,template_id);

        weixinTemplateMessageDTO.setUrl("https://gaga.plus");
        weixinTemplateMessageDTO.setData(data);


        Call<WeixinTemplateRes> call  = iweixinApiService.sendTemplateMessage(token, weixinTemplateMessageDTO);
        WeixinTemplateRes res = call.execute().body();
        System.out.println(res.toString());

    }
}
