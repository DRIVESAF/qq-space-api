package io.drivesaf.qq.space.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("t_photo_album")
public class PhotoAlbum {

    @TableId(value = "photoAlbum_id", type = IdType.AUTO)
    private Integer photoAlbumId; // 相册的唯一标识

    private Integer userId; // 用户ID，标识相册所属用户

    private String albumName; // 相册名称

    private String albumDesc; // 相册描述

    @TableField("visible_scope")
    private Integer visibleScope; // 可见范围：1-公开，2-部分好友，3-仅自己

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> visibleUserIds;

    private String coverUrl; // 相册封面图

    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag; // 删除标识（0-未删除，1-删除）

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime; // 创建时间

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime; // 更新时间
}
