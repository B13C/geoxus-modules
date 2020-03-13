package com.geoxus.modules.system.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.core.common.validator.GXValidateDBExists;
import com.geoxus.modules.system.entity.SMenuEntity;

import java.util.List;
import java.util.Set;

public interface SMenuService extends GXBusinessService<SMenuEntity>, GXValidateDBExists {
    /**
     * 获取树状结构
     *
     * @return List
     */
    List<Dict> getTree();

    /**
     * 获取管理员的permissions
     *
     * @param adminId 用户ID
     * @return Set
     */
    Set<String> getAllPerms(Long adminId);

    /**
     * 获取管理员的菜单列表
     *
     * @param adminId 用户ID
     * @return Set
     */
    List<Integer> getAllMenuId(Long adminId);
}
