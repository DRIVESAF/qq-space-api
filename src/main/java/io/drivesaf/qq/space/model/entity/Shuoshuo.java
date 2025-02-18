package io.drivesaf.qq.space.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: DRIVESAF
 * @createTime: 2025/01/22 21:40
 * @description:
 **/
@Data
@TableName(value = "t_shuoshuo", autoResultMap = true)
public class Shuoshuo implements Serializable {
    @Serial
    private static final long serialVersionUID = -7118493289943684477L;

    @TableId(value = "pk_id", type = IdType.AUTO)
    private Integer pkId;

    private String content;

    private Integer author;

    @TableField(exist = false)
    private String nickname;

    @TableField(exist = false)
    private String avatar;

    private Integer likeNum;

    private Integer isTop;

    private Integer transpondNum;

    private Integer visibleScope;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> visibleUser;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> images;

    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
