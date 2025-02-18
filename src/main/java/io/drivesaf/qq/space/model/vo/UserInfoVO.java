package io.drivesaf.qq.space.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author: DRIVESAF
 * @createTime: 2025/01/21 23:00
 * @description:
 **/
@Data
@Schema(description = "用户信息")
public class UserInfoVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -45095106764580159L;

    @Schema(description = "主键")
    private Integer pkId;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "QQ openId")
    private String qqOpenId;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "生日")
    private String birthday;


}

