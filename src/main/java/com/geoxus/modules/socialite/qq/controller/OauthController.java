package com.geoxus.modules.socialite.qq.controller;

import com.qq.connect.QQConnectException;
import com.qq.connect.oauth.Oauth;
import com.geoxus.modules.socialite.qq.service.IQQService;
import com.geoxus.modules.socialite.qq.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * QQ服务controller
 */
@Controller
@RequestMapping("/oauth")
@Slf4j
public class OauthController {

    @Autowired
    private IQQService qqService;

    /**
     * 跳转QQ登陆页面
     *
     * @param request
     * @param response
     */
    @GetMapping("/qq/login")
    @ResponseBody
    public void qqLogin(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        try {
            String url = new Oauth().getAuthorizeURL(request);
            log.info(String.format("qq sendRedirect --> %s", url));
            response.sendRedirect(new Oauth().getAuthorizeURL(request));
        } catch (QQConnectException | IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 获取QQ账号信息, 最终返回的是狮吼平台的用户数据
     *
     * @param request  参数 code 和  state
     * @param response
     * @return
     */
    @GetMapping("/qq/userInfo")
    @ResponseBody
    public ResponseEntity<Result> getQQUserInfo(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(Result.build().content(qqService.getUserInfo(request)));
    }
}
