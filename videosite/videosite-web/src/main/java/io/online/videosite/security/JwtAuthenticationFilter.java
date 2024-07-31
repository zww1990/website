package io.online.videosite.security;

import io.online.videosite.properties.VideoSiteAppProperties;
import io.online.videosite.properties.VideoSiteAppProperties.JwtProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/**
 * JWT身份验证过滤器
 * @author 张维维
 * @since 2024-07-31 15:21:35
 */
@Slf4j
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final VideoSiteAppProperties properties;
    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        JwtProperties config = this.properties.getJwt();
        log.info("doFilterInternal(): header = {}, tokenPrefix = {}",
                header, config.getTokenPrefix());
        if (StringUtils.hasText(header) && header.startsWith(config.getTokenPrefix())) {
            String token = header.substring(config.getTokenPrefix().length());
            log.info("doFilterInternal(): token = {}", token);
            if (StringUtils.hasText(token)) {
                String username = this.jwtHelper.getSubject(token);
                log.info("doFilterInternal(): username = {}", username);
                if (StringUtils.hasText(username) &&
                        Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    if (this.jwtHelper.verifyToken(token)) {
                        UsernamePasswordAuthenticationToken authenticated = UsernamePasswordAuthenticationToken
                                .authenticated(userDetails, null, userDetails.getAuthorities());
                        authenticated.setDetails(new WebAuthenticationDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authenticated);
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
