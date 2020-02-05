package com.geoxus.modules.general.service.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.core.common.util.GXChineseToPinYinUtils;
import com.geoxus.modules.general.entity.SRegionEntity;
import com.geoxus.modules.general.mapper.SRegionMapper;
import com.geoxus.modules.general.service.SRegionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SRegionServiceImpl extends ServiceImpl<SRegionMapper, SRegionEntity> implements SRegionService {
    private static final String NAME_TAG = "name";

    private static final String PARENT_TAG = "parent_id";

    private static final String TYPE_TAG = "type";

    @Override
    public List<SRegionEntity> getRegionTree() {
        List<SRegionEntity> list = list(new QueryWrapper<>());
        //把根分类区分出来
        List<SRegionEntity> rootList = list.stream().filter(root -> root.getParentId() == 100000).collect(Collectors.toList());
        //把非根分类区分出来
        List<SRegionEntity> subList = list.stream().filter(sub -> sub.getParentId() != 100000).collect(Collectors.toList());
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
    private void buildSubs(SRegionEntity parent, List<SRegionEntity> subs) {
        List<SRegionEntity> children = subs.stream().filter(sub -> sub.getParentId() == parent.getId()).collect(Collectors.toList());
        parent.setChildren(children);
        if (!CollectionUtils.isEmpty(children)) {//有子分类的情况
            children.forEach(child -> buildSubs(child, subs));//再次递归构建
        }
    }

    @Override
    public List<SRegionEntity> getRegion(Dict param) {
        QueryWrapper<SRegionEntity> queryWrapper = new QueryWrapper<>();
        final String name = param.getStr(NAME_TAG);
        final Integer parentId = param.getInt(PARENT_TAG);
        final Short type = param.getShort(TYPE_TAG);
        if (!StringUtils.isBlank(name)) {
            queryWrapper.like("name", name);
        } else {
            if (null != parentId) {
                queryWrapper.eq("parent_id", parentId);
            }
            queryWrapper.eq("type", type == null ? 1 : type);
        }
        return list(queryWrapper);
    }

    @Override
    public boolean convertNameToPinYin() {
        final List<SRegionEntity> list = list(new QueryWrapper<>());
        for (SRegionEntity entity : list) {
            final String firstLetter = GXChineseToPinYinUtils.getFirstLetter(entity.getName());
            final String fullLetter = GXChineseToPinYinUtils.getFullLetter(entity.getName());
            entity.setFirstLetter(firstLetter);
            entity.setPinyin(fullLetter);
            updateById(entity);
        }
        return false;
    }
}
