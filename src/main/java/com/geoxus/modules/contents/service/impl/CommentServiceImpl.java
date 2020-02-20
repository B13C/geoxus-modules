package com.geoxus.modules.contents.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.vo.GXBusinessStatusCode;
import com.geoxus.core.common.vo.response.GXPagination;
import com.geoxus.core.framework.service.GXCoreModelService;
import com.geoxus.modules.contents.constant.CommentConstants;
import com.geoxus.modules.contents.entity.CommentEntity;
import com.geoxus.modules.contents.mapper.CommentMapper;
import com.geoxus.modules.contents.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, CommentEntity> implements CommentService {
    @Autowired
    private GXCoreModelService gxCoreModelService;

    @Override
    public long create(CommentEntity target, Dict param) {
        target.setPath(getPath(target.getParentId()));
        target.setStatus(GXBusinessStatusCode.NORMAL.getCode());
        target.setModelType(gxCoreModelService.getModelTypeByModelId(target.getTargetCoreModelId(), ""));
        save(target);
        handleMedia(target, target.getCommentId(), Dict.create());
        return target.getCommentId();
    }

    @Override
    public long update(CommentEntity target, Dict param) {
        target.setPath(getPath(target.getParentId()));
        target.setStatus(GXBusinessStatusCode.NORMAL.getCode());
        target.setModelType(gxCoreModelService.getModelTypeByModelId(target.getTargetCoreModelId(), ""));
        updateById(target);
        handleMedia(target, target.getCommentId(), Dict.create());
        return target.getCommentId();
    }

    @Override
    public boolean delete(Dict param) {
        final List<Integer> ids = Convert.convert(new TypeReference<List<Integer>>() {
        }, param.getObj(CommentConstants.PRIMARY_KEY));
        if (null == ids) {
            return false;
        }
        boolean b = true;
        if (!ids.isEmpty()) {
            final ArrayList<CommentEntity> commentEntities = new ArrayList<>();
            for (int id : ids) {
                CommentEntity entity = getById(id);
                entity.setStatus(GXBusinessStatusCode.DELETED.getCode());
                commentEntities.add(entity);
            }
            b = updateBatchById(commentEntities);
        }
        return b;
    }

    @Override
    public GXPagination listOrSearchPage(Dict param) {
        return generatePage(param);
    }

    @Override
    public Dict detail(Dict param) {
        return baseMapper.detail(param);
    }

    /**
     * 获取树状结构
     *
     * @return
     */
    public List<Dict> getTree(Dict param) {
        final IPage<Dict> page = new Page<>(1, 10000);
        final List<Dict> list = baseMapper.listOrSearchPage(page, param);
        //把根分类区分出来
        List<Dict> rootList = list.stream().filter(root -> root.getInt("parent_id") == 0).collect(Collectors.toList());
        //把非根分类区分出来
        List<Dict> subList = list.stream().filter(sub -> sub.getInt("parent_id") != 0).collect(Collectors.toList());
        //递归构建结构化的分类信息
        rootList.forEach(root -> buildSubs(root, subList));
        return rootList;
    }

    /**
     * 递归构建
     *
     * @param parent 父级ID
     * @param subs   子集数据
     */
    private void buildSubs(Dict parent, List<Dict> subs) {
        List<Dict> children = subs.stream().filter(sub -> sub.getInt("parent_id") == (int) parent.getInt(CommentConstants.PRIMARY_KEY)).collect(Collectors.toList());
        parent.set("children", children);
        if (!CollectionUtils.isEmpty(children)) {//有子分类的情况
            children.forEach(child -> buildSubs(child, subs));//再次递归构建
        }
    }

    private String getPath(int parentId) {
        final CommentEntity entity = getById(parentId);
        if (null != entity) {
            return entity.getPath() + "," + parentId;
        }
        return "";
    }
}
