package io.drivesaf.qq.space.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PhotoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer photoId; // 图片的唯一标识

    private Integer photoAlbumId; // 所属相册ID

    private Integer userId; // 上传图片的用户ID

    private String photoUrl; // 图片地址

    private String photoDesc; // 图片描述

    private LocalDateTime uploadTime; // 图片上传时间

    private String albumName; // 所属相册名称（通过联表查询获得）
}