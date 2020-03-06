package com.geoxus.modules.goods.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.core.framework.service.GXCoreModelService;
import com.geoxus.modules.goods.constant.GoodsConstants;
import com.geoxus.modules.goods.entity.GoodsEntity;
import com.geoxus.modules.goods.mapper.GoodsMapper;
import com.geoxus.modules.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, GoodsEntity> implements GoodsService {
    @Autowired
    private GXCoreModelService coreModelService;

    @Override
    @Transactional
    public long create(GoodsEntity target, Dict param) {
        final boolean save = save(target);
        handleMedia(target, target.getGoodsId(), Dict.create());
        return target.getGoodsId();
    }

    @Override
    @Transactional
    public long update(GoodsEntity target, Dict param) {
        final boolean save = updateById(target);
        handleMedia(target, target.getGoodsId(), Dict.create());
        return target.getGoodsId();
    }

    @Override
    public boolean delete(Dict param) {
        final int goodsId = param.getInt(GoodsConstants.PRIMARY_KEY);
        final GoodsEntity entity = getById(goodsId);
        return updateById(entity);
    }

    @Override
    public GXPagination listOrSearchPage(Dict param) {
        return generatePage(param, Dict.create());
    }

    @Override
    public Dict detail(Dict param) {
        final int goodsId = param.getInt(GoodsConstants.PRIMARY_KEY);
        return Convert.convert(Dict.class, getById(goodsId));
    }
}
