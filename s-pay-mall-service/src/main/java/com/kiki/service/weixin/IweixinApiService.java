package com.kiki.service.weixin;

import com.kiki.domain.res.WeixinTokenRes;
import com.kiki.domain.vo.WeixinTemplateMessageVO;
import com.kiki.domain.req.WeixinQrCodeReq;
import com.kiki.domain.res.WeixinQrCodeRes;
import com.kiki.domain.res.WeixinTemplateRes;

import retrofit2.Call;
import retrofit2.http.Query;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface IweixinApiService {


    /*
     * 获取微信AccessToken
     * @param grantType 授权类型，固定值为client_credential
     * @param appid  公众号的appid
     * @param secret 公众号的secret
     * @return 微信AccessToken
     * @see <a href="https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-access-token/getAccessToken.html">微信AccessToken文档</a>
     */
    @GET("cgi-bin/token")
    Call<WeixinTokenRes> getAccessToken(@Query("grant_type") String grantType, @Query("appid") String appid, @Query("secret") String secret);

    /*
     * 获取微信二维码
     * @param access_token 微信AccessToken
     * @param weixinQrCodeReq 微信二维码请求参数
     * @return 微信二维码
     * @see <a href="https://developers.weixin.qq.com/doc/service/api/qrcode/qrcodes/api_createqrcode.html">微信二维码请求文档</a>
     */
    @POST("cgi-bin/qrcode/create")
    Call<WeixinQrCodeRes> creatQrCode(@Query("access_token") String accessToken,@Body WeixinQrCodeReq weixinQrCodeReq);



    /*
    * 发送模板消息
    * @param access_token 微信AccessToken
    * @param weixinTemplateMessageVO 微信模板消息
    * @return 
    * @see <a href="https://mp.weixin.qq.com/debug/cgi-bin/readtmpl?t=tmplmsg/faq_tmpl">微信模板消息文档</a>
    */
    @POST("cgi-bin/message/template/send")
    Call<WeixinTemplateRes> sendTemplateMessage(@Query("access_token") String access_token, @Body WeixinTemplateMessageVO weixinTemplateMessageVO);


}