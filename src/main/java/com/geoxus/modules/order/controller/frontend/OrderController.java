package com.geoxus.modules.order.controller.frontend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXLoginAnnotation;
import com.geoxus.core.common.annotation.GXRequestBodyToBeanAnnotation;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.oauth.GXTokenManager;
import com.geoxus.core.common.util.GXHttpContextUtils;
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
import java.util.List;

@RestController("frontendOrderController")
@RequestMapping("/order/frontend")
public class OrderController implements GXController<OrderEntity> {
    @Autowired
    private OrderService orderService;

    @Override
    @PostMapping("/create")
    @GXLoginAnnotation
    public GXResultUtils create(@Valid @GXRequestBodyToBeanAnnotation OrderEntity target) {
        final long userId = getUserIdFromToken(GXTokenManager.USER_TOKEN, GXTokenManager.USER_ID);
        final Integer flag = GXHttpContextUtils.getHttpParam("flag", Integer.class);
        final List items = GXHttpContextUtils.getHttpParam("items", List.class);
        final List coupon = GXHttpContextUtils.getHttpParam("coupon", List.class);
        final long i = orderService.create(target, Dict.create().set(GXTokenManager.USER_ID, userId).set("flag", flag).set("items", items).set("coupon", coupon));
        return GXResultUtils.ok().putData(Dict.create().set("id", i));
    }

    @Override
    @PostMapping("/update")
    @GXLoginAnnotation
    public GXResultUtils update(@Valid @GXRequestBodyToBeanAnnotation OrderEntity target) {
        final long userId = getUserIdFromToken(GXTokenManager.USER_TOKEN, GXTokenManager.USER_ID);
        final int flag = GXHttpContextUtils.getHttpParam("flag", Integer.class);
        final List items = GXHttpContextUtils.getHttpParam("items", List.class);
        final List coupon = GXHttpContextUtils.getHttpParam("coupon", List.class);
        final long i = orderService.update(target, Dict.create().set(GXTokenManager.USER_ID, userId).set("flag", flag).set("items", items).set("coupon", coupon));
        return GXResultUtils.ok().putData(Dict.create().set("id", i));
    }

    @Override
    @PostMapping("/delete")
    @GXLoginAnnotation
    public GXResultUtils delete(@RequestBody Dict param) {
        param.set(GXTokenManager.USER_ID, getUserIdFromToken(GXTokenManager.USER_TOKEN, GXTokenManager.USER_ID));
        final boolean b = orderService.delete(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @Override
    @PostMapping("/list-or-search")
    @GXLoginAnnotation
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        param.set(GXTokenManager.USER_ID, getUserIdFromToken(GXTokenManager.USER_TOKEN, GXTokenManager.USER_ID));
        final GXPagination page = orderService.listOrSearchPage(param);
        return GXResultUtils.ok().putData(page);
    }

    @Override
    @PostMapping("/detail")
    @GXLoginAnnotation
    public GXResultUtils detail(@RequestBody Dict param) {
        param.set(GXTokenManager.USER_ID, getUserIdFromToken(GXTokenManager.USER_TOKEN, GXTokenManager.USER_ID));
        return GXResultUtils.ok().putData(orderService.detail(param));
    }

    @PostMapping("/cancel-order-status")
    @GXLoginAnnotation
    public GXResultUtils cancelOrderStatus(@RequestBody Dict param) {
        final boolean b = orderService.cancelOrderStatus(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }
}
