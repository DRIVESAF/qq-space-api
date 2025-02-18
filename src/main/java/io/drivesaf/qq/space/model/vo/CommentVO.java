package io.drivesaf.qq.space.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author: DRIVESAF
 * @createTime: 2025/02/18
 * @description: 评论视图对象
 **/
@Data
@Schema(description = "评论视图对象")
public class CommentVO implements Serializable {

    @Schema(description = "评论主键ID")
    private Integer pkId;

    @Schema(description = "说说ID")
    private Integer shuoshuoId;

    @Schema(description = "评论作者ID")
    private Integer commentAuthor;

    @Schema(description = "评论作者的昵称")
    private String commentAuthorNickname;

    @Schema(description = "评论内容")
    private String commentContent;

    @Schema(description = "评论时间")
    private LocalDateTime createTime;

    @Schema(description = "评论图片")
    private String image;

    @Schema(description = "说说发布人ID")
    private Integer shuoshuoAuthor;

    @Schema(description = "说说发布人昵称")
    private String shuoshuoAuthorNickname;

}
