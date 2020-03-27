package com.geoxus.modules.general.controller;

import com.geoxus.core.ueditor.GXActionEnter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 百度编辑器
 */
@RestController
@RequestMapping("/general/editor")
@Slf4j
public class EditorController {
    /**
     * 百度编辑器
     */
    @PostMapping(value = "/editor")
    public String server(HttpServletRequest request) {
        try {
            return new GXActionEnter(request).exec();
        } catch (JSONException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
