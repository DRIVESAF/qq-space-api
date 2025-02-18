package io.drivesaf.qq.space.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.drivesaf.qq.space.common.exception.ServerException;
import io.drivesaf.qq.space.mapper.CommentMapper;
import io.drivesaf.qq.space.mapper.ShuoshuoMapper;
import io.drivesaf.qq.space.mapper.ShuoshuoCommentRelationMapper;
import io.drivesaf.qq.space.model.entity.Comment;
import io.drivesaf.qq.space.model.entity.ShuoshuoCommentRelation;
import io.drivesaf.qq.space.model.vo.CommentVO;
import io.drivesaf.qq.space.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;


    @Autowired
    private ShuoshuoCommentRelationMapper shuoshuoCommentRelationMapper;

    @Override
    @Transactional
    public Comment addComment(Integer shuoshuoId, Integer commentAuthor, String commentContent, String image) {
        // 插入评论
        Comment comment = new Comment();
        comment.setCommentAuthor(commentAuthor);
        comment.setCommentContent(commentContent);
        comment.setImage(image);
        comment.setCreateTime(LocalDateTime.now());
        comment.setDeleteFlag(0);

        commentMapper.insert(comment);

        ShuoshuoCommentRelation relation = new ShuoshuoCommentRelation();
        relation.setShuoshuoId(shuoshuoId);
        relation.setCommentId(comment.getPkId());
        shuoshuoCommentRelationMapper.insert(relation);

        return comment;
    }

    @Override
    public List<CommentVO> getCommentsByShuoshuoId(Integer shuoshuoId) {
        return commentMapper.getCommentsByShuoshuoId(shuoshuoId);
    }

    @Override
    public void deleteComment(Integer commentId) {
        int rowsUpdated = baseMapper.updateDeleteFlag(commentId);
        if (rowsUpdated == 0) {
            throw new ServerException("未找到该说说或已删除");
        }
    }
}