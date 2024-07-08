package io.online.videosite.config;

import io.online.videosite.constant.Constants;
import io.online.videosite.constant.UserType;
import io.online.videosite.domain.User;
import io.online.videosite.properties.VideoSiteAppProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.stream.Stream;

/**
 * 身份验证拦截器
 *
 * @author 张维维
 */
@Slf4j
@AllArgsConstructor
//@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final PathPatternParser pathPatternParser = new PathPatternParser();
    private final VideoSiteAppProperties appProps;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String servletPath = request.getServletPath();
        User user = (User) request.getSession().getAttribute(Constants.SESSION_USER_KEY);
        log.info("preHandle(): servletPath = {}, user = {}", servletPath, user);
        // 如果会话中不存在登录的用户，不允许访问
        if (user == null) {
            log.info("preHandle(): 用户未登录！");
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }
        // 判断当前请求的路径是否需要管理员身份的用户
        boolean result = Stream.of(this.appProps.getAdminPathPatterns()).anyMatch(pattern -> {
            boolean matches = this.pathPatternParser.parse(pattern)
                    .matches(PathContainer.parsePath(servletPath));
            log.info("preHandle(): servletPath = {}, pattern = {}, matches = {}",
                    servletPath, pattern, matches);
            return matches;
        });
        // 如果需要管理员身份，但当前用户不是管理员
        if (result && user.getUserType() != UserType.ADMIN) {
            log.info("preHandle(): 用户未授权！");
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN);
        }
        return true;
    }
}
