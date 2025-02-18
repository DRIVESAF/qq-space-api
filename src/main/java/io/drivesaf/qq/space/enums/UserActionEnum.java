package io.drivesaf.qq.space.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserActionEnum {
    /**
     * 收藏
     */
    COLLECT(0, "收藏"),
    /**
     * 发布
     */
    PUBLISH(1, "发布"),
    /**
     * 兑换（下载）
     */
    COMMENT(2, "评论"),
    /**
     * 点赞
     */
    LIKE(3, "点赞"),
    ;
    private final Integer code;
    private final String desc;

    public static UserActionEnum getByCode(Integer code) {
        for (UserActionEnum value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}