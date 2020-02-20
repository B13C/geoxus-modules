package com.geoxus.modules.system.service.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.system.constant.SAdminConstants;
import com.geoxus.modules.system.constant.SPermissionsConstants;
import com.geoxus.modules.system.entity.SAdminEntity;
import com.geoxus.modules.system.entity.SAdminHasPermissionsEntity;
import com.geoxus.modules.system.entity.SPermissionsEntity;
import com.geoxus.modules.system.mapper.SAdminHasPermissionsMapper;
import com.geoxus.modules.system.service.SAdminHasPermissionsService;
import com.geoxus.modules.system.service.SAdminService;
import com.geoxus.modules.system.service.SPermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SAdminHasPermissionsServiceImpl extends ServiceImpl<SAdminHasPermissionsMapper, SAdminHasPermissionsEntity> implements SAdminHasPermissionsService {
    @Autowired
    private SPermissionsService sPermissionsService;

    @Autowired
    private SAdminService sAdminService;

    @Override
    public long create(SAdminHasPermissionsEntity target, Dict param) {
        return 0;
    }

    @Override
    public long update(SAdminHasPermissionsEntity target, Dict param) {
        return 0;
    }

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
    public Dict detail(Dict param) {
        return null;
    }

    @Override
    public boolean addPermissionBatch(long adminId, List<Long> permissions) {
        final Integer sAdminExists = sAdminService.checkRecordIsExists(SAdminEntity.class, Dict.create().set(SAdminConstants.PRIMARY_KEY, adminId));
        if (null == sAdminExists) {
            return false;
        }
        final Dict deleteCondition = Dict.create().set(SAdminConstants.PRIMARY_KEY, adminId);
        final Integer sAdminHasPermissionExists = checkRecordIsExists(SAdminHasPermissionsEntity.class, deleteCondition);
        boolean remove = true;
        if (null != sAdminHasPermissionExists) {
            final QueryWrapper<SAdminHasPermissionsEntity> removeQuery = new QueryWrapper<SAdminHasPermissionsEntity>().allEq(deleteCondition);
            remove = remove(removeQuery);
        }
        if (remove) {
            final ArrayList<SAdminHasPermissionsEntity> addEntityList = new ArrayList<>();
            for (Long permissionId : permissions) {
                final SAdminHasPermissionsEntity entity = new SAdminHasPermissionsEntity();
                final Integer permissionExists = sPermissionsService.checkRecordIsExists(SPermissionsEntity.class, Dict.create().set(SPermissionsConstants.PRIMARY_KEY, permissionId));
                if (null == permissionExists) {
                    continue;
                }
                entity.setAdminId(adminId);
                entity.setPermissionId(permissionId);
                addEntityList.add(entity);
            }
            return saveBatch(addEntityList);
        }
        return false;
    }
}
