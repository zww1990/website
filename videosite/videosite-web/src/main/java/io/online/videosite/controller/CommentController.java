package io.online.videosite.controller;

import io.online.videosite.annotation.CurrentUser;
import io.online.videosite.api.CommentService;
import io.online.videosite.api.VideoService;
import io.online.videosite.domain.Comment;
import io.online.videosite.domain.User;
import io.online.videosite.domain.Video;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 视频评论控制器
 *
 * @author 张维维
 */
@RestController
@RequestMapping(path = "/comment")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final VideoService videoService;

    /**
     * 处理添加评论
     */
    @PostMapping(path = "/add")
    public ResponseEntity<?> add(
            @RequestBody Comment comment,
            @CurrentUser User user) {
        if (comment.getVideoId() == null) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("视频id不能为空！");
        }
        if (!StringUtils.hasText(comment.getContent())) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("请输入评论内容！");
        }
        Video video = this.videoService.queryOne(comment.getVideoId(), FetchType.LAZY);
        // 如果视频不存在
        if (video == null) {
            throw new EntityNotFoundException("此视频不存在");
        }
        this.commentService.save(comment, user);
        return ResponseEntity.ok(this.commentService.queryByVideoId(comment.getVideoId()));
    }
}
