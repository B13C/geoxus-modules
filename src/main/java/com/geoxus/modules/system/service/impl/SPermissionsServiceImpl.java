package com.geoxus.modules.system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.util.GXCommonUtils;
import com.geoxus.core.common.util.GXShiroUtils;
import com.geoxus.modules.system.constant.SPermissionsConstants;
import com.geoxus.modules.system.entity.SPermissionsEntity;
import com.geoxus.modules.system.mapper.SPermissionsMapper;
import com.geoxus.modules.system.service.SMenuService;
import com.geoxus.modules.system.service.SPermissionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidatorContext;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SPermissionsServiceImpl extends ServiceImpl<SPermissionsMapper, SPermissionsEntity> implements SPermissionsService {
    @Autowired
    private SMenuService sMenuService;

    @Override
    public List<SPermissionsEntity> getPermissionsTree() {
        return super.list(new QueryWrapper<>());
    }

    @Override
    public long create(SPermissionsEntity target, Dict param) {
        save(target);
        return target.getPermissionId();
    }

    @Override
    public boolean delete(Dict param) {
        final List<Long> ids = Convert.convert(new TypeReference<List<Long>>() {
        }, param.getObj(getPrimaryKey()));
        final QueryWrapper<SPermissionsEntity> conditionQuery = new QueryWrapper<SPermissionsEntity>().in(SPermissionsConstants.PRIMARY_KEY, ids);
        return baseMapper.delete(conditionQuery) > 0;
    }

    @Override
    public Set<String> getAdminAllPermissions(Long adminId) {
        adminId = adminId == null ? GXShiroUtils.getAdminId() : adminId;
        final Long superAdminId = GXCommonUtils.getEnvironmentValue("super.admin.id", Long.class);
        if (0 == adminId.compareTo(superAdminId)) {
            return baseMapper.getAllPermissionsCode();
        }
        return sMenuService.getAllPerms(adminId);
    }

    @Override
    public Set<String> getPermissionsCode(List<Integer> permissionIds) {
        if (permissionIds.isEmpty()) {
            return Collections.emptySet();
        }
        final String permissionIdStr = permissionIds.stream().map(Object::toString).collect(Collectors.joining(","));
        return baseMapper.getPermissionCodeByPermissionIds(permissionIdStr);
    }

    @Override
    public String getPrimaryKey() {
        return SPermissionsConstants.PRIMARY_KEY;
    }

    @Override
    public boolean validateUnique(Object value, String field, ConstraintValidatorContext constraintValidatorContext, Dict param) {
        log.info("value : {} , field_name : {}", value, field);
        final Integer val = checkRecordIsUnique(SPermissionsEntity.class, Dict.create().set(field, value));
        return val > 0;
    }
}
