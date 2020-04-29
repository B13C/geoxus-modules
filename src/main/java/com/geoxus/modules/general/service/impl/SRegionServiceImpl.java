package com.geoxus.modules.general.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.modules.general.constant.SRegionConstants;
import com.geoxus.modules.general.entity.SRegionEntity;
import com.geoxus.modules.general.mapper.SRegionMapper;
import com.geoxus.modules.general.service.SRegionService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SRegionServiceImpl extends ServiceImpl<SRegionMapper, SRegionEntity> implements SRegionService {
    private static final String NAME_FIELD = "name";

    private static final String PARENT_ID_FIELD = "parent_id";

    private static final String TYPE_FIELD = "type";

    @Override
    @Cacheable(value = "region", key = "targetClass + methodName")
    public List<Dict> getRegionTree(Dict param) {
        List<Dict> list = baseMapper.listOrSearch(param);
        //把根分类区分出来
        List<Dict> rootList = list.stream().filter(root -> root.getInt(PARENT_ID_FIELD) == 100000).collect(Collectors.toList());
        //把非根分类区分出来
        List<Dict> subList = list.stream().filter(sub -> sub.getInt(PARENT_ID_FIELD) != 100000).collect(Collectors.toList());
        //递归构建结构化的分类信息
        rootList.forEach(root -> buildSubs(root, subList));
        return rootList;
    }

    @Override
    public Integer getIdByName(String name) {
        final Dict condition = Dict.create().set("name", name);
        return getSingleFieldValueByDB(SRegionEntity.class, SRegionConstants.PRIMARY_KEY, Integer.class, condition);
    }

    @Override
    public Dict getDataById(Integer id) {
        final Dict condition = Dict.create().set(SRegionConstants.PRIMARY_KEY, id);
        return getFieldValueBySQL(SRegionEntity.class, CollUtil.newHashSet("region_id", "parent_id", "type", "name"), condition, false);
    }

    @Override
    public String getNameById(Integer id) {
        final Dict condition = Dict.create().set(SRegionConstants.PRIMARY_KEY, id);
        return baseMapper.getNameByCondition(condition);
    }

    /**
     * 递归构建
     *
     * @param parent 父级ID
     * @param subs   子集数据
     */
    private void buildSubs(Dict parent, List<Dict> subs) {
        List<Dict> children = subs.stream().filter(sub -> sub.getInt(PARENT_ID_FIELD).equals(parent.getInt("region_id"))).collect(Collectors.toList());
        parent.set("children", children);
        //有子分类的情况
        if (!CollectionUtils.isEmpty(children)) {
            //再次递归构建
            children.forEach(child -> buildSubs(child, subs));
        }
    }
}
