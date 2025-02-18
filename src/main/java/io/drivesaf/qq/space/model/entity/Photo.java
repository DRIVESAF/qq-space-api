package io.drivesaf.qq.space.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_photo")
public class Photo {

    @TableId(value = "photo_id", type = IdType.AUTO)
    private Integer photoId;

    private Integer photoAlbumId;

    private Integer userId;

    private String photoUrl;

    private String photoDesc;

    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag;

    @TableField(value = "upload_time", fill = FieldFill.INSERT)
    private LocalDateTime uploadTime;
}
