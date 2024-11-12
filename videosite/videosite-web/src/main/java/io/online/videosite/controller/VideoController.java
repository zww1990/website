package io.online.videosite.controller;

import io.online.videosite.security.CurrentUser;
import io.online.videosite.api.CommentService;
import io.online.videosite.api.VideoService;
import io.online.videosite.constant.AuditStatus;
import io.online.videosite.domain.Comment;
import io.online.videosite.domain.User;
import io.online.videosite.domain.Video;
import io.online.videosite.model.VideoModel;
import io.online.videosite.properties.VideoSiteAppProperties;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.PathContainer;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.pattern.PathPatternParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * 视频控制器
 *
 * @author 张维维
 */
@RestController
@RequestMapping(path = "/videohub")
@AllArgsConstructor
@Slf4j
public class VideoController {
    private final VideoService videoService;
    private final CommentService commentService;
    private final PathPatternParser pathPatternParser;
    private final VideoSiteAppProperties appProps;

    /**
     * 跳转到用户的视频列表页
     */
    @GetMapping(path = "/list")
    public ResponseEntity<?> list(@CurrentUser User user) {
        // 查询此用户所有的视频
        List<Video> videos = this.videoService.queryForUser(user);
        log.info("list(): 视频数量 = {}", videos.size());
        return ResponseEntity.ok(videos);
    }

    /**
     * 按关键字查询审核通过的视频
     */
    @GetMapping(path = "/search")
    public ResponseEntity<?> search(@RequestParam String keyword) {
        List<Video> videos = this.videoService.queryForKeyword(keyword);
        log.info("search(): 视频数量 = {}", videos.size());
        return ResponseEntity.ok(videos);
    }

    /**
     * 跳转到查看页面
     */
    @GetMapping(path = "/show")
    public ResponseEntity<?> show(@RequestParam Integer id) {
        Video video = this.videoService.queryOne(id, FetchType.EAGER);
        // 如果视频不存在
        if (video == null) {
            throw new EntityNotFoundException("此视频不存在");
        }
        List<Comment> comments = this.commentService.queryByVideoId(id);
        log.info("show(): 视频主键 = {}, 视频评论数量 = {}", id, comments.size());
        Map<String, Object> mav = new HashMap<>();
        mav.put("video", video);
        mav.put("comments", comments);
        return ResponseEntity.ok(mav);
    }

    /**
     * 增加播放量
     */
    @PutMapping(path = "/addhits")
    public Object addHits(
            @RequestParam Integer id,
            @CurrentUser User user) {
        Video video = this.videoService.queryOne(id, FetchType.LAZY);
        // 如果视频不存在
        if (video == null) {
            throw new EntityNotFoundException("此视频不存在");
        }
        this.videoService.addHits(id, user);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("更新成功！");
    }

    /**
     * 跳转到审核页面
     */
    @GetMapping(path = "/audit")
    public ResponseEntity<?> audit(@RequestParam Integer id) {
        Video video = this.videoService.queryOne(id, FetchType.EAGER);
        // 如果视频不存在
        if (video == null) {
            throw new EntityNotFoundException("此视频不存在");
        }
        return ResponseEntity.ok(video);
    }

    /**
     * 处理审核
     */
    @PutMapping(path = "/audit")
    public ResponseEntity<?> handleAudit(
            @RequestBody Video param,
            @CurrentUser User user) {
        if (param.getId() == null) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("视频id不能为空！");
        }
        Video video = this.videoService.queryOne(param.getId(), FetchType.LAZY);
        // 如果视频不存在
        if (video == null) {
            throw new EntityNotFoundException("此视频不存在");
        }
        // 只有视频状态为待审核，且提交的审核状态为通过 或 不通过，才进行处理
        if (video.getAuditStatus() == AuditStatus.PENDING &&
                (param.getAuditStatus() == AuditStatus.PASSED ||
                        param.getAuditStatus() == AuditStatus.UNPASSED)) {
            if (AuditStatus.UNPASSED == param.getAuditStatus()) {
                video.setAuditReason(param.getAuditReason());
            }
            video.setAuditStatus(param.getAuditStatus());
            this.videoService.audit(video, user);
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("审核成功！");
    }

