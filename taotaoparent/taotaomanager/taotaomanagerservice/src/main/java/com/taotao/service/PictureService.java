package com.taotao.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @ Create by ostreamBaba on 18-12-9
 * @ 描述
 */
public interface PictureService {

    Map<String, Object> uploadPicture(MultipartFile uploadFile);

}
