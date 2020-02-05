package com.geoxus.modules.goods.controller.backend;

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

@RestController("backendGoodsController")
@RequestMapping("/goods/backend")
public class GoodsController implements GXController<GoodsEntity> {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GXApiIdempotentService apiIdempotentService;

    @PostMapping("/get-goods-list")
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        final GXPagination page = goodsService.listOrSearch(param);
        return GXResultUtils.ok().putData(page);
    }

    @PostMapping("/create")
    public GXResultUtils create(@Valid @GXRequestBodyToBeanAnnotation GoodsEntity goodsEntity) {
        final long i = goodsService.create(goodsEntity, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set("id", i));
    }

    @PostMapping("/update")
    public GXResultUtils update(@Valid @GXRequestBodyToBeanAnnotation GoodsEntity goodsEntity) {
        final long i = goodsService.update(goodsEntity, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set("id", i));
    }

    @Override
    public GXResultUtils detail(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(goodsService.detail(param));
    }

    @Override
    public GXResultUtils delete(@RequestBody Dict param) {
        final boolean b = goodsService.delete(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @PostMapping("/modify-goods")
    public GXResultUtils modifyGoods(@GXRequestBodyToBeanAnnotation GoodsEntity goodsEntity, @RequestBody Dict param) {
        final GoodsEntity entity = goodsService.modifyEntityJSONFieldMultiValue(goodsEntity, param);
        final boolean b = goodsService.updateById(entity);
        return GXResultUtils.ok().putData(entity);
    }
}
