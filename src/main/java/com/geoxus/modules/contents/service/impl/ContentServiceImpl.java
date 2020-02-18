package com.geoxus.modules.contents.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.core.framework.service.GXCoreModelService;
import com.geoxus.modules.contents.constant.ContentConstant;
import com.geoxus.modules.contents.entity.ContentEntity;
import com.geoxus.modules.contents.mapper.ContentMapper;
import com.geoxus.modules.contents.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContentServiceImpl extends ServiceImpl<ContentMapper, ContentEntity> implements ContentService {
    @Autowired
    private GXCoreModelService coreModelService;

    @Override
    public long create(ContentEntity target, Dict param) {
        target.setCoreModelId(coreModelService.getModelIdByModelIdentification(ContentConstant.MODEL_IDENTIFICATION));
        save(target);
        return target.getContentId();
    }

    @Override
    public long update(ContentEntity target, Dict param) {
        final int coreModelId = coreModelService.getModelIdByModelIdentification(ContentConstant.MODEL_IDENTIFICATION);
        target.setCoreModelId(coreModelId);
        updateById(target);
        return target.getContentId();
    }

    @Override
    public boolean delete(Dict param) {
        final List<Integer> list = Convert.convert(new TypeReference<List<Integer>>() {
        }, param.getObj(getPrimaryKey()));
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
        final Dict condition = Dict.create().set(ContentConstant.PRIMARY_KEY, param.getInt(ContentConstant.PRIMARY_KEY));
        return modifyStatus(GXBusinessStatusCode.NORMAL.getCode(), condition, GXBaseBuilderConstants.NON_OPERATOR);
    }

    @Override
    public boolean hidden(Dict param) {
        final Dict condition = Dict.create().set(ContentConstant.PRIMARY_KEY, param.getInt(ContentConstant.PRIMARY_KEY));
        return modifyStatus(GXBusinessStatusCode.FREEZE.getCode(), condition, GXBaseBuilderConstants.NON_OPERATOR);
    }

    @Override
    public boolean validateExists(Object value, String field, ConstraintValidatorContext constraintValidatorContext, Dict param) throws UnsupportedOperationException {
        final int contentId = Convert.toInt(value);
        return null != getById(contentId);
    }

    @Override
    public String getPrimaryKey() {
        return ContentConstant.PRIMARY_KEY;
    }
}
