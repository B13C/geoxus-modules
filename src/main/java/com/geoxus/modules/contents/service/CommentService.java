package com.geoxus.modules.contents.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.service.GXBusinessService;
import com.geoxus.modules.contents.entity.CommentEntity;

import java.util.List;

public interface CommentService extends GXBusinessService<CommentEntity> {
    /**
     * 获取树状结构
     *
     * @param param
     * @return
     */
    List<Dict> getTree(Dict param);
}