    /**
     * 处理删除
     */
    @DeleteMapping(path = "/delete")
    public ResponseEntity<?> delete(
            @RequestParam Integer id,
            @CurrentUser User user) {
        Video video = this.videoService.queryOne(id, FetchType.LAZY);
        // 如果视频不存在
        if (video == null) {
            throw new EntityNotFoundException("此视频不存在");
        }
        // 如果视频不是自己创建的，不允许删除
        if (!video.getCreator().equals(user.getUsername())) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        }
        this.videoService.delete(video);
        return ResponseEntity.ok(this.videoService.queryForUser(user));
    }

    /**
     * 处理添加
     */
    @PostMapping(path = "/add")
    public ResponseEntity<?> handleAdd(
            @ModelAttribute VideoModel model,
            @CurrentUser User user) throws Exception {
        if (!StringUtils.hasText(model.getVideoName())) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("请输入视频名称！");
        }
        if (model.getCategoryId() == null) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("请选择视频类别！");
        }
        if (model.getVideoLogo() == null || model.getVideoLogo().isEmpty()) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("请上传视频封面！");
        }
        if (model.getVideoLink() == null || model.getVideoLink().isEmpty()) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("请上传视频文件！");
        }
        // 如果上传视频封面的格式不正确
        if (!this.isMatch(this.appProps.getImageMimePatterns(), model.getVideoLogo().getContentType())) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("上传视频封面的格式不正确，请重新上传！");
        }
        // 如果上传视频文件的格式不正确
        if (!this.isMatch(this.appProps.getVideoMimePatterns(), model.getVideoLink().getContentType())) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("上传视频文件的格式不正确，请重新上传！");
        }
        if (this.videoService.existsByVideoLinkMd5(model.getVideoLinkMd5())) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("此视频文件已存在，请重新上传！");
        }
        log.info("handleAdd(): VideoLogo = {}, VideoLink = {}",
                model.getVideoLogo().getOriginalFilename(), model.getVideoLink().getOriginalFilename());
        model.setVideoLogoPath(this.makeFileName(model.getVideoLogo().getOriginalFilename()));
        model.setVideoLinkPath(this.makeFileName(model.getVideoLink().getOriginalFilename()));
        // 生成视频文件的MD5
        model.setVideoLinkMd5(DigestUtils.md5DigestAsHex(model.getVideoLink().getInputStream()));
        // 先保存数据
        this.videoService.save(model, user);
        // 然后再写入文件，避免保存失败，上传临时文件。
        this.createDirectories(this.appProps.getImageUploadFolder());
        model.getVideoLogo().transferTo(Paths.get(this.appProps.getImageUploadFolder(), model.getVideoLogoPath()));
        this.createDirectories(this.appProps.getVideoUploadFolder());
        model.getVideoLink().transferTo(Paths.get(this.appProps.getVideoUploadFolder(), model.getVideoLinkPath()));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("添加成功！");
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping(path = "/edit")
    public ResponseEntity<?> edit(@RequestParam Integer id) {
        Video video = this.videoService.queryOne(id, FetchType.LAZY);
        // 如果视频不存在
        if (video == null) {
            throw new EntityNotFoundException("此视频不存在");
        }
        return ResponseEntity.ok(video);
    }

    /**
     * 处理编辑
     */
    @PutMapping(path = "/edit")
    public ResponseEntity<?> handleEdit(
            @CurrentUser User user,
            @ModelAttribute VideoModel model) throws Exception {
        if (model.getId() == null) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("视频id不能为空！");
        }
        Video video = this.videoService.queryOne(model.getId(), FetchType.LAZY);
        // 如果视频不存在
        if (video == null) {
            throw new EntityNotFoundException("此视频不存在");
        }
        // 如果视频不是自己创建的，不允许编辑
        if (!video.getCreator().equals(user.getUsername())) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        }
        if (!StringUtils.hasText(model.getVideoName())) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("请输入视频名称！");
        }
        if (model.getCategoryId() == null) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("请选择视频类别！");
        }
        // 如果重新上传了视频封面
        if (model.getVideoLogo() != null && !model.getVideoLogo().isEmpty()) {
            // 如果上传视频封面的格式不正确
            if (!this.isMatch(this.appProps.getImageMimePatterns(), model.getVideoLogo().getContentType())) {
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("上传视频封面的格式不正确，请重新上传！");
            }
            log.info("handleEdit(): VideoLogo = {}", model.getVideoLogo().getOriginalFilename());
            model.setVideoLogoPath(this.makeFileName(model.getVideoLogo().getOriginalFilename()));
        }
        // 如果重新上传了视频文件
        if (model.getVideoLink() != null && !model.getVideoLink().isEmpty()) {
            // 如果上传视频文件的格式不正确
            if (!this.isMatch(this.appProps.getVideoMimePatterns(), model.getVideoLink().getContentType())) {
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("上传视频文件的格式不正确，请重新上传！");
            }
            log.info("handleEdit(): VideoLink = {}", model.getVideoLink().getOriginalFilename());
            model.setVideoLinkPath(this.makeFileName(model.getVideoLink().getOriginalFilename()));
        }
        // 先保存数据
        this.videoService.update(model, user, video);
        // 然后再写入视频封面、文件，避免保存失败，上传临时文件。
        if (model.getVideoLogo() != null && !model.getVideoLogo().isEmpty()) {
            // 写入文件
            this.createDirectories(this.appProps.getImageUploadFolder());
            model.getVideoLogo().transferTo(Paths.get(this.appProps.getImageUploadFolder(), model.getVideoLogoPath()));
        }
        if (model.getVideoLink() != null && !model.getVideoLink().isEmpty()) {
            // 写入文件
            this.createDirectories(this.appProps.getVideoUploadFolder());
            model.getVideoLink().transferTo(Paths.get(this.appProps.getVideoUploadFolder(), model.getVideoLinkPath()));
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("编辑成功！");
    }

    private boolean isMatch(String[] patterns, String contentType) {
        log.info("isMatch(): patterns = {}, contentType = {}", patterns, contentType);
        if (!StringUtils.hasText(contentType)) {
            return false;
        }
        return Stream.of(patterns).anyMatch(pattern ->
                this.pathPatternParser.parse(pattern)
                        .matches(PathContainer.parsePath(contentType)));
    }

    private String makeFileName(String originFileName) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        if (!StringUtils.hasText(originFileName)) {
            return uuid;
        }
        return String.format("%s.%s", uuid, StringUtils.getFilenameExtension(originFileName));
    }

    private void createDirectories(String uploadFolder) throws Exception {
        Path path = Paths.get(uploadFolder);
        if (Files.notExists(path)) {
            // 创建上传目录
            Files.createDirectories(path);
        }
    }
}
