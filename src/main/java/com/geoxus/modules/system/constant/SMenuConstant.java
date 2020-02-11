package com.geoxus.modules.system.constant;

import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;

public class SMenuConstant {
    @GXFieldCommentAnnotation(zh = "主键ID")
    public static final String PRIMARY_KEY = "menu_id";

    /**
     * 菜单类型
     */
    public enum MenuType {
        @GXFieldCommentAnnotation(zh = "目录")
        CATALOG(0),

        @GXFieldCommentAnnotation(zh = "菜单")
        MENU(1),

        @GXFieldCommentAnnotation(zh = "按钮")
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
