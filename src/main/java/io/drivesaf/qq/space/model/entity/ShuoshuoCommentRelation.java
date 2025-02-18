package io.drivesaf.qq.space.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_shuoshuo_comment_relation")
public class ShuoshuoCommentRelation {
    @TableId(value = "pk_id", type = IdType.AUTO)
    private Integer pkId;

    private Integer shuoshuoId;

    private Integer commentId;
}