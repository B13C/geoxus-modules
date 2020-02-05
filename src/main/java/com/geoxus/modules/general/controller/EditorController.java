package com.geoxus.modules.general.controller;

import com.geoxus.core.ueditor.GXActionEnter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 百度编辑器
 */
@RestController
@RequestMapping("/generate/editor")
@Slf4j
public class EditorController {
    /**
     * 百度编辑器
     */
    @RequestMapping(value = "/editor", method = {RequestMethod.GET, RequestMethod.POST})
    public String server(HttpServletRequest request) {
        try {
            return new GXActionEnter(request).exec();
        } catch (JSONException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
