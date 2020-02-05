package com.geoxus.modules.contents.controller.frontend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXLoginAnnotation;
import com.geoxus.core.common.annotation.GXLoginUserAnnotation;
import com.geoxus.core.common.annotation.GXRequestBodyToBeanAnnotation;
import com.geoxus.core.common.controller.GXController;
import com.geoxus.core.common.oauth.GXTokenManager;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.modules.contents.entity.CommentEntity;
import com.geoxus.modules.contents.service.CommentService;
import com.geoxus.core.framework.service.GXCoreMediaLibraryService;
import com.geoxus.modules.user.entity.UUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController("frontendCommentController")
@RequestMapping("/comment/frontend")
public class CommentController implements GXController<CommentEntity> {
    @Autowired
    private CommentService commentService;

    @Autowired
    private GXCoreMediaLibraryService coreMediaLibraryService;

    @Override
    @PostMapping("/create")
    @GXLoginAnnotation
    public GXResultUtils create(@Valid @GXRequestBodyToBeanAnnotation CommentEntity target) {
        final long userId = getUserIdFromToken(GXTokenManager.USER_TOKEN, GXTokenManager.USER_ID);
        target.setUserId(userId);
        final long i = commentService.create(target, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set("id", i));
    }

    @Override
    @PostMapping("/update")
    @GXLoginAnnotation
    public GXResultUtils update(@Valid @GXRequestBodyToBeanAnnotation CommentEntity target) {
        final long userId = getUserIdFromToken(GXTokenManager.USER_TOKEN, GXTokenManager.USER_ID);
        target.setUserId(userId);
        final long i = commentService.update(target, Dict.create());
        return GXResultUtils.ok().putData(Dict.create().set("id", i));
    }

    @Override
    @PostMapping("/delete")
    @GXLoginAnnotation
    public GXResultUtils delete(@RequestBody Dict param) {
        final boolean b = commentService.delete(param);
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }

    @Override
    @PostMapping("/list-or-search")
    @GXLoginAnnotation
    public GXResultUtils listOrSearch(@RequestBody Dict param) {
        final GXPagination pagination = commentService.listOrSearch(param);
        return GXResultUtils.ok().putData(pagination);
    }

    @Override
    @PostMapping("/detail")
    @GXLoginAnnotation
    public GXResultUtils detail(@RequestBody Dict param) {
        return GXResultUtils.ok().putData(commentService.detail(param));
    }

    @PostMapping("/get-tree")
    @GXLoginAnnotation
    public GXResultUtils getTree(@RequestBody Dict param) {
        final List<Dict> tree = commentService.getTree(param);
        return GXResultUtils.ok().putData(tree);
    }

    @PostMapping("/delete-media")
    @GXLoginAnnotation
    public GXResultUtils deleteMedia(@RequestBody Dict param, @GXLoginUserAnnotation UUserEntity uUserEntity) {
        final boolean b = coreMediaLibraryService.deleteByCondition(Dict.create().set("model_id", uUserEntity.getUserId()).set("id", param.getInt("id")));
        return GXResultUtils.ok().putData(Dict.create().set("status", b));
    }
}
