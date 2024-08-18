package io.online.videosite.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.online.videosite.domain.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * JSON登录身份验证成功处理程序
 *
 * @author 张维维
 * @since 2024-07-10 20:43:49
 */
@Slf4j
@AllArgsConstructor
@Component
public class JsonAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper objectMapper;
    private final JwtHelper jwtHelper;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        User user = (User) authentication.getPrincipal();
        // 生成JWT令牌
        user.setToken(this.jwtHelper.createToken(user));
        log.info("onAuthenticationSuccess(): user = {}", user);
        try (PrintWriter out = response.getWriter()) {
            if (Objects.isNull(user.getToken())) {
                out.write(this.objectMapper.writeValueAsString("用户名或密码错误！"));
            } else {
                out.write(this.objectMapper.writeValueAsString(user));
            }
        }
    }
}
