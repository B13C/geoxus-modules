package com.geoxus.modules.banner.controller.backend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXRequestBodyToBeanAnnotation;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.modules.banner.constant.BannerConstants;
import com.geoxus.modules.banner.entity.BannerEntity;
import com.geoxus.modules.banner.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController("backendBannerController")
@RequestMapping("/banner/backend")
public class BannerController implements GXController<BannerEntity> {
    @Autowired
    private BannerService bannerService;

    @Override
    @PostMapping("/create")
    public GXResultUtils create(@Valid @GXRequestBodyToBeanAnnotation BannerEntity bannerEntity) {
        long i = bannerService.create(bannerEntity, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set(BannerConstants.PRIMARY_KEY, i));
    }

    @Override
    @PostMapping("/update")
    public GXResultUtils update(@Valid @GXRequestBodyToBeanAnnotation BannerEntity bannerEntity) {
        long i = bannerService.update(bannerEntity, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set(BannerConstants.PRIMARY_KEY, i));
    }

    @Override
    @PostMapping("/delete")
    public GXResultUtils delete(@RequestBody Dict param) {
        final boolean b = bannerService.delete(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @Override
    @PostMapping("/list-or-search")
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        param.set("status", GXBusinessStatusCode.NORMAL.getCode());
        return GXResultUtils.ok().putData(bannerService.listOrSearchPage(param));
    }

    @Override
    @PostMapping("/detail")
    public GXResultUtils detail(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(bannerService.detail(param));
    }

    @PostMapping("/show")
    public GXResultUtils show(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(Dict.create().set("status", bannerService.show(param)));
    }

    @PostMapping("/hidden")
    public GXResultUtils hidden(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(Dict.create().set("status", bannerService.hidden(param)));
    }
}
