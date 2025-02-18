package io.drivesaf.qq.space.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "用户登录 VO")
public class UserLoginVO implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 8212240698099812005L;
    
    @Schema(description = "用户ID")
    private Integer pkId;
    
    @Schema(description = "手机号")
    private String phone;
    
    @Schema(description = "QQ OpenId")
    private String qqOpenId;
    
    @Schema(description = "令牌")
    private String accessToken;
}
