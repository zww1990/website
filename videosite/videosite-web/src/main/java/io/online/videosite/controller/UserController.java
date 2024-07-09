package io.online.videosite.controller;

import io.online.videosite.api.UserService;
import io.online.videosite.domain.User;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

/**
 * 用户控制器
 *
 * @author 张维维
 */
@RestController
@RequestMapping(path = "/user")
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    // 匹配非大小写字母、数字、下划线
    private final Pattern pattern = Pattern.compile("\\W");

    /**
     * 处理用户登录
     */
    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        log.info("login(): user = {}", user);
        if (!StringUtils.hasText(user.getUsername())) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("请输入用户名！");
        }
        if (!StringUtils.hasText(user.getPassword())) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("请输入密码！");
        }
        Authentication authentication = this.authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(
                        user.getUsername(), user.getPassword()));
        if (!authentication.isAuthenticated()) {
            log.info("login(): 此用户 {} 不存在或密码不正确！", user.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("用户名或密码不正确！");
        }
        System.err.println(SecurityContextHolder.getContext().getAuthentication());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.err.println(SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(authentication.getPrincipal());
    }

    /**
     * 处理用户注册
     */
    @PostMapping(path = "/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        log.info("register(): user = {}", user);
        if (!StringUtils.hasText(user.getUsername())) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("请输入用户名！");
        }
        if (this.pattern.matcher(user.getUsername()).find()) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("用户名只允许输入：大写或小写字母、数字、下划线！");
        }
        if (!StringUtils.hasText(user.getNickname())) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("请输入昵称！");
        }
        if (!StringUtils.hasText(user.getPassword())) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("请输入密码！");
        }
        if (!StringUtils.hasText(user.getPassword2())) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("请再次确认密码！");
        }
        if (!user.getPassword().equals(user.getPassword2())) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("两次输入的密码不一致！");
        }
        User entity = this.userService.query(user);
        if (entity != null) {
            log.info("login(): 此用户 {} 已存在！", user.getUsername());
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(String.format("此用户 %s 已存在！", user.getUsername()));
        }
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));// 密码加密
        this.userService.save(user);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("注册成功！");
    }

    /**
     * 注销登录
     */
    @GetMapping(path = "/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("注销成功！");
    }

    /**
     * 用户列表
     */
    @GetMapping(path = "/list")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(this.userService.queryUsers());
    }
}
