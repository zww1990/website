package io.online.videosite.controller;

import io.online.videosite.security.CurrentUser;
import io.online.videosite.api.CategoryService;
import io.online.videosite.domain.Category;
import io.online.videosite.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 视频类别控制器
 *
 * @author 张维维
 */
@RestController
@RequestMapping(path = "/category")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * 处理添加类别
     */
    @PostMapping(path = "/add")
    public ResponseEntity<?> handleAdd(
            @RequestBody Category category,
            @CurrentUser User user) {
        if (!StringUtils.hasText(category.getCategoryName())) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("请输入视频类别名称！");
        }
        if (this.categoryService.checkExist(category)) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(String.format("此 %s 已存在！", category.getCategoryName()));
        }
        this.categoryService.save(category, user);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("添加成功！");
    }

    /**
     * 类别列表
     */
    @GetMapping(path = "/list")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(this.categoryService.query());
    }
}
