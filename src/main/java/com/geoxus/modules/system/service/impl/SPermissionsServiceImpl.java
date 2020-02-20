package com.geoxus.modules.system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.oauth.GXTokenManager;
import com.geoxus.core.common.util.GXShiroUtils;
import com.geoxus.modules.system.entity.SPermissionsEntity;
import com.geoxus.modules.system.entity.SRoleHasPermissionsEntity;
import com.geoxus.modules.system.mapper.SPermissionsMapper;
import com.geoxus.modules.system.service.SAdminHasPermissionsService;
import com.geoxus.modules.system.service.SPermissionsService;
import com.geoxus.modules.system.service.SRoleHasPermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SPermissionsServiceImpl extends ServiceImpl<SPermissionsMapper, SPermissionsEntity> implements SPermissionsService {
    @Autowired
    private SAdminHasPermissionsService sAdminHasPermissionsService;

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
        //有子分类的情况
        if (!CollectionUtils.isEmpty(children)) {
            //再次递归构建
            children.forEach(child -> buildSubs(child, subs));
        }
    }

    @Override
    public Set<String> getAdminAllPermissions(long adminId) {
        long id;
        if (adminId != 0) {
            id = adminId;
        } else {
            id = GXShiroUtils.getAdminId();
        }
        return baseMapper.getAdminAllPermissions(Dict.create().set(GXTokenManager.ADMIN_ID, id));
    }

    @Override
    @Transactional
    public void updateRolePermissions(Dict requestParam) {
        final Integer roleId = requestParam.getInt("role_id");
        //删除之前的权限
        roleHasPermissionsService.remove(new QueryWrapper<SRoleHasPermissionsEntity>().eq("role_id", roleId));
        //保存权限
        final List<Integer> permissionIds = Convert.convert(new TypeReference<List<Integer>>() {
        }, requestParam.getObj("permission_ids"));
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
    public List<Long> getRolePermissions(Integer roleId) {
        List<SRoleHasPermissionsEntity> list = roleHasPermissionsService.list(new QueryWrapper<SRoleHasPermissionsEntity>().eq("role_id", roleId));
        return list.stream().map(SRoleHasPermissionsEntity::getPermissionId).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getAdminPermissions(long adminId) {
        return null;
    }
}
