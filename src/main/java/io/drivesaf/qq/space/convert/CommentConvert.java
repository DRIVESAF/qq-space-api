package io.drivesaf.qq.space.convert;

import io.drivesaf.qq.space.model.entity.Comment;
import io.drivesaf.qq.space.model.vo.CommentVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author: DRIVESAF
 * @createTime: 2025/02/18
 * @description: Comment 和 CommentVO 的转换器
 **/
@Mapper
public interface CommentConvert {
    // 获取CommentConvert实例，由MapStruct自动生成实现类并提供实例
    CommentConvert INSTANCE = Mappers.getMapper(CommentConvert.class);

    // 将Comment对象转换为CommentVO对象
    CommentVO convert(Comment comment);

    // 将Comment对象列表转换为CommentVO对象列表
    List<CommentVO> convertList(List<Comment> comments);
}
