package io.drivesaf.qq.space.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(value = "t_user_space", autoResultMap = true)
public class Space implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "space_id", type = IdType.AUTO)
    private Integer spaceId;

    private String spaceName;

    private Integer allowAccessScope;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> allowedUserIds;

    private Integer questionId;

    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "create_user_id")
    private Integer createUserId;
}