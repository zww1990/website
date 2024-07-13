package io.online.videosite.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/**
 * 自定义注解：获取当前认证的用户
 *
 * @author 张维维
 * @since 2024-07-08 15:17:48
 */
@AuthenticationPrincipal
@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {
}
