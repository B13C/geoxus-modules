package com.geoxus.modules.system.mapper;

import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.modules.system.entity.CommonOperationLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统操作日志
 */
@Mapper
public interface CommonOperationLogMapper extends GXBaseMapper<CommonOperationLogEntity> {

}