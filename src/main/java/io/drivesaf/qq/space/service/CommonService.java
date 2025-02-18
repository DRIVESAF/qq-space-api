package io.drivesaf.qq.space.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author: DRIVESAF
 * @createTime: 2024/04/28 18:01
 * @description:
 **/
public interface CommonService {

        /**
         * 发送短信
         *
         * @param phone 手机号
         */

        void sendSms(String phone);

        /**
         * 文件上传
         *
         * @param file 文件
         * @return 上传后的 URL
         */

        String upload(MultipartFile file);

}


