package io.drivesaf.qq.space.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_user")
public class User {
    @TableId(value = "pk_id", type = IdType.AUTO)
    private Integer pkId;

    private String phone;

    @TableField("qq_open_id")
    private String qqOpenId;

    private String nickname;

    private String avatar;

    private Integer gender;

    private String birthday;

    private String remark;

    private String password;

    private Integer enabled;

    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
