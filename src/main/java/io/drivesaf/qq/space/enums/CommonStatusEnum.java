package io.drivesaf.qq.space.enums;

import lombok.Getter;

@Getter
public enum CommonStatusEnum {
    NO(0, "否"),
    YES(1, "是");

    private final Integer status;
    private final String desc;

    CommonStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
