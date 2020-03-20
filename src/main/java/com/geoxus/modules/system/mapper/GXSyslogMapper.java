package com.geoxus.modules.system.mapper;

import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.modules.system.entity.GXSyslogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统操作日志
 */
@Mapper
public interface GXSyslogMapper extends GXBaseMapper<GXSyslogEntity> {

}