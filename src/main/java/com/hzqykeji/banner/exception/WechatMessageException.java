package com.hzqykeji.banner.exception;

import lombok.Getter;
import lombok.Setter;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;

@Getter
@Setter
public class WechatMessageException extends RuntimeException {

    private WxMpTemplateMessage templateMessage;

    public WechatMessageException(String message, WxMpTemplateMessage templateMessage) {
        super(message);
        this.templateMessage = templateMessage;
    }
    public WechatMessageException() {
    }
}
