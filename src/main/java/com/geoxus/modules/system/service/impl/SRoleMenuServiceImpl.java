package com.geoxus.modules.system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.system.constant.SMenuConstants;
import com.geoxus.modules.system.entity.SRoleMenuEntity;
import com.geoxus.modules.system.mapper.SRoleMenuMapper;
import com.geoxus.modules.system.service.SPermissionsService;
import com.geoxus.modules.system.service.SRoleMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SRoleMenuServiceImpl extends ServiceImpl<SRoleMenuMapper, SRoleMenuEntity> implements SRoleMenuService {
    @Autowired
    private SPermissionsService sPermissionsService;

    @Override
    public long create(SRoleMenuEntity target, Dict param) {
        save(target);
        return target.getMenuId();
    }

    @Override
    public long update(SRoleMenuEntity target, Dict param) {
        updateById(target);
        return target.getMenuId();
    }

    @Override
    public boolean delete(Dict param) {
        final List<Integer> ids = Convert.convert(new TypeReference<List<Integer>>() {
        }, param.getObj(SMenuConstants.PRIMARY_KEY));
        final ArrayList<SRoleMenuEntity> updateList = new ArrayList<>();
        for (int id : ids) {
            SRoleMenuEntity entity = getById(id);
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
        return Dict.create();
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
    public boolean validateExists(Object value, String field, ConstraintValidatorContext constraintValidatorContext, Dict param) throws UnsupportedOperationException {
        log.info("validateExists : {} , field : {}", value, field);
        final int parentId = Convert.toInt(value, 0);
        return parentId == 0 || 1 == checkRecordIsExists(SRoleMenuEntity.class, Dict.create().set(SMenuConstants.PRIMARY_KEY, parentId));
    }
}
