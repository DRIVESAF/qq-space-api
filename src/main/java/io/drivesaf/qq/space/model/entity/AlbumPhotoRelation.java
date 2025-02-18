package io.drivesaf.qq.space.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_album_photo_relation")
public class AlbumPhotoRelation {

    @TableId(value = "relation_id", type = IdType.AUTO)
    private Integer relationId; // 关系唯一标识

    private Integer photoAlbumId; // 相册ID

    private Integer photoId; // 图片ID

    private LocalDateTime createTime; // 创建时间
}
