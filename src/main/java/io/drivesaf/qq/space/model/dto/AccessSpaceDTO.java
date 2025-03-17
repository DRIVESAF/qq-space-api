package io.drivesaf.qq.space.model.dto;

import lombok.Data;

@Data
public class AccessSpaceDTO {
    private Integer spaceId;
    private String userAnswer; // 用户回答的问题答案（如果有问题）
}