package com.geoxus.modules.contents.controller.backend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXRequestBodyToBeanAnnotation;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.modules.contents.entity.FeedBackEntity;
import com.geoxus.modules.contents.service.FeedBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController("backendFeedBackController")
@RequestMapping("/feedback/backend")
public class FeedBackController implements GXController<FeedBackEntity> {
    @Autowired
    private FeedBackService feedBackService;

    @Override
//    @PostMapping("/create")
    public GXResultUtils create(@Valid @GXRequestBodyToBeanAnnotation FeedBackEntity target) {
        final long i = feedBackService.create(target, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set("id", i));
    }

    @Override
    @PostMapping("/update")
    public GXResultUtils update(@Valid @GXRequestBodyToBeanAnnotation FeedBackEntity target) {
        final long i = feedBackService.update(target, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set("id", i));
    }

    @Override
    @PostMapping("/delete")
    public GXResultUtils delete(@RequestBody Dict param) {
        final boolean b = feedBackService.delete(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @Override
    @PostMapping("/list-or-search")
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(feedBackService.listOrSearch(param));
    }

    @Override
    @PostMapping("/detail")
    public GXResultUtils detail(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(feedBackService.detail(param));
    }

    @PostMapping("/reply-business")
    public GXResultUtils replyBusiness(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(feedBackService.replay(param));
    }
    @PostMapping("/reply-entrust")
    public GXResultUtils replyEntrust(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(feedBackService.replay(param));
    }
    @PostMapping("/reply-customer")
    public GXResultUtils replyCustomer(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(feedBackService.replay(param));
    }

    @PostMapping("/list-or-search-business")
    public GXResultUtils listOrSearchBusiness(@RequestBody Dict param) {
        param.set("account_type",2);
        return GXResultUtils.ok().putData(feedBackService.listOrSearch(param));
    }
    @PostMapping("/list-or-search-entrust")
    public GXResultUtils listOrSearchEntrust(@RequestBody Dict param) {
        param.set("account_type",3);
        return GXResultUtils.ok().putData(feedBackService.listOrSearch(param));
    }
    @PostMapping("/list-or-search-customer")
    public GXResultUtils listOrSearchCustomer(@RequestBody Dict param) {
        param.set("account_type",1);
        return GXResultUtils.ok().putData(feedBackService.listOrSearch(param));
    }

    @PostMapping("/detail-business")
    public GXResultUtils detailBusiness(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(feedBackService.detail(param));
    }

    @PostMapping("/detail-entrust")
    public GXResultUtils detailEntrust(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(feedBackService.detail(param));
    }

    @PostMapping("/detail-customer")
    public GXResultUtils detailCustomer(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(feedBackService.detail(param));
    }
}
