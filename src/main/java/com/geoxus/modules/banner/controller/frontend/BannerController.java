package com.geoxus.modules.banner.controller.frontend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.modules.banner.entity.BannerEntity;
import com.geoxus.modules.banner.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("frontendBannerController")
@RequestMapping("/banner/frontend")
public class BannerController implements GXController<BannerEntity> {
    @Autowired
    private BannerService bannerService;

    @Override
    @PostMapping("/list-or-search")
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        param.set("status", GXBusinessStatusCode.NORMAL.getCode());
        return GXResultUtils.ok().putData(bannerService.listOrSearch(param));
    }

    @Override
    @PostMapping("/detail")
    public GXResultUtils detail(@RequestBody Dict dict) {
        return GXResultUtils.ok().putData(bannerService.detail(dict));
    }
}
