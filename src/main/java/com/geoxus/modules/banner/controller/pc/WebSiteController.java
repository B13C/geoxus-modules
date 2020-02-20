package com.geoxus.modules.banner.controller.pc;

import cn.hutool.core.lang.Dict;
import com.geoxus.modules.banner.constant.BannerConstants;
import com.geoxus.modules.banner.service.BannerService;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.core.common.util.GXResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/banner/pc")
public class WebSiteController {
    @Autowired
    private BannerService bannerService;

    @PostMapping("/list-or-search")
    public GXResultUtils banner(@RequestBody Dict param) {
        final Dict dict = Dict.create().set("type", BannerConstants.WEBSITE_TYPE)
                .set("status", GXBusinessStatusCode.NORMAL.getCode())
                .set("core_model_id", param.getInt("core_model_id"));
        return GXResultUtils.ok().putData(bannerService.listOrSearchPage(dict));
    }

    @PostMapping("/detail")
    public GXResultUtils detail(@RequestBody Dict dict) {
        return GXResultUtils.ok().putData(bannerService.detail(dict));
    }
}
