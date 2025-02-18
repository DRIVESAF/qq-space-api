package io.drivesaf.qq.space.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_shuoshuo_comment")
public class Comment {
    @TableId(value = "pk_id", type = IdType.AUTO)
    private Integer pkId;

    private Integer commentAuthor;

    private String commentContent;

    private String image;

    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
