package io.online.videosite.security;

import io.online.videosite.constant.Constants;
import io.online.videosite.domain.JsonWebToken;
import io.online.videosite.repository.JsonWebTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/**
 * JWT身份验证过滤器
 *
 * @author 张维维
 * @since 2024-07-31 15:21:35
 */
@Slf4j
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;
    private final JsonWebTokenRepository jsonWebTokenRepository;
    private final AuthenticationFailureHandler failureHandler;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("doFilterInternal(): header = {}", header);
        if (StringUtils.hasText(header) && header.startsWith(Constants.TOKEN_PREFIX)) {
            String token = header.substring(Constants.TOKEN_PREFIX.length());
            log.info("doFilterInternal(): token = {}", token);
            if (StringUtils.hasText(token)) {
                JsonWebToken jsonWebToken = this.jwtHelper.getJsonWebToken(token);
                String username = jsonWebToken.getSubject();
                String jwtId = jsonWebToken.getJwtId();
                log.info("doFilterInternal(): username = {}, jwtId = {}", username, jwtId);
                if (this.jsonWebTokenRepository.getToken(jwtId).isPresent()) {
                    if (StringUtils.hasText(username) &&
                            Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                        if (this.jwtHelper.verifyToken(token, jwtId)) {
                            UsernamePasswordAuthenticationToken authenticated = UsernamePasswordAuthenticationToken
                                    .authenticated(userDetails, jsonWebToken, userDetails.getAuthorities());
                            authenticated.setDetails(new WebAuthenticationDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authenticated);
                        }
                    }
                } else {
                    BadJwtException cause = new BadJwtException("无效的JWT");
                    log.error(cause.getLocalizedMessage(), cause);
                    this.failureHandler.onAuthenticationFailure(request, response,
                            new BadCredentialsException("会话失效，请重新登录。", cause));
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
