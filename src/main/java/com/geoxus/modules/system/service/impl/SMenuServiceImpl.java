package com.geoxus.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.system.constant.SMenuConstants;
import com.geoxus.modules.system.entity.SMenuEntity;
import com.geoxus.modules.system.mapper.SMenuMapper;
import com.geoxus.modules.system.service.SMenuService;
import com.geoxus.modules.system.service.SPermissionsService;
import com.geoxus.modules.system.service.SRolesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidatorContext;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SMenuServiceImpl extends ServiceImpl<SMenuMapper, SMenuEntity> implements SMenuService {
    @Autowired
    private SPermissionsService sPermissionsService;

    @Autowired
    private SRolesService sRolesService;

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
        final List<Integer> ids = Convert.convert(new TypeReference<List<Integer>>() {
        }, param.getObj(SMenuConstants.PRIMARY_KEY));
        return removeByIds(ids);
    }

    @Override
    public GXPagination<Dict> listOrSearchPage(Dict param) {
        return generatePage(param, Dict.create());
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
    public Set<String> getAllPerms(Long adminId) {
        final Set<String> permsSet = CollUtil.newHashSet();
        List<Integer> roleIds = sRolesService.getRoleIdsByAdminId(adminId);
        Set<String> roleAllPerms = getRoleAllPerms(roleIds);
        Set<String> adminAllPerms = getAdminAllPerms(adminId);
        permsSet.addAll(roleAllPerms);
        permsSet.addAll(adminAllPerms);
        return permsSet;
    }

    @Override
    public List<Integer> getAllMenuId(Long adminId) {
        return baseMapper.getAllMenuId(adminId);
    }

    @Override
    public Set<String> getRoleAllPerms(List<Integer> roles) {
        HashSet<String> permsSet = CollUtil.newHashSet();
        if (null == roles || roles.isEmpty()) {
            return permsSet;
        }
        List<Dict> roleAllPerms = baseMapper.getRoleAllPerms(roles.stream().map(Objects::toString).collect(Collectors.joining(",")));
        for (Dict dict : roleAllPerms) {
            final String perms = dict.getStr("perms");
            if (StrUtil.isNotBlank(perms)) {
                if (StrUtil.isBlank(perms)) {
                    continue;
                }
                permsSet.addAll(Arrays.asList(perms.trim().split(",")));
            }
        }
        return permsSet;
    }

    @Override
    public Set<String> getAdminAllPerms(Long adminId) {
        HashSet<String> permsSet = CollUtil.newHashSet();
        if (null == adminId || adminId == 0) {
            return permsSet;
        }
        List<Dict> adminAllPerms = baseMapper.getAdminAllPerms(adminId);
        for (Dict dict : adminAllPerms) {
            final String perms = dict.getStr("perms");
            if (StrUtil.isNotBlank(perms)) {
                if (StrUtil.isBlank(perms)) {
                    continue;
                }
                permsSet.addAll(Arrays.asList(perms.trim().split(",")));
            }
        }
        return permsSet;
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
