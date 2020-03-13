package com.geoxus.modules.system.service.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.util.GXChineseToPinYinUtils;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.system.constant.SRolesConstants;
import com.geoxus.modules.system.entity.SRolesEntity;
import com.geoxus.modules.system.mapper.SRolesMapper;
import com.geoxus.modules.system.service.SRolesService;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidatorContext;
import java.util.List;

@Service
public class SRolesServiceImpl extends ServiceImpl<SRolesMapper, SRolesEntity> implements SRolesService {
    @Override
    public long create(SRolesEntity target, Dict param) {
        final String fullPinyin = GXChineseToPinYinUtils.getFullLetter(target.getRoleName(), "_", PinyinFormat.WITHOUT_TONE);
        final String shortPinyin = GXChineseToPinYinUtils.getFullFirstLetter(target.getRoleName());
        final Dict condition = Dict.create().set("role_pinyin_short_name", shortPinyin).set("role_pinyin_name", fullPinyin);
        if (null != checkRecordIsExists(SRolesEntity.class, condition)) {
            return -1;
        }
        target.setRolePinyinName(fullPinyin);
        target.setRolePinyinShortName(shortPinyin);
        target.setPath(getParentPath(SRolesEntity.class, target.getParentId(), true));
        save(target);
        return target.getRoleId();
    }

    @Override
    public long update(SRolesEntity target, Dict param) {
        final String fullPinyin = GXChineseToPinYinUtils.getFullLetter(target.getRoleName(), "_", PinyinFormat.WITHOUT_TONE);
        final String shortPinyin = GXChineseToPinYinUtils.getFullFirstLetter(target.getRoleName());
        final Dict condition = Dict.create().set("role_pinyin_short_name", shortPinyin).set("role_pinyin_name", fullPinyin);
        if (null != checkRecordIsExists(SRolesEntity.class, condition)) {
            return -1;
        }
        target.setRolePinyinName(fullPinyin);
        target.setRolePinyinShortName(shortPinyin);
        target.setPath(getParentPath(SRolesEntity.class, target.getParentId(), true));
        updateById(target);
        return target.getRoleId();
    }

    @Override
    public boolean delete(Dict param) {
        final Dict condition = Dict.create().set(SRolesConstants.PRIMARY_KEY, param.getObj(SRolesConstants.PRIMARY_KEY));
        return modifyStatus(GXBusinessStatusCode.DELETED.getCode(), condition);
    }

    @Override
    public GXPagination listOrSearchPage(Dict param) {
        return generatePage(param, Dict.create());
    }

    @Override
    public Dict detail(Dict param) {
        final Dict detail = baseMapper.detail(param);
        return detail;
    }

    @Override
    public String getPrimaryKey() {
        return SRolesConstants.PRIMARY_KEY;
    }

    @Override
    public boolean validateExists(Object value, String field, ConstraintValidatorContext constraintValidatorContext, Dict param) throws UnsupportedOperationException {
        final Integer exists = checkRecordIsExists(SRolesEntity.class, Dict.create().set(SRolesConstants.PRIMARY_KEY, value));
        return null != exists;
    }

    @Override
    public boolean freeze(Dict param) {
        final Dict condition = Dict.create().set(SRolesConstants.PRIMARY_KEY, param.getObj(SRolesConstants.PRIMARY_KEY));
        return modifyStatus(GXBusinessStatusCode.FREEZE.getCode(), condition);
    }

    @Override
    public boolean unfreeze(Dict param) {
        final Dict condition = Dict.create().set(SRolesConstants.PRIMARY_KEY, param.getObj(SRolesConstants.PRIMARY_KEY));
        return modifyStatus(GXBusinessStatusCode.NORMAL.getCode(), condition);
    }

    @Override
    public List<Integer> getIDS(Long adminId) {
        return baseMapper.getIDS(adminId);
    }
}
