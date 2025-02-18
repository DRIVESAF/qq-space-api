package io.drivesaf.qq.space.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "相册 DTO")
public class PhotoAlbumDTO {

    @Schema(description = "相册主键")
    private Integer photoAlbumId;

    @Schema(description = "相册名称")
    private String albumName;

    @Schema(description = "相册描述")
    private String albumDesc;

    @Schema(description = "可见范围：1-公开，2-部分好友可见，3-仅自己可见")
    private Integer visibleScope;

    @Schema(description = "相册封面图 URL")
    private String coverUrl;
}
