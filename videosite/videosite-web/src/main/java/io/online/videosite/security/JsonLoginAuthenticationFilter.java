package io.online.videosite.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * JSON登录身份验证过滤器
 *
 * @author 张维维
 * @since 2024-07-10 13:45:36
 */
@Component
@AllArgsConstructor
@Slf4j
public class JsonLoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper objectMapper;

    @Override
    @SuppressWarnings({"unchecked"})
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals(RequestMethod.POST.name())) {
            throw new AuthenticationServiceException("不支持身份验证方法: " + request.getMethod());
        }
        if (!MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType())) {
            return super.attemptAuthentication(request, response);
        }
        Map<String, String> postData;
        try {
            postData = this.objectMapper.readValue(request.getInputStream(), Map.class);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            throw new AuthenticationServiceException(e.getLocalizedMessage());
        }
        String username = postData.get(super.getUsernameParameter());
        username = (username != null) ? username.trim() : "";
        String password = postData.get(super.getPasswordParameter());
        password = (password != null) ? password : "";
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(
                username, password);
        // 允许子类设置 "details" 属性
        super.setDetails(request, authRequest);
        return super.getAuthenticationManager().authenticate(authRequest);
    }
}
