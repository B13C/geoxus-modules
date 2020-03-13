package com.geoxus.modules.system.mapper;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.modules.system.builder.SRolesBuilder;
import com.geoxus.modules.system.entity.SRolesEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface SRolesMapper extends GXBaseMapper<SRolesEntity> {
    @Override
    @SelectProvider(type = SRolesBuilder.class, method = "listOrSearch")
    List<Dict> listOrSearchPage(IPage<Dict> page, Dict param);

    @SelectProvider(type = SRolesBuilder.class, method = "detail")
    Dict detail(Dict param);

    @SelectProvider(type = SRolesBuilder.class, method = "getIDS")
    List<Integer> getIDS(Long adminId);
}
