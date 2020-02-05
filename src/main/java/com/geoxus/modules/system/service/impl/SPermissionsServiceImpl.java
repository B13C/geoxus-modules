package com.geoxus.modules.system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.util.GXCommonUtils;
import com.geoxus.core.common.util.GXShiroUtils;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.system.entity.SAdminHasRolesEntity;
import com.geoxus.modules.system.entity.SPermissionsEntity;
import com.geoxus.modules.system.entity.SRoleHasPermissionsEntity;
import com.geoxus.modules.system.mapper.SPermissionsMapper;
import com.geoxus.modules.system.service.SAdminHasRolesService;
import com.geoxus.modules.system.service.SPermissionsService;
import com.geoxus.modules.system.service.SRoleHasPermissionsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SPermissionsServiceImpl extends ServiceImpl<SPermissionsMapper, SPermissionsEntity> implements SPermissionsService {
    @Autowired
    private SAdminHasRolesService adminHasRolesService;

    @Autowired
    private SRoleHasPermissionsService roleHasPermissionsService;

    @Override
    public List<SPermissionsEntity> getPermissionsTree() {
        List<SPermissionsEntity> list = super.list(new QueryWrapper<>());
        //把根分类区分出来
        List<SPermissionsEntity> roots = list.stream().filter(root -> root.getParentId() == 0).collect(Collectors.toList());
        //把非根分类区分出来
        List<SPermissionsEntity> subs = list.stream().filter(sub -> sub.getParentId() != 0).collect(Collectors.toList());
        //递归构建结构化的分类信息
        roots.forEach(root -> buildSubs(root, subs));
        return roots;
    }

    /**
     * 递归构建
     *
     * @param parent
     * @param subs
     */
    private void buildSubs(SPermissionsEntity parent, List<SPermissionsEntity> subs) {
        List<SPermissionsEntity> children = subs.stream().filter(sub -> sub.getParentId() == parent.getPermissionId()).collect(Collectors.toList());
        parent.setChildren(children);
        if (!CollectionUtils.isEmpty(children)) {//有子分类的情况
            children.forEach(child -> buildSubs(child, subs));//再次递归构建
        }
    }

    @Override
    public Set<String> getAdminPermissions(long adminId) {
        long id = 0;
        if (adminId != 0) {
            id = adminId;
        } else {
            id = GXShiroUtils.getAdminId();
        }
        Set<String> codeSet = new HashSet<>();

        if (id == GXCommonUtils.getEnvironmentValue("super.admin.id", Long.class)) {
            //超级管理员直接获取 所有权限
            List<SPermissionsEntity> list = super.list(new QueryWrapper<SPermissionsEntity>().isNotNull("name"));
            //组装权限CODE
            list.forEach(x -> {
                if (StringUtils.isNotBlank(x.getPermissionCode())) {
                    codeSet.addAll(Arrays.asList(x.getPermissionCode().trim().split(",")));
                }
            });
        } else {
            //普通管理员
            SAdminHasRolesEntity adminHasRoles = adminHasRolesService.getOne(new QueryWrapper<SAdminHasRolesEntity>().eq("admin_id", id));
            if (null != adminHasRoles) {
                //获取角色与权限的对应
                List<SRoleHasPermissionsEntity> roleHasPermissionsList = roleHasPermissionsService.list(new QueryWrapper<SRoleHasPermissionsEntity>().eq("role_id", adminHasRoles.getRoleId()));
                //获得权限ID
                List<Integer> idList = roleHasPermissionsList.stream().map(SRoleHasPermissionsEntity::getPermissionId).collect(Collectors.toList());
                if (!idList.isEmpty()) {
                    List<SPermissionsEntity> list = super.list(new QueryWrapper<SPermissionsEntity>().in("id", idList));
                    //组装权限CODE
                    list.forEach(x -> {
                        if (StringUtils.isNotBlank(x.getPermissionCode())) {
                            codeSet.addAll(Arrays.asList(x.getPermissionCode().trim().split(",")));
                        }
                    });
                }
            }
        }
        return codeSet;
    }

    @Override
    @Transactional
    public void updateRolePermissions(Dict param) {
        final Integer roleId = param.getInt("role_id");
        //删除之前的权限
        roleHasPermissionsService.remove(new QueryWrapper<SRoleHasPermissionsEntity>().eq("role_id", roleId));
        //保存权限
        final List<Integer> permissionIds = Convert.convert(List.class, param.getObj("permission_ids"));
        if (permissionIds != null && !permissionIds.isEmpty()) {
            permissionIds.forEach(id -> {
                SRoleHasPermissionsEntity entity = new SRoleHasPermissionsEntity();
                entity.setPermissionId(id);
                entity.setRoleId(roleId);
                roleHasPermissionsService.save(entity);
            });
        }
    }

    @Override
    public List<Integer> getRolePermissions(Integer roleId) {
        List<SRoleHasPermissionsEntity> list = roleHasPermissionsService.list(new QueryWrapper<SRoleHasPermissionsEntity>().eq("role_id", roleId));
        List<Integer> idList = list.stream().map(x -> x.getPermissionId()).collect(Collectors.toList());
        return idList;
    }

    @Override
    public long create(SPermissionsEntity target, Dict param) {
        return 0;
    }

    @Override
    public long update(SPermissionsEntity target, Dict param) {
        return 0;
    }

    @Override
    public boolean delete(Dict param) {
        return false;
    }

    @Override
    public GXPagination listOrSearch(Dict param) {
        return null;
    }

    @Override
    public Dict detail(Dict param) {
        return null;
    }
}
