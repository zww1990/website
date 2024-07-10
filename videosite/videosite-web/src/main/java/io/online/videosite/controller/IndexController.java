package io.online.videosite.controller;

import io.online.videosite.annotation.CurrentUser;
import io.online.videosite.api.CategoryService;
import io.online.videosite.api.VideoService;
import io.online.videosite.constant.AuditStatus;
import io.online.videosite.domain.Category;
import io.online.videosite.domain.User;
import io.online.videosite.domain.Video;
import io.online.videosite.properties.VideoSiteAppProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.PathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主页控制器
 *
 * @author 张维维
 */
@RestController
@Slf4j
@AllArgsConstructor
public class IndexController {
    private final VideoService videoService;
    private final CategoryService categoryService;
    private final VideoSiteAppProperties appProps;

    /**
     * 跳转到主页
     */
    @GetMapping(path = "/home")
    public ResponseEntity<?> home(
            @CurrentUser User user,
            @RequestParam(required = false) Integer categoryId) {
        // 查询所有用户审核通过的视频
        List<Video> videos = this.videoService.query(categoryId, AuditStatus.PASSED);
        List<Category> categories = this.categoryService.query();
        log.info("index(): 用户 = {}, 视频数量 = {}, 类别数量 = {}, 类别主键 = {}",
                user, videos.size(), categories.size(), categoryId);
        Map<String, Object> mav = new HashMap<>();
        mav.put("videos", videos);
        mav.put("categories", categories);
        return ResponseEntity.ok(mav);
    }

    /**
     * 加载图片资源
     */
    @GetMapping(path = "${video-site-app.image-upload-folder}/{filename}")
    public ResponseEntity<PathResource> loadImage(@PathVariable String filename) {
        log.info("loadImage(): filename = {}", filename);
        return ResponseEntity.ok(new PathResource(Paths.get(this.appProps.getImageUploadFolder(), filename)));
    }

    /**
     * 加载视频资源
     */
    @GetMapping(path = "${video-site-app.video-upload-folder}/{filename}")
    public ResponseEntity<PathResource> loadVideo(@PathVariable String filename) {
        log.info("loadVideo(): filename = {}", filename);
        return ResponseEntity.ok(new PathResource(Paths.get(this.appProps.getVideoUploadFolder(), filename)));
    }
}
