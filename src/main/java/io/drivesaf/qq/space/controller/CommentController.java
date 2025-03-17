package io.drivesaf.qq.space.controller;

import io.drivesaf.qq.space.common.cache.RequestContext;
import io.drivesaf.qq.space.common.result.Result;
import io.drivesaf.qq.space.model.entity.Comment;
import io.drivesaf.qq.space.model.vo.CommentVO;
import io.drivesaf.qq.space.service.CommentService;
import io.drivesaf.qq.space.service.CommonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/comment")
@AllArgsConstructor
@Tag(name = "评论接口")
public class CommentController {

    private final CommentService commentService;
    private final CommonService commonService;

    @PostMapping("/publish/{shuoshuoId}")
    @Operation(summary = "发布评论")
    public Result<Comment> publishComment(@PathVariable Integer shuoshuoId,
                                          @RequestParam String commentContent,
                                          @RequestParam(required = false) MultipartFile image) {
        Integer commentAuthor = RequestContext.getUserId();
        String imageUrl = null;
        if (image != null) {
            imageUrl = commonService.upload(image);
        }
        Comment comment = commentService.addComment(shuoshuoId, commentAuthor, commentContent, imageUrl);
        return Result.ok(comment);
    }


    @GetMapping("/list/{shuoshuoId}")
    @Operation(summary = "查询评论列表")
    public Result<List<CommentVO>> getComments(@PathVariable Integer shuoshuoId) {
        List<CommentVO> comments = commentService.getCommentsByShuoshuoId(shuoshuoId);
        return Result.ok(comments);
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "删除评论")
    public Result<?> deleteShuoShuo(@PathVariable Integer id) {
        commentService.deleteComment(id);
        return Result.ok();
    }
}