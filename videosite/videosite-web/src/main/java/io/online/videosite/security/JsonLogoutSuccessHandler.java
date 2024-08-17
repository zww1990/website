package io.online.videosite.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.online.videosite.domain.JsonWebToken;
import io.online.videosite.repository.JsonWebTokenRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * JSON登出成功处理程序
 *
 * @author 张维维
 * @since 2024-07-10 21:26:50
 */
@Slf4j
@AllArgsConstructor
public class JsonLogoutSuccessHandler implements LogoutSuccessHandler {
    private final ObjectMapper objectMapper;
    private final JsonWebTokenRepository jsonWebTokenRepository;

    @Override
    public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        log.info("onLogoutSuccess(): authentication = {}", authentication);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        Optional.ofNullable(authentication)
                .map(Authentication::getCredentials)
                .map(m -> (JsonWebToken) m)
                .map(JsonWebToken::getJwtId)
                .ifPresent(this.jsonWebTokenRepository::removeToken);
        try (PrintWriter out = response.getWriter()) {
            out.write(this.objectMapper.writeValueAsString("注销成功！"));
        }
    }
}
