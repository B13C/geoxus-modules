package com.geoxus.modules.general.controller;

import cn.hutool.core.lang.Dict;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.modules.general.config.AliYunVoDConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/general/ali-vod")
@Slf4j
public class AliYunVoDController {
    @Autowired
    private AliYunVoDConfig aliYunVoDConfig;

    /**
     * 获取上传凭证
     *
     * @param param
     * @return
     */
    @PostMapping("/upload-video")
    public GXResultUtils uploadVideo(Dict param) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-shanghai", aliYunVoDConfig.getAccessKey(), aliYunVoDConfig.getAccessKeySecret());
        DefaultAcsClient client = new DefaultAcsClient(profile);
        CreateUploadVideoRequest vodRequest = new CreateUploadVideoRequest();
        vodRequest.setTitle(param.getStr("title"));
        vodRequest.setFileName(param.getStr("file_name"));
        vodRequest.setTemplateGroupId(aliYunVoDConfig.getTemplateGroupId());
        try {
            CreateUploadVideoResponse response = client.getAcsResponse(vodRequest);
            Map<String, String> result = new HashMap<>();
            result.put("videoId", response.getVideoId());
            result.put("uploadAddress", response.getUploadAddress());
            result.put("uploadAuth", response.getUploadAuth());
            result.put("requestId", response.getRequestId());
            return GXResultUtils.ok().putData(result);
        } catch (ClientException e) {
            log.error(e.getMessage(), e);
            return GXResultUtils.error("获取上传凭证失败");
        }
    }

    /**
     * 获取播放地址
     *
     * @param param
     * @return
     */
    @PostMapping("/play-video")
    public GXResultUtils playVideo(Dict param) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-shanghai", aliYunVoDConfig.getAccessKey(), aliYunVoDConfig.getAccessKeySecret());
        DefaultAcsClient client = new DefaultAcsClient(profile);
        GetPlayInfoRequest playRequest = new GetPlayInfoRequest();
        playRequest.setVideoId(param.getStr("video_id"));
        try {
            GetPlayInfoResponse response = client.getAcsResponse(playRequest);
            return GXResultUtils.ok().putData(response);
        } catch (ClientException e) {
            log.error(e.getMessage(), e);
            return GXResultUtils.error("获取播放地址失败");
        }
    }
}
