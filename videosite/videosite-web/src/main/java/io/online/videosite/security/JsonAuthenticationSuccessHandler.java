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

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * JSON登录身份验证成功处理程序
 *
 * @author 张维维
 * @since 2024-07-10 20:43:49
 */
@Slf4j
@AllArgsConstructor
public class JsonAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try (PrintWriter out = response.getWriter()) {
            User user = (User) authentication.getPrincipal();
            // TODO 生成JWT
            user.setToken("");
            log.info("onAuthenticationSuccess(): user = {}", user);
            out.write(this.objectMapper.writeValueAsString(user));
        }
    }
}
