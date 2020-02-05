package com.geoxus.modules.contents.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.core.framework.service.GXCoreModelService;
import com.geoxus.modules.contents.constant.ContentConstants;
import com.geoxus.modules.contents.entity.ContentEntity;
import com.geoxus.modules.contents.mapper.ContentMapper;
import com.geoxus.modules.contents.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ContentServiceImpl extends ServiceImpl<ContentMapper, ContentEntity> implements ContentService {
    @Autowired
    private GXCoreModelService coreModelService;

    @Override
    public long create(ContentEntity target, Dict param) {
        target.setCoreModelId(coreModelService.getModelIdByModelIdentification(ContentConstants.MODEL_IDENTIFICATION));
        save(target);
        return target.getContentId();
    }

    @Override
    public long update(ContentEntity target, Dict param) {
        target.setCoreModelId(coreModelService.getModelIdByModelIdentification(ContentConstants.MODEL_IDENTIFICATION));
        updateById(target);
        return target.getContentId();
    }

    @Override
    public boolean delete(Dict param) {
        final List<Integer> list = Convert.convert(new TypeReference<List<Integer>>() {
        }, param.getObj(ContentConstants.PRIMARY_KEY));
        final ArrayList<ContentEntity> updateEntities = new ArrayList<>();
        for (int id : list) {
            ContentEntity entity = getById(id);
            if (null == entity) {
                continue;
            }
            entity.setStatus(GXBusinessStatusCode.DELETED.getCode());
            updateEntities.add(entity);
        }
        if (!updateEntities.isEmpty()) {
            return updateBatchById(updateEntities);
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
        return modifyStatus(Dict.create().set(ContentConstants.PRIMARY_KEY, param.getInt(ContentConstants.PRIMARY_KEY)), GXBusinessStatusCode.NORMAL.getCode());
    }

    @Override
    public boolean hidden(Dict param) {
        return modifyStatus(Dict.create().set(ContentConstants.PRIMARY_KEY, param.getInt(ContentConstants.PRIMARY_KEY)), GXBusinessStatusCode.FREEZE.getCode());
    }

    @Override
    public Dict testRPC(Dict param) {
        Objects.requireNonNull(param.getInt("model_id"));
        return Dict.create().set("cccc", "aaaa");
    }
}
