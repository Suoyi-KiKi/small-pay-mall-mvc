
package com.kiki.domain.res;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class WeixinTemplateRes {
    String msgid;
    String errcode;
    String errmsg;
}