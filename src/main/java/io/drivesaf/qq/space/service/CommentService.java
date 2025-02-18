package io.drivesaf.qq.space.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.drivesaf.qq.space.model.entity.Comment;
import io.drivesaf.qq.space.model.vo.CommentVO;

import java.util.List;

public interface CommentService extends IService<Comment> {
    Comment addComment(Integer shuoshuoId, Integer commentAuthor, String commentContent, String image);

    List<CommentVO> getCommentsByShuoshuoId(Integer shuoshuoId);

    /**
     * 删除说说
     *
     * @param commentId 评论ID
     */
    void deleteComment(Integer commentId);

}
