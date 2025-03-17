package io.drivesaf.qq.space.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "修改空间权限 DTO")
public class SpaceDTO {

    @Schema(description = "空间ID")
    private Integer spaceId;

    @Schema(description = "访问权限范围：1-公开，2-所有好友，3-部分好友，4-需回答问题正确")
    private Integer allowAccessScope;

    @Schema(description = "当 allowAccessScope 为 3 时，允许访问的好友 ID 列表")
    private List<Integer> allowedUserIds;

    @Schema(description = "当 allowAccessScope 为 4 时，关联的问题 ID")
    private Integer questionId;

    private String questionContent;
    private String correctAnswer;
}