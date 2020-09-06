package com.geoxus.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.exception.GXException;
import com.geoxus.core.common.oauth.GXTokenManager;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.core.common.vo.GXResultCode;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.system.constant.SAdminConstants;
import com.geoxus.modules.system.entity.SAdminEntity;
import com.geoxus.modules.system.mapper.SAdminMapper;
import com.geoxus.modules.system.service.SAdminRolesService;
import com.geoxus.modules.system.service.SAdminService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.List;

@Service
public class SAdminServiceImpl extends ServiceImpl<SAdminMapper, SAdminEntity> implements SAdminService {
    @Autowired
    private SAdminRolesService sAdminRolesService;

    @Override
    public long create(SAdminEntity target, Dict param) {
        final String salt = RandomUtil.randomString(8);
        target.setSalt(salt);
        target.setPassword(SecureUtil.md5(target.getPassword() + salt));
        save(target);
        return target.getAdminId();
    }

    @Override
    public long update(SAdminEntity target, Dict param) {
        final String salt = RandomUtil.randomString(8);
        target.setSalt(salt);
        target.setPassword(SecureUtil.md5(target.getPassword() + salt));
        updateById(target);
        return target.getAdminId();
    }

    @Override
    public boolean delete(Dict param) {
        final Dict condition = Dict.create().set(getPrimaryKey(), param.getInt(getPrimaryKey()));
        return modifyStatus(GXBusinessStatusCode.DELETED.getCode(), condition);
    }

    @Override
    public GXPagination<Dict> listOrSearchPage(Dict param) {
        return generatePage(param, Dict.create());
    }

    @Override
    public Dict detail(Dict param) {
        return baseMapper.detail(param);
    }

    @Override
    public boolean changePassword(Dict param) {
        final String oldPassword = param.getStr("old_password");
        final String newPassword = param.getStr("new_password");
        if (StrUtil.isBlank(oldPassword) || StrUtil.isBlank(newPassword)) {
            throw new GXException("新密码或旧密码不能为空!");
        }
        if (StrUtil.equals(oldPassword, newPassword)) {
            throw new GXException("新旧密码相同!");
        }
        if (StrUtil.length(newPassword) < 8) {
            throw new GXException("密码长度不能小于8位!");
        }
        final long id = param.getLong(getPrimaryKey());
        final SAdminEntity entity = getById(id);
        if (entity.getStatus() != GXBusinessStatusCode.NORMAL.getCode()) {
            throw new GXException("账号状态不正常,请联系管理员!");
        }
        final String salt = RandomUtil.randomString(RandomUtil.randomInt(6, 8));
        entity.setSalt(salt);
        String pwd = SecureUtil.md5(newPassword + salt);
        entity.setPassword(pwd);
        return updateById(entity);
    }

    @Override
    public Dict login(Dict param) {
        SAdminEntity adminEntity = getOne(new QueryWrapper<SAdminEntity>().eq("username", param.getStr("username")).eq("status", GXBusinessStatusCode.NORMAL.getCode()));
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
        return Dict.create().set("nick_name", adminEntity.getNickName()).set(GXTokenManager.ADMIN_TOKEN, token);
    }

    @Override
    @RequiresPermissions("sys:admin:freeze")
    @RequiresRoles("administrator")
    public boolean freeze(Dict param) {
        final Dict condition = Dict.create().set(getPrimaryKey(), param.getInt(getPrimaryKey()));
        return modifyStatus(GXBusinessStatusCode.FREEZE.getCode(), condition);
    }

    @Override
    @RequiresPermissions("sys:admin:unfreeze")
    @RequiresRoles("administrator")
    public boolean unfreeze(Dict param) {
        final Dict condition = Dict.create().set(getPrimaryKey(), param.getInt(getPrimaryKey()));
        return modifyStatus(GXBusinessStatusCode.NORMAL.getCode(), condition);
    }

    @Override
    @RequiresPermissions("sys:admin:assign:roles:to:admin")
    @RequiresRoles("administrator")
    public boolean assignRolesToAdmin(Long adminId, List<Long> roleIds) {
        return sAdminRolesService.addRoleToAdmin(adminId, roleIds);
    }

    @Override
    public boolean validateExists(Object value, String field, ConstraintValidatorContext constraintValidatorContext, Dict param) throws UnsupportedOperationException {
        if (0 == Long.parseLong(value.toString())) {
            return true;
        }
        final Dict condition = Dict.create().set(SAdminConstants.PRIMARY_KEY, value);
        return null != checkRecordIsExists(SAdminEntity.class, condition);
    }

    @Override
    public boolean validateUnique(Object value, String field, ConstraintValidatorContext constraintValidatorContext, Dict param) {
        final Dict condition = Dict.create().set("username", value);
        return null != checkRecordIsExists(SAdminEntity.class, condition);
    }

    @Override
    public String getPrimaryKey() {
        return SAdminConstants.PRIMARY_KEY;
    }

    @Override
    public Dict getStatus(long adminId) {
        final Dict condition = Dict.create().set(SAdminConstants.PRIMARY_KEY, adminId);
        HashSet<String> columns = CollUtil.newHashSet("admin_id", "status", "is_super_admin");
        return getFieldValueBySQL(SAdminEntity.class, columns, condition, false);
    }
}
