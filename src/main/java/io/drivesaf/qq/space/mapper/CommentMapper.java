package io.drivesaf.qq.space.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.drivesaf.qq.space.model.entity.Comment;
import io.drivesaf.qq.space.model.vo.CommentVO;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CommentMapper extends BaseMapper<Comment> {

    @Select("SELECT c.pk_id AS pkId, " +
            "r.shuoshuo_id AS shuoshuoId, " +
            "c.comment_author AS commentAuthor, " +
            "u.nickname AS commentAuthorNickname, " +
            "u.avatar AS commentAuthorAvatar, " +
            "c.comment_content AS commentContent, " +
            "c.create_time AS createTime, " +
            "c.image AS image, " +
            "s.author AS shuoshuoAuthor, " +
            "su.nickname AS shuoshuoAuthorNickname " +
            "FROM t_shuoshuo_comment c " +
            "JOIN t_shuoshuo_comment_relation r ON c.pk_id = r.comment_id " +
            "JOIN t_shuoshuo s ON r.shuoshuo_id = s.pk_id " +
            "JOIN t_user u ON c.comment_author = u.pk_id " +
            "JOIN t_user su ON s.author = su.pk_id " +
            "WHERE r.shuoshuo_id = #{shuoshuoId} AND c.delete_flag = 0")
    List<CommentVO> getCommentsByShuoshuoId(Integer shuoshuoId);

    @Update("UPDATE t_shuoshuo_comment SET delete_flag = 1 WHERE pk_id = #{pk_id} AND delete_flag = 0")
    int updateDeleteFlag(Integer commentId);
}
