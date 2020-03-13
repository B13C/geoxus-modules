package com.geoxus.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.modules.system.constant.SAdminConstants;
import com.geoxus.modules.system.constant.SRolesConstants;
import com.geoxus.modules.system.entity.SAdminEntity;
import com.geoxus.modules.system.entity.SAdminRolesEntity;
import com.geoxus.modules.system.entity.SRolesEntity;
import com.geoxus.modules.system.mapper.SAdminRolesMapper;
import com.geoxus.modules.system.service.SAdminRolesService;
import com.geoxus.modules.system.service.SAdminService;
import com.geoxus.modules.system.service.SRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SAdminRolesServiceImpl extends ServiceImpl<SAdminRolesMapper, SAdminRolesEntity> implements SAdminRolesService {
    @Autowired
    private SRolesService sRolesService;

    @Autowired
    private SAdminService sAdminService;

    @Override
    public Dict getAdminRoles(long adminId) {
        final List<Dict> roles = baseMapper.getRolesByAdminId(adminId);
        final Dict dict = Dict.create();
        for (Dict role : roles) {
            dict.set(role.getStr("role_id"), role.getStr("role_name"));
        }
        return dict;
    }

    @Override
    public boolean addRoleToAdmin(long adminId, List<Long> roleIds) {
        final Integer sAdminExists = sAdminService.checkRecordIsExists(SAdminEntity.class, Dict.create().set(SAdminConstants.PRIMARY_KEY, adminId));
        if (null == sAdminExists) {
            return false;
        }
        boolean remove = true;
        final QueryWrapper<SAdminRolesEntity> deleteQuery = new QueryWrapper<SAdminRolesEntity>().eq(SAdminConstants.PRIMARY_KEY, adminId);
        if (null != checkRecordIsExists(SAdminRolesEntity.class, Dict.create().set(SAdminConstants.PRIMARY_KEY, adminId))) {
            remove = remove(deleteQuery);
        }
        if (remove) {
            final ArrayList<SAdminRolesEntity> addListEntity = CollUtil.newArrayList();
            for (Long roleId : roleIds) {
                final Integer sRoleExists = sRolesService.checkRecordIsExists(SRolesEntity.class, Dict.create().set(SRolesConstants.PRIMARY_KEY, roleId));
                if (null == sRoleExists) {
                    continue;
                }
                final SAdminRolesEntity entity = new SAdminRolesEntity();
                entity.setAdminId(adminId);
                entity.setRoleId(roleId);
                addListEntity.add(entity);
            }
            if (!addListEntity.isEmpty()) {
                return saveBatch(addListEntity);
            }
        }
        return false;
    }
}
