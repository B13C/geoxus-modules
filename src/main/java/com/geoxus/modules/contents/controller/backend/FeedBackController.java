package com.geoxus.modules.contents.controller.backend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXRequestBodyToEntityAnnotation;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.modules.contents.constant.FeedBackConstants;
import com.geoxus.modules.contents.entity.FeedBackEntity;
import com.geoxus.modules.contents.service.FeedBackService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
    @PostMapping("/update")
    @RequiresPermissions("feedback-update")
    public GXResultUtils update(@Valid @GXRequestBodyToEntityAnnotation FeedBackEntity target) {
        final long i = feedBackService.update(target, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set(FeedBackConstants.PRIMARY_KEY, i));
    }

    @Override
    @PostMapping("/delete")
    @RequiresPermissions("feedback-delete")
    public GXResultUtils delete(@RequestBody Dict param) {
        final boolean b = feedBackService.delete(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @Override
    @PostMapping("/list-or-search")
    @RequiresPermissions("feedback-list-or-search")
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(feedBackService.listOrSearchPage(param));
    }

    @Override
    @PostMapping("/detail")
    @RequiresPermissions("feedback-detail")
    public GXResultUtils detail(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(feedBackService.detail(param));
    }

    @PostMapping("/reply")
    @RequiresPermissions("feedback-reply")
    public GXResultUtils reply(@RequestBody Dict param) {
        final boolean replay = feedBackService.replay(param);
        return GXResultUtils.ok().addKeyValue("status", replay);
    }
}
