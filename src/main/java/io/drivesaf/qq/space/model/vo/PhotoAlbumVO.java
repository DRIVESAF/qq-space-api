package io.drivesaf.qq.space.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: DRIVESAF
 * @createTime: 2025/02/06
 * @description: 相册 VO 类
 **/
@Data
@Schema(description = "相册视图对象")
public class PhotoAlbumVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1234567890123456789L;

    @Schema(description = "相册主键ID")
    private Integer photoAlbumId;

    @Schema(description = "用户ID，标识相册所属用户")
    private Integer userId;

    @Schema(description = "相册名称")
    private String albumName;

    @Schema(description = "可见范围")
    private Integer visibleScope;

    @Schema(description = "相册描述")
    private String albumDesc;

    @Schema(description = "相册封面图")
    private String coverUrl;


    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
