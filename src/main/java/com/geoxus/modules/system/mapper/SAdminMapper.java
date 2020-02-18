package com.geoxus.modules.system.mapper;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.modules.system.builder.SAdminBuilder;
import com.geoxus.modules.system.entity.SAdminEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * 管理员
 */
@Mapper
public interface SAdminMapper extends GXBaseMapper<SAdminEntity> {
    @Override
    @SelectProvider(type = SAdminBuilder.class, method = "listOrSearch")
    List<Dict> listOrSearchPage(IPage<Dict> page, Dict param);

    @SelectProvider(type = SAdminBuilder.class, method = "detail")
    Dict detail(Dict param);
}
