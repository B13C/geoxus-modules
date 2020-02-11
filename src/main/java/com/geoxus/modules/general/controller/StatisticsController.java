package com.geoxus.modules.general.controller;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXValidateExtDataAnnotation;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.common.validator.group.GXAddGroup;
import com.geoxus.modules.general.service.StatisticsService;
import com.geoxus.modules.goods.listener.GoodsListener;
import com.geoxus.modules.goods.service.GoodsService;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/general/statistics")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsListener goodsListener;

    /**
     * 首页数据统计
     * 根据业务的不同去创建service的实现类
     * 只有Service实现后才能调用,否则会报错
     *
     * @return
     */
    @PostMapping("/get-data-statistics")
    public GXResultUtils getDataStatistics() {
        List<Dict> list = statisticsService.getDataStatistics();
        return GXResultUtils.ok().putData(list);
    }

    @Data
    public static class BtnData {
        @NotEmpty
        @Length(min = 20, max = 200)
        private String name;
        private int age;
        private String ext;
        private List<Map<String, Object>> address = new ArrayList<>();
    }

    @Data
    public static class PersonData {
        @GXValidateExtDataAnnotation(tableName = "goods")
        private String ext;
        private String name;
        private int age;
        @Valid
        private BookData bookData;
    }

    @Data
    public static class BookData {
        @Length(max = 200, min = 15)
        private String bookName;
        @Min(56)
        private double price;
    }

    @Data
    public static class CompositeData {
        @NotEmpty
        @Length(min = 10, max = 20, groups = {GXAddGroup.class})
        private String name;
        @Max(100)
        @Min(10)
        private int age;
    }
}
