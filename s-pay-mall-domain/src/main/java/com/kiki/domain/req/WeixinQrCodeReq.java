package com.kiki.domain.req;

import javax.annotation.Generated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


/** 
 * 微信二维码请求参数
 * @see <a href="https://developers.weixin.qq.com/doc/service/api/qrcode/qrcodes/api_createqrcode.html">微信二维码请求文档</a>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeixinQrCodeReq {
    private int expire_seconds;
    private String action_name;
    private ActionInfo action_info;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ActionInfo {
        private Scene scene;
        
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class Scene {
            int scene_id;
            String scene_str;
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum ActionName {
        QR_SCENE("QR_SCENE","临时整形"),
        QR_STR_SCENE("QR_STR_SCENE","临时字符串"),
        QR_LIMIT_SCENE("QR_LIMIT_SCENE","永久整形"),
        QR_LIMIT_STR_SCENE("QR_LIMIT_STR_SCENE","永久字符串");

        private String code;
        private String info;

    }

    
}
