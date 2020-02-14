package com.geoxus.modules.general.controller;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.annotation.GXUploadFileLegalAnnotation;
import com.geoxus.core.common.config.UploadConfig;
import com.geoxus.core.common.exception.GXException;
import com.geoxus.core.common.util.GXBase64DecodedMultipartFileUtils;
import com.geoxus.core.common.util.GXHttpContextUtils;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.core.common.util.GXUploadUtils;
import com.geoxus.core.common.vo.GXProgressData;
import com.geoxus.core.common.vo.GXResultCode;
import com.geoxus.core.framework.entity.GXCoreMediaLibraryEntity;
import com.geoxus.core.framework.service.GXCoreMediaLibraryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/general/upload")
@Slf4j
public class UploadController {
    @Autowired
    private GXCoreMediaLibraryService mediaLibraryService;

    @Autowired
    private UploadConfig uploadConfig;

    /**
     * 单图片上传
     *
     * @param file
     * @return
     */
    @PostMapping("/single-upload")
    @GXUploadFileLegalAnnotation
    public GXResultUtils singleUpload(MultipartFile file) throws IOException {
        if (file.isEmpty() || null == FileTypeUtil.getType(file.getInputStream())) {
            return GXResultUtils.error(GXResultCode.FILE_ERROR);
        }
        GXCoreMediaLibraryEntity entity = mediaLibraryService.saveFileInfo(file, getParamFromRequest());
        Map<String, Object> map = new HashMap<>();
        map.put("mediaId", entity.getId());
        map.put("mediaName", entity.getFilePath());
        return GXResultUtils.ok().putData(map);
    }

    /**
     * Base64图片上传
     *
     * @param param 参数集合
     * @return ResultUtil
     */
    @PostMapping("/single-base64-upload")
    @GXUploadFileLegalAnnotation
    public GXResultUtils singleBase64Upload(@RequestBody Dict param) throws IOException {
        final GXBase64DecodedMultipartFileUtils file = new GXBase64DecodedMultipartFileUtils(param.getStr("file"));
        return singleUpload(file);
    }

    /**
     * 上传图片不保存数据库
     *
     * @param file 文件名字
     * @return
     */
    @PostMapping("/single-upload-without-save-db")
    @GXUploadFileLegalAnnotation
    public GXResultUtils singleUploadNotSave(MultipartFile file) throws IOException {
        if (file.isEmpty() || null == FileTypeUtil.getType(file.getInputStream())) {
            return GXResultUtils.error(GXResultCode.FILE_ERROR);
        }
        try {
            String fileName = GXUploadUtils.singleUpload(file, uploadConfig.getDepositPath().trim());
            return GXResultUtils.ok().putData(fileName);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return GXResultUtils.error("上传失败");
        }
    }

    @PostMapping("/multi-upload-file")
    @GXUploadFileLegalAnnotation
    public GXResultUtils multiUploadFile(MultipartFile... files) throws Exception {
        List<Dict> list = new ArrayList<>();
        final List<Dict> dicts = GXUploadUtils.multiUpload(files, uploadConfig.getDepositPath());
        for (Dict dict : dicts) {
            final int id = mediaLibraryService.save(dict);
            final Dict d = Dict.create().set("id", id).set("name", dict.getStr("file_name"));
            list.add(d);
        }
        return GXResultUtils.ok().putData(list);
    }

    @PostMapping("/download-file")
    @GXUploadFileLegalAnnotation
    public void downloadFile(@RequestBody Dict dict, HttpServletResponse response) {
        try {
            GXCoreMediaLibraryEntity entity = mediaLibraryService.getById(dict.getInt("id"));
            File file = new File(uploadConfig.getDepositPath() + File.separator + entity.getFileName());
            byte[] data = FileUtils.readFileToByteArray(file);
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=" + dict.getStr("name"));
            response.addHeader("Content-Length", "" + data.length);
            response.setContentType("application/octet-stream; charset=UTF-8");
            IOUtils.write(data, response.getOutputStream());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GXException("下载失败", e);
        }
    }

    /**
     * 获取实时上传进度
     *
     * @param request
     * @return int percent
     */
    @GetMapping("/get-percent")
    @GXUploadFileLegalAnnotation
    public GXResultUtils getUploadPercent(HttpServletRequest request) {
        HttpSession session = request.getSession();
        GXProgressData percent = session.getAttribute("progress") == null ? null : (GXProgressData) session.getAttribute("progress");
        return GXResultUtils.ok().putData(percent);
    }

    /**
     * 从请求中获取额外的数据
     *
     * @return
     */
    private Dict getParamFromRequest() {
        final String resourceType = GXHttpContextUtils.getHttpParam("resource_type", String.class);
        final String modelType = GXHttpContextUtils.getHttpParam("model_type", String.class);
        final String collectionName = GXHttpContextUtils.getHttpParam("collection_name", String.class);
        final Long modelId = GXHttpContextUtils.getHttpParam("model_id", Long.class);
        final Long coreModelId = GXHttpContextUtils.getHttpParam("core_model_id", Long.class);
        return Dict.create()
                .set("collection_name", collectionName)
                .set("model_id", modelId)
                .set("core_model_id", coreModelId)
                .set("resource_type", resourceType)
                .set("model_type", modelType);
    }
}
