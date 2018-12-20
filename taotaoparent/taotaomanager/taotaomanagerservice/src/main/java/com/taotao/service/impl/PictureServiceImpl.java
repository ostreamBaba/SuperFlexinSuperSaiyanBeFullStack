package com.taotao.service.impl;

import com.taotao.common.utils.FtpUtils;
import com.taotao.common.utils.IDUtils;
import com.taotao.service.PictureService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ Create by ostreamBaba on 18-12-9
 * @ 图片上传服务
 */

@Service
public class PictureServiceImpl implements PictureService{

    @Value("${FTP_ADDRESS}")
    private String FTP_ADDRESS;

    @Value("${FTP_PORT}")
    private Integer FTP_PORT;

    @Value("${FTP_USERNAME}")
    private String FTP_USERNAME;

    @Value("${FTP_PASSWORD}")
    private String FTP_PASSWORD;

    @Value("${FTP_BASE_PATH}")
    private String FTP_BASE_PATH;

    @Value("${IMAGE_BASE_URL}")
    private String IMAGE_BASE_URL;


    @Override
    public Map<String, Object> uploadPicture(MultipartFile uploadFile) {
        Map<String, Object> resultMap = new HashMap<>();
        //生成一个新的文件名
        //取原文件名
        String oldName = uploadFile.getOriginalFilename();
        //生成新的文件名 UUID
        String newName = IDUtils.genImageName();
        newName += oldName.substring(oldName.indexOf("."));
        //图片上传
        try {
            String imagePath = new DateTime().toString("/yyyy/MM/dd");
            boolean result = FtpUtils.uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD,
                    FTP_BASE_PATH, imagePath, newName, uploadFile.getInputStream());
            if(!result){
                resultMap.put("error", 1);
                resultMap.put("message", "文件上传失败");
            }else {
                resultMap.put("error", 0);
                resultMap.put("url", IMAGE_BASE_URL+imagePath+"/"+newName);
            }

        } catch (IOException e) {
            e.printStackTrace();
            resultMap.put("error", 1);
            resultMap.put("message", "文件上传发生异常");
        }
        //返回结果
        return resultMap;
    }
}
