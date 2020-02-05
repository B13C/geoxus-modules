package com.geoxus.modules.order.mapper;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.core.framework.handler.GXJsonToListTypeHandler;
import com.geoxus.core.framework.handler.GXJsonToMapTypeHandler;
import com.geoxus.modules.order.builder.OrderBuilder;
import com.geoxus.modules.order.entity.OrderEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper extends GXBaseMapper<OrderEntity> {
    @Override
    @SelectProvider(type = OrderBuilder.class, method = "listOrSearch")
    @Results({
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class),
            @Result(column = "pay_info", property = "payInfo", typeHandler = GXJsonToMapTypeHandler.class),
            @Result(column = "{order_number=order_number}", property = "items", many = @Many(
                    select = "com.geoxus.modules.order.mapper.OrderItemMapper.listOrSearch"
            ))
    })
    List<Dict> listOrSearch(IPage<Dict> page, Dict param);

    @SelectProvider(type = OrderBuilder.class, method = "detail")
    @Results({
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class),
            @Result(column = "pay_info", property = "payInfo", typeHandler = GXJsonToMapTypeHandler.class),
            @Result(column = "items", property = "items", typeHandler = GXJsonToListTypeHandler.class)
    })
    Dict detail(Dict param);
}
