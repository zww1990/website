package io.online.videosite.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.online.videosite.constant.UserType;
import io.online.videosite.properties.VideoSiteAppProperties;
import io.online.videosite.security.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

/**
 * 自定义Spring MVC配置类
 *
 * @author 张维维
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
@Slf4j
public class VideoSiteAppConfig implements WebMvcConfigurer, ErrorPageRegistrar {

    /**
     * 注册密码编码器
     */
    @Bean
    static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/index.html"));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JsonLoginAuthenticationFilter jsonLoginAuthenticationFilter,
            JsonAuthenticationEntryPoint jsonAuthenticationEntryPoint,
            JsonLogoutSuccessHandler jsonLogoutSuccessHandler,
            JsonAccessDeniedHandler jsonAccessDeniedHandler,
            VideoSiteAppProperties appProps) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(appProps.getIncludePathPatterns()).hasAuthority(UserType.ROLE_NORMAL.name())
                        .requestMatchers(appProps.getAdminPathPatterns()).hasAuthority(UserType.ROLE_ADMIN.name())
                        // 任何请求任何人都能访问
                        .anyRequest().permitAll())
                // 启用CORS
                .cors(cors -> cors.configurationSource(this.corsConfigurationSource()))
                // 禁用CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // 禁用匿名身份
                .anonymous(AbstractHttpConfigurer::disable)
                // 禁用响应头
                .headers(AbstractHttpConfigurer::disable)
                // 禁用请求缓存
                .requestCache(AbstractHttpConfigurer::disable)
                // 禁用Spring Security使用HTTP session
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 启用登出功能
                .logout(logout -> logout
                        .logoutUrl("/user/logout")
                        .logoutSuccessHandler(jsonLogoutSuccessHandler))
                // 允许配置异常处理
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jsonAuthenticationEntryPoint)
                        .accessDeniedHandler(jsonAccessDeniedHandler))
                // 添加过滤器
                .addFilterAfter(jwtAuthenticationFilter, LogoutFilter.class)
                .addFilterAfter(jsonLoginAuthenticationFilter, LogoutFilter.class)
        ;
        return http.build();
    }

    /**
     * 跨域资源共享
     */
    @Bean
    @Primary
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of(CorsConfiguration.ALL));
        config.setAllowedHeaders(List.of(CorsConfiguration.ALL));
        config.setAllowedMethods(List.of(CorsConfiguration.ALL));
        config.setExposedHeaders(List.of(CorsConfiguration.ALL));
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        // 是否隐藏用户不存在异常
        authenticationProvider.setHideUserNotFoundExceptions(false);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            JwtHelper jwtHelper, UserDetailsService userDetailsService) {
        return new JwtAuthenticationFilter(jwtHelper, userDetailsService);
    }

    @Bean
    public JsonLoginAuthenticationFilter jsonLoginAuthenticationFilter(
            ObjectMapper objectMapper,
            AuthenticationManager authenticationManager,
            JwtHelper jwtHelper) {
        JsonLoginAuthenticationFilter filter = new JsonLoginAuthenticationFilter(objectMapper);
        filter.setAuthenticationManager(authenticationManager);
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
        filter.setFilterProcessesUrl("/user/login");
        filter.setAuthenticationFailureHandler(new JsonAuthenticationFailureHandler(objectMapper));
        filter.setAuthenticationSuccessHandler(new JsonAuthenticationSuccessHandler(objectMapper, jwtHelper));
        return filter;
    }

    @Bean
    public JsonAuthenticationEntryPoint jsonAuthenticationEntryPoint(ObjectMapper objectMapper) {
        return new JsonAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    public JsonLogoutSuccessHandler jsonLogoutSuccessHandler(ObjectMapper objectMapper) {
        return new JsonLogoutSuccessHandler(objectMapper);
    }

    @Bean
    public JsonAccessDeniedHandler jsonAccessDeniedHandler(ObjectMapper objectMapper) {
        return new JsonAccessDeniedHandler(objectMapper);
    }

    /**
     * 注册角色继承
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy(
                String.format("%s > %s", UserType.ROLE_ADMIN.name(), UserType.ROLE_NORMAL.name()));
    }
}
