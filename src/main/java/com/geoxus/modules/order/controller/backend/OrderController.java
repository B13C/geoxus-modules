package com.geoxus.modules.order.controller.backend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXRequestBodyToEntityAnnotation;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.oauth.GXTokenManager;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.order.entity.OrderEntity;
import com.geoxus.modules.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController("backendOrderController")
@RequestMapping("/order/backend")
public class OrderController implements GXController<OrderEntity> {
    @Autowired
    private OrderService orderService;

    @Override
    @PostMapping("/create")
    public GXResultUtils create(@Valid @GXRequestBodyToEntityAnnotation OrderEntity target) {
        final long userId = getUserIdFromToken(GXTokenManager.USER_TOKEN, GXTokenManager.USER_ID);
        final long i = orderService.create(target, Dict.create().set(GXTokenManager.USER_ID, userId));
        return GXResultUtils.ok().putData(Dict.create().set("id", i));
    }

    @Override
    @PostMapping("/update")
    public GXResultUtils update(@Valid @GXRequestBodyToEntityAnnotation OrderEntity target) {
        final long userId = getUserIdFromToken(GXTokenManager.USER_TOKEN, GXTokenManager.USER_ID);
        final long i = orderService.update(target, Dict.create().set(GXTokenManager.USER_ID, userId));
        return GXResultUtils.ok().putData(Dict.create().set("id", i));
    }

    @Override
    @PostMapping("/delete")
    public GXResultUtils delete(@RequestBody Dict param) {
        final boolean b = orderService.delete(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @Override
    @PostMapping("/list-or-search")
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        final GXPagination page = orderService.listOrSearchPage(param);
        return GXResultUtils.ok().putData(page);
    }

    @Override
    @PostMapping("/detail")
    public GXResultUtils detail(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(orderService.detail(param));
    }

    @PostMapping("/cancel-order-status")
    public GXResultUtils cancelOrderStatus(@RequestBody Dict param) {
        final boolean b = orderService.cancelOrderStatus(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }
}
