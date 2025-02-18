package io.drivesaf.qq.space.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "修改说说 DTO")
public class ShuoshuoDTO {

    @Schema(description = "说说主键")
    private Integer pkId;

    @Schema(description = "说说内容")
    private String content;

    @Schema(description = "可见范围：1-公开，2-部分好友可见，3-仅自己可见")
    private Integer visibleScope;

    @Schema(description = "部分好友可见时的好友列表")
    private List<Integer> visibleUser;
}
