package io.drivesaf.qq.space.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import io.drivesaf.qq.space.common.cache.RedisCache;
import io.drivesaf.qq.space.common.cache.RedisKeys;
import io.drivesaf.qq.space.common.config.CloopenConfig;
import io.drivesaf.qq.space.common.config.OssConfig;
import io.drivesaf.qq.space.common.exception.ErrorCode;
import io.drivesaf.qq.space.common.exception.ServerException;
import io.drivesaf.qq.space.service.CommonService;
import io.drivesaf.qq.space.utils.CommonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

/**
 * @program: share-app-api
 * @description:
 * @author: ytq
 * @create: 2024-04-28 15:50
 **/
@Slf4j
@Service
@AllArgsConstructor
public class CommonServiceImpl implements CommonService {

    private final RedisCache redisCache;
    private final CloopenConfig cloopenConfig;
    private final OssConfig ossConfig;

    //允许上传文件（图片）的格式
    private static final String[] IMAGE_TYPE = new String[]{".bmp", ".jpg", ",jpeg", ".gif", "png"};

    @Override
    public void sendSms(String phone) {
        //  校验手机号合法性
        if (!CommonUtils.checkPhone(phone)) {
            throw new ServerException(ErrorCode.PARAMS_ERROR);
        }
        //  生成随机验证码
        int code = CommonUtils.generateCode();
        // redis缓存验证码
        redisCache.set(RedisKeys.getSmsKey(phone), code, 60);
        // 调用内部方法发送短信
        boolean result = cloopenSendSms(phone, code);
        if (result) {
            log.info("=========== 短信发送成功 ================");
        }
    }

    /**
     * cloopen 发送短信
     *
     * @param phone 电话
     * @param code  验证码
     * @return boolean
     */
    // private boolean cloopenSendSms(String phone, int code) {
    //     try {
    //         log.info(" ============= 创建短信发送通道中 ============= \nphone is {},code is {}", phone, code);
    //         String serverIp = cloopenConfig.getServerIp();
    //         // 请求端口
    //         String serverPort = cloopenConfig.getPort();
    //         // 主账号,登陆云通讯网站后,可在控制台首页看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN
    //         String accountSId = cloopenConfig.getAccountSId();
    //         String accountToken = cloopenConfig.getAccountToken();
    //         // 请使用管理控制台中已创建应用的APPID
    //         String appId = cloopenConfig.getAppId();
    //         CCPRestSmsSDK sdk = new CCPRestSmsSDK();
    //         sdk.init(serverIp, serverPort);
    //         sdk.setAccount(accountSId, accountToken);
    //         sdk.setAppId(appId);
    //         sdk.setBodyType(BodyType.Type_JSON);
    //         String templateId = cloopenConfig.getTemplateId();
    //         String[] datas = {String.valueOf(code), "1"};
    //         HashMap<String, Object> result = sdk.sendTemplateSMS(phone, templateId, datas, "1234", UUID.randomUUID().toString());
    //         if ("000000".equals(result.get("statusCode"))) {
    //             // 正常返回输出data包体信息（map）
    //             HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
    //             Set<String> keySet = data.keySet();
    //             for (String key : keySet) {
    //                 Object object = data.get(key);
    //                 log.info("{} = {}", key, object);
    //             }
    //         } else {
    //             // 异常返回输出错误码和错误信息
    //             log.error("错误码={} 错误信息= {}", result.get("statusCode"), result.get("statusMsg"));
    //             throw new ServerException(ErrorCode.CODE_SEND_FAIL);
    //         }
    //     } catch (Exception e) {
    //         throw new ServerException(ErrorCode.CODE_SEND_FAIL);
    //     }
    //     return true;
    // }
    private boolean cloopenSendSms(String phone, int code) {
        try {
            // 模拟发送短信：直接将验证码存入 Redis
            log.info("模拟发送短信到 {}，验证码是 {}，将验证码存入 Redis", phone, code);

            // 将验证码存入 Redis，使用 Redis 缓存
            redisCache.set(RedisKeys.getSmsKey(phone), code, 60); // 设置验证码有效期为 60 秒

            // 记录日志（可选）：这里可以记录存入 Redis 的操作
            log.info("验证码已存入 Redis，phone: {}, code: {}", phone, code);

        } catch (Exception e) {
            // 如果出现异常，抛出服务器异常
            log.error("存入 Redis 时出现异常：", e);
            throw new ServerException(ErrorCode.CODE_SEND_FAIL);
        }

        return true;  // 返回成功标志，表示处理完成
    }



    @Override
    public String upload(MultipartFile file) {
        String returnImgUrl;

        //校验图片格式
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), type)) {
                isLegal = true;
                break;
            }
        }
        if (!isLegal) {
            //如果图片格式不合法
            throw new ServerException("图片格式不正确");
        }
        //获取文件原名称
        String originalFilename = file.getOriginalFilename();
        //获取文件类型
        String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
        //新文件名称
        String newFileName = UUID.randomUUID().toString() + fileType;
        //构建日期路径, 例如：OSS目标文件夹/2024/04/31 文件名
        String filePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        //文件上传的路径地址
        String uploadUrl = filePath + "/" + newFileName;

        //获取文件输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * 现在阿里云OSS 默认图片上传ContentType是image/jpeg
         * 也就是说，获取图片链接后，图片是下载链接， 而并非在线浏览链接，
         * 因此，这里在上传的时候要解决ContentType的问题，将其改为image/jpg
         */
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType("image/jpg");

        //读取配置文件中的属性
        String endpoint = ossConfig.getEndpoint();
        String accessKeyId = ossConfig.getAccessKeyId();
        String accessKeySecret = ossConfig.getAccessKeySecret();
        String bucketName = ossConfig.getBucketName();
        //创建 OssClient
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        //文件上传至阿里云OSS
        ossClient.putObject(bucketName, uploadUrl, inputStream, meta);
        //获取文件上传后的图片返回地址
        returnImgUrl = "https://" + bucketName + "." + endpoint + "/" + uploadUrl;

        return returnImgUrl;
    }

}
