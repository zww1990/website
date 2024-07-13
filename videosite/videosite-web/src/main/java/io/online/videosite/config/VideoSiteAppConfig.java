package io.online.videosite.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.online.videosite.properties.VideoSiteAppProperties;
import io.online.videosite.security.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.util.UrlPathHelper;

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

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders(CorsConfiguration.ALL)
                .allowedMethods(CorsConfiguration.ALL)
                .allowedOriginPatterns(CorsConfiguration.ALL)
                .exposedHeaders(CorsConfiguration.ALL);
    }

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
            JsonLoginAuthenticationFilter jsonLoginAuthenticationFilter,
            JsonAuthenticationEntryPoint jsonAuthenticationEntryPoint,
            JsonLogoutSuccessHandler jsonLogoutSuccessHandler,
            JsonAccessDeniedHandler jsonAccessDeniedHandler,
            VideoSiteAppProperties appProps) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(appProps.getIncludePathPatterns()).hasAuthority("ROLE_NORMAL")
                        .requestMatchers(appProps.getAdminPathPatterns()).hasAuthority("ROLE_ADMIN")
                        // 任何请求任何人都能访问
                        .anyRequest().permitAll())
                // 禁用CORS
                .cors(AbstractHttpConfigurer::disable)
                // 禁用CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // 禁用匿名身份
                .anonymous(AbstractHttpConfigurer::disable)
                // 禁用响应头
                .headers(AbstractHttpConfigurer::disable)
                // 禁用请求缓存
                .requestCache(AbstractHttpConfigurer::disable)
                // 启用登出功能
                .logout(logout -> logout.logoutUrl("/user/logout").logoutSuccessHandler(jsonLogoutSuccessHandler))
                // 允许配置异常处理
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jsonAuthenticationEntryPoint)
                        .accessDeniedHandler(jsonAccessDeniedHandler))
                // 添加过滤器
                .addFilterAfter(jsonLoginAuthenticationFilter, SecurityContextHolderFilter.class)
        ;
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public JsonLoginAuthenticationFilter jsonLoginAuthenticationFilter(
            ObjectMapper objectMapper, AuthenticationManager authenticationManager) {
        JsonLoginAuthenticationFilter filter = new JsonLoginAuthenticationFilter(objectMapper);
        filter.setAuthenticationManager(authenticationManager);
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
        filter.setFilterProcessesUrl("/user/login");
        filter.setAuthenticationFailureHandler(new JsonAuthenticationFailureHandler(objectMapper));
        filter.setAuthenticationSuccessHandler(new JsonAuthenticationSuccessHandler(objectMapper));
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
        return RoleHierarchyImpl.fromHierarchy("ROLE_ADMIN > ROLE_NORMAL");
    }
}
