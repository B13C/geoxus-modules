package com.geoxus.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.TypeUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.system.constant.SMenuConstants;
import com.geoxus.modules.system.entity.SMenuEntity;
import com.geoxus.modules.system.mapper.SMenuMapper;
import com.geoxus.modules.system.service.SMenuService;
import com.geoxus.modules.system.service.SPermissionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SMenuServiceImpl extends ServiceImpl<SMenuMapper, SMenuEntity> implements SMenuService {
    @Autowired
    private SPermissionsService sPermissionsService;

    @Override
    public long create(SMenuEntity target, Dict param) {
        final List<Integer> permissionsIds = Convert.convert(new TypeReference<List<Integer>>() {
        }, target.getPerms());
        final Set<String> permissionsCode = sPermissionsService.getPermissionsCode(permissionsIds);
        target.setPerms(String.join(",", permissionsCode));
        save(target);
        return target.getMenuId();
    }

    @Override
    public long update(SMenuEntity target, Dict param) {
        final List<Integer> permissionsIds = Convert.convert(new TypeReference<List<Integer>>() {
        }, target.getPerms());
        final Set<String> permissionsCode = sPermissionsService.getPermissionsCode(permissionsIds);
        target.setPerms(String.join(",", permissionsCode));
        updateById(target);
        return target.getMenuId();
    }

    @Override
    public boolean delete(Dict param) {
        final List<Integer> ids = Convert.convert((Type) TypeUtil.getClass(param.getObj(SMenuConstants.PRIMARY_KEY).getClass()), param.getObj(SMenuConstants.PRIMARY_KEY));
        final ArrayList<SMenuEntity> updateList = new ArrayList<>();
        for (int id : ids) {
            SMenuEntity entity = getById(id);
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
        return generatePage(param, Dict.create());
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
    @Cacheable(value = "__DEFAULT__", key = "targetClass + methodName")
    public List<Dict> getTree() {
        final List<Dict> list = baseMapper.listOrSearch(Dict.create());
        //把根分类区分出来
        List<Dict> rootList = list.stream().filter(root -> root.getInt(SMenuConstants.PARENT_ID_NAME) == 0).collect(Collectors.toList());
        //把非根分类区分出来
        List<Dict> subList = list.stream().filter(sub -> sub.getInt(SMenuConstants.PARENT_ID_NAME) != 0).collect(Collectors.toList());
        //递归构建结构化的分类信息
        rootList.forEach(root -> buildSubs(root, subList));
        return rootList;
    }

    @Override
    public boolean openStatus(Dict param) {
        final int id = param.getInt(SMenuConstants.PRIMARY_KEY);
        final Dict condition = Dict.create().set(SMenuConstants.PRIMARY_KEY, id);
        return modifyStatus(GXBusinessStatusCode.NORMAL.getCode(), condition);
    }

    @Override
    public boolean closeStatus(Dict param) {
        final int id = param.getInt(SMenuConstants.PRIMARY_KEY);
        final Dict condition = Dict.create().set(SMenuConstants.PRIMARY_KEY, id);
        return modifyStatus(GXBusinessStatusCode.OFF_STATE.getCode(), condition);
    }

    @Override
    public boolean freezeStatus(Dict param) {
        final int id = param.getInt(SMenuConstants.PRIMARY_KEY);
        final Dict condition = Dict.create().set(SMenuConstants.PRIMARY_KEY, id);
        return modifyStatus(GXBusinessStatusCode.FREEZE.getCode(), condition);
    }

    @Override
    public Set<String> getAllPerms(Long adminId) {
        final HashSet<Object> perms = CollUtil.newHashSet();
        final Set<String> allPerms = baseMapper.getAllPerms(adminId);
        return allPerms;
    }

    @Override
    public List<Integer> getAllMenuId(Long adminId) {
        return baseMapper.getAllMenuId(adminId);
    }

    /**
     * 递归构建
     *
     * @param parent 父级ID
     * @param subs   子集数据
     */
    private void buildSubs(Dict parent, List<Dict> subs) {
        List<Dict> children = subs.stream().filter(sub -> sub.getInt(SMenuConstants.PARENT_ID_NAME) == (int) parent.getInt(SMenuConstants.PRIMARY_KEY)).collect(Collectors.toList());
        parent.set("children", children);
        if (!CollectionUtils.isEmpty(children)) {//有子分类的情况
            children.forEach(child -> buildSubs(child, subs));//再次递归构建
        }
    }

    @Override
    public boolean validateExists(Object value, String field, ConstraintValidatorContext constraintValidatorContext, Dict param) throws UnsupportedOperationException {
        log.info("validateExists : {} , field : {}", value, field);
        final int parentId = Convert.toInt(value, 0);
        return parentId == 0 || 1 == checkRecordIsExists(SMenuEntity.class, Dict.create().set(SMenuConstants.PRIMARY_KEY, parentId));
    }
}
