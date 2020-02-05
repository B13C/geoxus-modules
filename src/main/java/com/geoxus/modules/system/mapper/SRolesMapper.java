package com.geoxus.modules.system.mapper;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.modules.system.builder.RoleBuilder;
import com.geoxus.modules.system.entity.SRolesEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface SRolesMapper extends GXBaseMapper<SRolesEntity> {
    @Override
    @SelectProvider(type = RoleBuilder.class, method = "listOrSearch")
    List<Dict> listOrSearch(IPage<Dict> page, Dict param);

    @SelectProvider(type = RoleBuilder.class, method = "detail")
    Dict detail(Dict param);
}
