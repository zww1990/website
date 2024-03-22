package io.online.videosite;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.stream.Stream;

/**
 * 视频网站应用程序启动类
 *
 * @author 张维维
 */
@SpringBootApplication
@Slf4j
public class VideoSiteApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(VideoSiteApplication.class);
//        closePrintBanner(application);
        ConfigurableApplicationContext context = application.run(args);
        log.info("Spring Bean Definition Count = {}", context.getBeanDefinitionCount());
//        printBeans(context);
    }

    static void printBeans(ApplicationContext context) {
        Stream.of(context.getBeanDefinitionNames()).forEach(System.err::println);
    }

    static void closePrintBanner(SpringApplication application) {
        application.setBannerMode(Banner.Mode.OFF);
    }
}
