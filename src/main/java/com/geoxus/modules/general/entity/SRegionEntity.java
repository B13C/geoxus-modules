package com.geoxus.modules.general.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.modules.general.constant.SRegionConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@TableName(SRegionConstants.TABLE_NAME)
@EqualsAndHashCode(callSuper = false)
public class SRegionEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
    private int regionId;

    /**
     * 父ID
     */
    private int parentId;

    /**
     * 等级
     */
    private int type;

    /**
     * 名称
     */
    private String name;

    /**
     * 拼音全称
     */
    private String pinyin;

    /**
     * 拼音首字母
     */
    private String firstLetter;

    @TableField(exist = false)
    private List<SRegionEntity> children;
}
