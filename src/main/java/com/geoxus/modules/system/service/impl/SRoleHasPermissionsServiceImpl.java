package com.geoxus.modules.system.service.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.system.constant.SPermissionsConstants;
import com.geoxus.modules.system.constant.SRolesConstants;
import com.geoxus.modules.system.entity.SPermissionsEntity;
import com.geoxus.modules.system.entity.SRoleHasPermissionsEntity;
import com.geoxus.modules.system.entity.SRolesEntity;
import com.geoxus.modules.system.mapper.SRoleHasPermissionsMapper;
import com.geoxus.modules.system.service.SPermissionsService;
import com.geoxus.modules.system.service.SRoleHasPermissionsService;
import com.geoxus.modules.system.service.SRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SRoleHasPermissionsServiceImpl extends ServiceImpl<SRoleHasPermissionsMapper, SRoleHasPermissionsEntity> implements SRoleHasPermissionsService {
    @Autowired
    private SRolesService sRolesService;

    @Autowired
    private SPermissionsService sPermissionsService;

    @Override
    public boolean delete(Dict param) {
        return false;
    }

    @Override
    public GXPagination listOrSearchPage(Dict param) {
        return null;
    }

    @Override
    public List<Dict> listOrSearch(Dict param) {
        return baseMapper.listOrSearch(param);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addPermissionBatch(long roleId, List<Long> permissions) {
        final Integer sRoleExists = sRolesService.checkRecordIsExists(SRolesEntity.class, Dict.create().set(SRolesConstants.PRIMARY_KEY, roleId));
        if (null == sRoleExists) {
            return false;
        }
        final Dict deleteCondition = Dict.create().set(SRolesConstants.PRIMARY_KEY, roleId);
        final Integer sRoleHasPermissionExists = checkRecordIsExists(SRoleHasPermissionsEntity.class, deleteCondition);
        boolean remove = true;
        if (null != sRoleHasPermissionExists) {
            final QueryWrapper<SRoleHasPermissionsEntity> removeQuery = new QueryWrapper<SRoleHasPermissionsEntity>().allEq(deleteCondition);
            remove = remove(removeQuery);
        }
        if (remove) {
            final ArrayList<SRoleHasPermissionsEntity> addEntityList = new ArrayList<>();
            for (Long permissionId : permissions) {
                final SRoleHasPermissionsEntity entity = new SRoleHasPermissionsEntity();
                final Integer permissionExists = sPermissionsService.checkRecordIsExists(SPermissionsEntity.class, Dict.create().set(SPermissionsConstants.PRIMARY_KEY, permissionId));
                if (null == permissionExists) {
                    continue;
                }
                entity.setRoleId(roleId);
                entity.setPermissionId(permissionId);
                addEntityList.add(entity);
            }
            return saveBatch(addEntityList);
        }
        return false;
    }
}
