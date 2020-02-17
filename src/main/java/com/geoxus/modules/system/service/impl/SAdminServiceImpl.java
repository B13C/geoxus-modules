package com.geoxus.modules.system.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.constant.GXBaseBuilderConstants;
import com.geoxus.core.common.oauth.GXTokenManager;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.core.common.vo.GXResultCode;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.system.entity.SAdminEntity;
import com.geoxus.modules.system.mapper.SAdminMapper;
import com.geoxus.modules.system.service.SAdminService;
import org.springframework.stereotype.Service;

@Service
public class SAdminServiceImpl extends ServiceImpl<SAdminMapper, SAdminEntity> implements SAdminService {
    @Override
    public long create(SAdminEntity target, Dict param) {
        save(target);
        return target.getAdminId();
    }

    @Override
    public long update(SAdminEntity target, Dict param) {
        updateById(target);
        return target.getAdminId();
    }

    @Override
    public boolean delete(Dict param) {
        final Dict condition = Dict.create().set(getPrimaryKey(), param.getInt(getPrimaryKey()));
        return modifyStatus(GXBusinessStatusCode.DELETED.getCode(), condition, GXBaseBuilderConstants.NON_OPERATOR);
    }

    @Override
    public GXPagination listOrSearch(Dict param) {
        return generatePage(param);
    }

    @Override
    public Dict detail(Dict param) {
        return baseMapper.detail(param);
    }

    @Override
    public boolean changePassword(Dict param) {
        final int id = param.getInt(getPrimaryKey());
        final SAdminEntity entity = getById(id);
        final String salt = RandomUtil.randomString(6);
        entity.setSalt(salt);
        String pwd = SecureUtil.md5(param.getStr("password") + salt);
        entity.setPassword(pwd);
        return updateById(entity);
    }

    @Override
    public Dict login(Dict param) {
        SAdminEntity adminEntity = getOne(new QueryWrapper<SAdminEntity>().eq("username", param.getStr("username")).ne("status", 2));
        if (null == adminEntity) {
            return Dict.create().set("code", GXResultCode.LOGIN_ERROR.getCode()).set("msg", GXResultCode.LOGIN_ERROR.getMsg());
        }
        //验证密码
        String pwd = SecureUtil.md5(param.getStr("password") + adminEntity.getSalt());
        if (!StrUtil.equals(adminEntity.getPassword(), pwd)) {
            return Dict.create().set("code", GXResultCode.LOGIN_ERROR.getCode()).set("msg", GXResultCode.LOGIN_ERROR.getMsg());
        }
        //账户冻结
        if (adminEntity.getStatus() == GXBusinessStatusCode.FREEZE.getCode()) {
            return Dict.create().set("code", GXResultCode.FREEZE.getCode()).set("msg", GXResultCode.FREEZE.getMsg());
        }
        //生成token
        String token = GXTokenManager.generateAdminToken(adminEntity.getAdminId(), Dict.create().set("username", adminEntity.getUsername()));
        final Dict dict = Dict.create().set("nickName", adminEntity.getNickName()).set(GXTokenManager.ADMIN_TOKEN, token);
        return dict;
    }

    @Override
    public boolean freeze(Dict param) {
        final Dict condition = Dict.create().set(getPrimaryKey(), param.getInt(getPrimaryKey()));
        return modifyStatus(GXBusinessStatusCode.FREEZE.getCode(), condition, GXBaseBuilderConstants.NON_OPERATOR);
    }

    @Override
    public boolean unfreeze(Dict param) {
        final Dict condition = Dict.create().set(getPrimaryKey(), param.getInt(getPrimaryKey()));
        return modifyStatus(GXBusinessStatusCode.NORMAL.getCode(), condition, GXBaseBuilderConstants.NON_OPERATOR);
    }
}
