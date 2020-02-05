package com.geoxus.modules.banner.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.event.GXMediaLibraryEvent;
import com.geoxus.core.common.util.GXHttpContextUtils;
import com.geoxus.core.common.util.GXSyncEventBusCenterUtils;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.core.framework.service.GXCoreMediaLibraryService;
import com.geoxus.core.framework.service.GXCoreModelService;
import com.geoxus.modules.banner.constant.BannerConstants;
import com.geoxus.modules.banner.entity.BannerEntity;
import com.geoxus.modules.banner.mapper.BannerMapper;
import com.geoxus.modules.banner.service.BannerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, BannerEntity> implements BannerService {
    @Autowired
    private GXCoreModelService coreModelService;

    @Autowired
    private GXCoreMediaLibraryService coreMediaLibraryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long create(BannerEntity target, Dict param) {
        save(target);
        final List<?> mediaInfo = GXHttpContextUtils.getHttpParam("media_info", List.class);
        if (null != mediaInfo) {
            if (null != param) {
                param.set("media", mediaInfo);
            } else {
                param = Dict.create().set("media", mediaInfo);
            }
            param.set("model_id", target.getBannerId());
            final GXMediaLibraryEvent<BannerEntity> event = new GXMediaLibraryEvent<>(coreModelService.getModelTypeByModelId(target.getCoreModelId(), "Banner"), target, param);
            GXSyncEventBusCenterUtils.getInstance().post(event);
        }
        return target.getBannerId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long update(BannerEntity target, Dict param) {
        updateById(target);
        final List<?> mediaInfo = GXHttpContextUtils.getHttpParam("media_info", List.class);
        if (null != mediaInfo) {
            if (null != param) {
                param.set("media", mediaInfo);
            } else {
                param = Dict.create().set("media", mediaInfo);
            }
            param.set("model_id", target.getBannerId());
            final GXMediaLibraryEvent<BannerEntity> event = new GXMediaLibraryEvent<>(coreModelService.getModelTypeByModelId(target.getCoreModelId(), "Banner"), target, param);
            GXSyncEventBusCenterUtils.getInstance().post(event);
        }
        return target.getBannerId();
    }

    @Override
    public boolean delete(Dict param) {
        final List<Integer> list = Convert.convert(new TypeReference<List<Integer>>() {
        }, param.getObj(BannerConstants.PRIMARY_KEY));
        final ArrayList<BannerEntity> updateArrayList = new ArrayList<>();
        for (int id : list) {
            BannerEntity entity = getOne(new QueryWrapper<BannerEntity>().allEq(Dict.create().set(BannerConstants.PRIMARY_KEY, id).set("user_id", param.getInt("user_id"))));
            if (null == entity) {
                continue;
            }
            entity.setStatus(GXBusinessStatusCode.DELETED.getCode());
            updateArrayList.add(entity);
        }
        if (!updateArrayList.isEmpty()) {
            return updateBatchById(updateArrayList);
        }
        return false;
    }

    @Override
    public GXPagination listOrSearch(Dict param) {
        return generatePage(param);
    }

    @Override
    public Dict detail(Dict param) {
        return baseMapper.detail(param);
    }

    @Override
    public boolean show(Dict param) {
        return modifyStatus(Dict.create().set(BannerConstants.PRIMARY_KEY, param.getInt(BannerConstants.PRIMARY_KEY)), GXBusinessStatusCode.NORMAL.getCode());
    }

    @Override
    public boolean hidden(Dict param) {
        return modifyStatus(Dict.create().set(BannerConstants.PRIMARY_KEY, param.getInt(BannerConstants.PRIMARY_KEY)), GXBusinessStatusCode.FREEZE.getCode());
    }
}
