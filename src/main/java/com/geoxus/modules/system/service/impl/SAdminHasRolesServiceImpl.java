package com.geoxus.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.modules.system.entity.SAdminHasRolesEntity;
import com.geoxus.modules.system.mapper.SAdminHasRolesMapper;
import com.geoxus.modules.system.service.SAdminHasRolesService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SAdminHasRolesServiceImpl extends ServiceImpl<SAdminHasRolesMapper, SAdminHasRolesEntity> implements SAdminHasRolesService {
    @Override
    public Set<String> getAdminRoles(long adminId) {
        return baseMapper.getRoleNameByAdminId(adminId);
    }
}
