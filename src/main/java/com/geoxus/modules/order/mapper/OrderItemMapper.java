package com.geoxus.modules.order.mapper;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.geoxus.core.common.mapper.GXBaseMapper;
import com.geoxus.core.framework.handler.GXJsonToMapTypeHandler;
import com.geoxus.modules.order.builder.OrderItemBuilder;
import com.geoxus.modules.order.entity.OrderItemEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderItemMapper extends GXBaseMapper<OrderItemEntity> {
    @Override
    @SelectProvider(type = OrderItemBuilder.class, method = "listOrSearch")
    @Results(id = "orderItemResult", value = {
            @Result(column = "ext", property = "ext", typeHandler = GXJsonToMapTypeHandler.class),
            @Result(column = "{goods_id=goods_id}", property = "goodsInfo", one = @One(
                    select = "com.geoxus.modules.goods.mapper.GoodsMapper.basicInfo"
            ), javaType = Map.class)
    })
    List<Dict> listOrSearchPage(IPage<Dict> page, Dict param);

    @SelectProvider(type = OrderItemBuilder.class, method = "detail")
    @ResultMap("orderItemResult")
    Dict detail(Dict param);
}
