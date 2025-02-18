package io.drivesaf.qq.space.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_action")
public class UserAction {
 @TableId(value = "pk_id", type = IdType.AUTO)
 private Integer pkId;
 
 private Integer userId;
 
 private Integer shuoshuoId;

 private Integer type;
 @TableField(value = "delete_flag", fill = FieldFill.INSERT)
 
 @TableLogic
 private Integer deleteFlag = 0;
 
 @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
 private LocalDateTime updateTime;
 
 @TableField(value = "create_time", fill = FieldFill.INSERT)
 private LocalDateTime createTime;
}