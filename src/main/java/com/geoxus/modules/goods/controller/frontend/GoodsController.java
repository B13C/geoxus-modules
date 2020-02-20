package com.geoxus.modules.goods.controller.frontend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXRequestBodyToBeanAnnotation;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.service.GXApiIdempotentService;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.goods.entity.GoodsEntity;
import com.geoxus.modules.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController("frontendGoodsController")
@RequestMapping("/goods/frontend")
public class GoodsController implements GXController<GoodsEntity> {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GXApiIdempotentService apiIdempotentService;

    @PostMapping("/get-goods-list")
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        final GXPagination page = goodsService.listOrSearchPage(param);
        return GXResultUtils.ok().putData(page);
    }

    @PostMapping("/create")
    public GXResultUtils create(@Valid @GXRequestBodyToBeanAnnotation GoodsEntity goodsEntity) {
        return null;
    }

    @PostMapping("/update")
    public GXResultUtils update(@Valid @GXRequestBodyToBeanAnnotation GoodsEntity goodsEntity) {
        return null;
    }

    @Override
    public GXResultUtils detail(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(goodsService.detail(param));
    }

    @Override
    public GXResultUtils delete(@RequestBody Dict param) {
        return null;
    }
}
