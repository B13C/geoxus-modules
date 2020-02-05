package com.geoxus.modules.system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.TypeUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.system.constant.CategoryConstants;
import com.geoxus.modules.system.entity.CategoryEntity;
import com.geoxus.modules.system.mapper.CategoryMapper;
import com.geoxus.modules.system.service.CategoryService;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryEntity> implements CategoryService {
    @Override
    public long create(CategoryEntity target, Dict param) {
        save(target);
        return target.getCategoryId();
    }

    @Override
    public long update(CategoryEntity target, Dict param) {
        updateById(target);
        return target.getCategoryId();
    }

    @Override
    public boolean delete(Dict param) {
        final List<Integer> ids = Convert.convert((Type) TypeUtil.getClass(param.getObj(CategoryConstants.PRIMARY_KEY).getClass()), param.getObj(CategoryConstants.PRIMARY_KEY));
        final ArrayList<CategoryEntity> updateList = new ArrayList<>();
        for (int id : ids) {
            CategoryEntity entity = getById(id);
            entity.setStatus(GXBusinessStatusCode.DELETED.getCode());
            updateList.add(entity);
        }
        if (!updateList.isEmpty()) {
            return updateBatchById(updateList);
        }
        return true;
    }

    @Override
    public GXPagination listOrSearch(Dict param) {
        return generatePage(param);
    }

    @Override
    public Dict detail(Dict param) {
        return baseMapper.detail(param);
    }

    /**
     * 获取树状结构
     *
     * @return
     */
    public List<Dict> getTree(Dict param) {
        final Page<Dict> page = new Page<>(1, 10000);
        final List<Dict> list = baseMapper.listOrSearch(page, param);
        //把根分类区分出来
        List<Dict> rootList = list.stream().filter(root -> root.getInt("parentId") == 0).collect(Collectors.toList());
        //把非根分类区分出来
        List<Dict> subList = list.stream().filter(sub -> sub.getInt("parentId") != 0).collect(Collectors.toList());
        //递归构建结构化的分类信息
        rootList.forEach(root -> buildSubs(root, subList));
        return rootList;
    }

    @Override
    public boolean openStatus(Dict param) {
        final int id = param.getInt(CategoryConstants.PRIMARY_KEY);
        return modifyStatus(Dict.create().set(CategoryConstants.PRIMARY_KEY, id), GXBusinessStatusCode.NORMAL.getCode());
    }

    @Override
    public boolean closeStatus(Dict param) {
        final int id = param.getInt(CategoryConstants.PRIMARY_KEY);
        return modifyStatus(Dict.create().set(CategoryConstants.PRIMARY_KEY, id), GXBusinessStatusCode.OFF_STATE.getCode());
    }

    @Override
    public boolean freezeStatus(Dict param) {
        final int id = param.getInt(CategoryConstants.PRIMARY_KEY);
        return modifyStatus(Dict.create().set(CategoryConstants.PRIMARY_KEY, id), GXBusinessStatusCode.FREEZE.getCode());
    }

    @Override
    public boolean updateFieldByCondition(String tableName, Dict data, Dict condition) {
        return baseMapper.updateFieldByCondition(tableName, data, condition);
    }

    /**
     * 递归构建
     *
     * @param parent 父级ID
     * @param subs   子集数据
     */
    private void buildSubs(Dict parent, List<Dict> subs) {
        List<Dict> children = subs.stream().filter(sub -> sub.getInt("parentId") == (int) parent.getInt(CategoryConstants.PRIMARY_KEY)).collect(Collectors.toList());
        parent.set("children", children);
        if (!CollectionUtils.isEmpty(children)) {//有子分类的情况
            children.forEach(child -> buildSubs(child, subs));//再次递归构建
        }
    }

    @Override
    public boolean validateExists(Object value, String field, ConstraintValidatorContext constraintValidatorContext, Dict param) throws UnsupportedOperationException {
        System.out.println("validateExists : " + value + "field : " + field);
        final int parentId = Convert.toInt(value);
        return parentId == 0 || null != getById(parentId);
    }
}
