package com.geoxus.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.core.common.constant.GXCommonConstants;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.core.framework.service.GXCoreModelService;
import com.geoxus.modules.system.constant.SCategoryConstants;
import com.geoxus.modules.system.entity.SCategoryEntity;
import com.geoxus.modules.system.mapper.SCategoryMapper;
import com.geoxus.modules.system.service.SCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SCategoryServiceImpl extends ServiceImpl<SCategoryMapper, SCategoryEntity> implements SCategoryService {
    @GXFieldCommentAnnotation(zh = "路径分隔符")
    private static final String PATH_SEPARATOR = "-";

    @Autowired
    private GXCoreModelService gxCoreModelService;

    @Override
    public long create(SCategoryEntity target, Dict param) {
        final String modelType = gxCoreModelService.getModelTypeByModelId(target.getCoreModelId(), "SCategoryModelType");
        target.setModelType(modelType);
        target.setPath(getParentPath(target.getParentId(), target.getCoreModelId()));
        save(target);
        return target.getCategoryId();
    }

    @Override
    public long update(SCategoryEntity target, Dict param) {
        final String modelType = gxCoreModelService.getModelTypeByModelId(target.getCoreModelId(), "SCategoryModelType");
        target.setModelType(modelType);
        updateById(target);
        return target.getCategoryId();
    }

    @Override
    public boolean delete(Dict param) {
        final List<Integer> ids = Convert.convert((Type) TypeUtil.getClass(param.getObj(SCategoryConstants.PRIMARY_KEY).getClass()), param.getObj(SCategoryConstants.PRIMARY_KEY));
        final ArrayList<SCategoryEntity> updateList = new ArrayList<>();
        for (int id : ids) {
            SCategoryEntity entity = getById(id);
            entity.setStatus(GXBusinessStatusCode.DELETED.getCode());
            updateList.add(entity);
        }
        if (!updateList.isEmpty()) {
            return updateBatchById(updateList);
        }
        return true;
    }

    @Override
    public GXPagination listOrSearchPage(Dict param) {
        return generatePage(param);
    }

    @Override
    public Dict detail(Dict param) {
        return baseMapper.detail(param);
    }

    /**
     * 获取树状结构
     *
     * @return List
     */
    public List<Dict> getTree(Dict param) {
        final Page<Dict> page = new Page<>(1, 10000);
        final List<Dict> list = baseMapper.listOrSearchPage(page, param);
        //把根分类区分出来
        List<Dict> rootList = list.stream().filter(root -> root.getInt("parent_id") == 0).collect(Collectors.toList());
        //把非根分类区分出来
        List<Dict> subList = list.stream().filter(sub -> sub.getInt("parent_id") != 0).collect(Collectors.toList());
        //递归构建结构化的分类信息
        rootList.forEach(root -> buildSubs(root, subList));
        return rootList;
    }

    @Override
    public boolean openStatus(Dict param) {
        final int id = param.getInt(SCategoryConstants.PRIMARY_KEY);
        final Dict condition = Dict.create().set(SCategoryConstants.PRIMARY_KEY, id);
        return modifyStatus(GXBusinessStatusCode.NORMAL.getCode(), condition, GXBaseBuilderConstants.NON_OPERATOR);
    }

    @Override
    public boolean closeStatus(Dict param) {
        final int id = param.getInt(SCategoryConstants.PRIMARY_KEY);
        final Dict condition = Dict.create().set(SCategoryConstants.PRIMARY_KEY, id);
        return modifyStatus(GXBusinessStatusCode.OFF_STATE.getCode(), condition, GXBaseBuilderConstants.NON_OPERATOR);
    }

    @Override
    public boolean freezeStatus(Dict param) {
        final int id = param.getInt(SCategoryConstants.PRIMARY_KEY);
        final Dict condition = Dict.create().set(SCategoryConstants.PRIMARY_KEY, id);
        return modifyStatus(GXBusinessStatusCode.FREEZE.getCode(), condition, GXBaseBuilderConstants.NON_OPERATOR);
    }

    /**
     * 递归构建
     *
     * @param parent 父级ID
     * @param subs   子集数据
     */
    private void buildSubs(Dict parent, List<Dict> subs) {
        List<Dict> children = subs.stream().filter(sub -> sub.getInt("parent_id") == (int) parent.getInt(SCategoryConstants.PRIMARY_KEY)).collect(Collectors.toList());
        parent.set("children", children);
        if (!CollectionUtils.isEmpty(children)) {//有子分类的情况
            children.forEach(child -> buildSubs(child, subs));//再次递归构建
        }
    }

    @Override
    @Cacheable(value = "__DEFAULT__", key = "targetClass + methodName + #value + #field")
    public boolean validateExists(Object value, String field, ConstraintValidatorContext constraintValidatorContext, Dict param) throws UnsupportedOperationException {
        log.info("validateExists : {} , field : {}", value, field);
        final int categoryId = Convert.toInt(value, 0);
        return 1 == checkRecordIsExists(SCategoryEntity.class, Dict.create().set(SCategoryConstants.PRIMARY_KEY, categoryId));
    }

    /**
     * 获取路径
     *
     * @param parentId    父级ID
     * @param coreModelId 　核心模型ID
     * @return String
     */
    private String getParentPath(int parentId, int coreModelId) {
        if (parentId == 0) {
            return "0";
        }
        final Dict dictData = getFieldBySQL(SCategoryEntity.class, CollUtil.newHashSet("path"), Dict.create().set(SCategoryConstants.PRIMARY_KEY, parentId).set(GXCommonConstants.CORE_MODEL_PRIMARY_NAME, coreModelId));
        if (null == dictData || null == dictData.getStr("path")) {
            return "0";
        }
        return StrUtil.concat(true, dictData.getStr("path"), PATH_SEPARATOR, String.valueOf(parentId));
    }
}
