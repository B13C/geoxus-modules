package com.geoxus.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.oauth.GXTokenManager;
import com.geoxus.core.common.util.GXCommonUtils;
import com.geoxus.core.common.util.GXShiroUtils;
import com.geoxus.modules.system.constant.SAdminConstants;
import com.geoxus.modules.system.constant.SRolesConstants;
import com.geoxus.modules.system.entity.SAdminHasPermissionsEntity;
import com.geoxus.modules.system.entity.SPermissionsEntity;
import com.geoxus.modules.system.entity.SRoleHasPermissionsEntity;
import com.geoxus.modules.system.mapper.SPermissionsMapper;
import com.geoxus.modules.system.service.SAdminHasPermissionsService;
import com.geoxus.modules.system.service.SPermissionsService;
import com.geoxus.modules.system.service.SRoleHasPermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SPermissionsServiceImpl extends ServiceImpl<SPermissionsMapper, SPermissionsEntity> implements SPermissionsService {
    @Autowired
    private SAdminHasPermissionsService sAdminHasPermissionsService;

    @Autowired
    private SRoleHasPermissionsService sRoleHasPermissionsService;

    @Override
    public List<SPermissionsEntity> getPermissionsTree() {
        return super.list(new QueryWrapper<>());
    }

    @Override
    public Set<String> getAdminAllPermissions(Long adminId) {
        adminId = adminId == null ? GXShiroUtils.getAdminId() : adminId;
        final Long superAdminId = GXCommonUtils.getEnvironmentValue("super.admin.id", Long.class);
        if (0 == adminId.compareTo(superAdminId)) {
            return baseMapper.getAllPermissionCode();
        }
        return baseMapper.getAdminAllPermissions(Dict.create().set(GXTokenManager.ADMIN_ID, adminId));
    }

    @Override
    public Set<String> getPermissionsCode(List<Integer> permissionIds) {
        if (permissionIds.isEmpty()) {
            return CollUtil.newHashSet();
        }
        final String permissionIdStr = permissionIds.stream().map(Object::toString).collect(Collectors.joining(","));
        return baseMapper.getPermissionsCode(permissionIdStr);
    }

    @Override
    public List<Long> getRolePermissions(Long roleId) {
        List<SRoleHasPermissionsEntity> list = sRoleHasPermissionsService.list(new QueryWrapper<SRoleHasPermissionsEntity>().eq(SRolesConstants.PRIMARY_KEY, roleId));
        return list.stream().map(SRoleHasPermissionsEntity::getPermissionId).collect(Collectors.toList());
    }

    @Override
    public List<Long> getAdminPermissions(long adminId) {
        List<SAdminHasPermissionsEntity> list = sAdminHasPermissionsService.list(new QueryWrapper<SAdminHasPermissionsEntity>().eq(SAdminConstants.PRIMARY_KEY, adminId));
        return list.stream().map(SAdminHasPermissionsEntity::getPermissionId).collect(Collectors.toList());
    }
}
