package io.online.videosite.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * 自定义配置属性类
 *
 * @author 张维维
 */
@ConfigurationProperties(ignoreInvalidFields = true, prefix = "videosite-app")
@Getter
@Setter
@ToString
public class VideoSiteAppProperties {
    /**
     * 拦截器包括的路径模式
     */
    private String[] includePathPatterns = {};
    /**
     * 拦截器排除的路径模式
     */
    private String[] excludePathPatterns = {};
    /**
     * 需要管理员身份的路径模式
     */
    private String[] adminPathPatterns = {};
    /**
     * 图片格式模式
     */
    private String[] imageMimePatterns = {"image/*"};
    /**
     * 视频格式模式
     */
    private String[] videoMimePatterns = {"video/*"};
    /**
     * 图片上传目录
     */
    private String imageUploadFolder = "/upload/images";
    /**
     * 视频上传目录
     */
    private String videoUploadFolder = "/upload/videos";
    /**
     * JWT配置
     */
    private JwtProperties jwt = new JwtProperties();

    @Getter
    @Setter
    public static class JwtProperties {
        /**
         * 密钥，最少32个字符
         */
        private String secret = "hellovideosite1234hellovideosite";
        /**
         * 发布者
         */
        private String issuer = "SpringSecurity";
        /**
         * 失效时间，单位分
         */
        @DurationUnit(ChronoUnit.MINUTES)
        private Duration expiration = Duration.ofMinutes(30);
    }
}
