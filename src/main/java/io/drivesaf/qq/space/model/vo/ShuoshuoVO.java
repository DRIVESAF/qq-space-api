package io.drivesaf.qq.space.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: DRIVESAF
 * @createTime: 2025/01/26
 * @description: 说说 VO 类
 **/
@Data
@Schema(description = "说说视图对象")
public class ShuoshuoVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -7118493289943684477L;

    @Schema(description = "说说主键ID")
    private Integer pkId;

    @Schema(description = "说说内容")
    private String content;

    @Schema(description = "昵称")
    private String author;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "点赞数")
    private Integer likeNum;

    @Schema(description = "图片列表")
    private List<String> images;

    @Schema(description = "是否置顶（0-否，1-是）")
    private Integer isTop;

    @Schema(description = "转发数")
    private Integer transpondNum;

    @Schema(description = "发布时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
