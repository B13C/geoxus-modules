package com.geoxus.modules.wechat.miniapp.controller;

import cn.binarywang.wx.miniapp.api.WxMaMsgService;
import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaKefuMessage;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController("miniIndexController")
@RequestMapping("/mini")
public class IndexController {
    @Autowired
    private WxMaQrcodeService wxMaQrcodeService;

    @Autowired
    private WxMaMsgService wxMaMsgService;

    @Autowired
    private WxMaUserService wxMaUserService;

    @GetMapping("/getMsg")
    public String getMsg(@RequestParam("name") String name) {
        return "Java Message : " + name;
    }

    @GetMapping("/qr")
    public String testQR() throws WxErrorException {
        File file = wxMaQrcodeService.createQrcode("abc");
        System.out.println(file.getName());
        return file.getPath() + File.separator + file.getName();
    }

    @GetMapping("/kfMsg")
    public String testMsg() throws WxErrorException {
        WxMaKefuMessage wxMaKefuMessage = new WxMaKefuMessage();
        wxMaKefuMessage.setText(new WxMaKefuMessage.KfText("Hello World"));
        wxMaKefuMessage.setToUser("oSyXV05v92yFIQ1NX1jcdoZtgjL8");
        Boolean bool = wxMaMsgService.sendKefuMsg(wxMaKefuMessage);
        return "发送客户消息";
    }

    //@GetMapping("/users")
    /*public String users(){
        wxMaUserService.getUserInfo()
    }*/
}
