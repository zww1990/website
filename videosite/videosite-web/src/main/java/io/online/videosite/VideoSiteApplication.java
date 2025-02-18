package io.online.videosite;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.stream.Stream;

/**
 * 视频网站应用程序启动类
 *
 * @author 张维维
 */
@SpringBootApplication
@ConfigurationPropertiesScan
@Slf4j
public class VideoSiteApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(VideoSiteApplication.class, args);
        log.info("Spring Bean Definition Count = {}", context.getBeanDefinitionCount());
//        printBeans(context);
    }

    static void printBeans(ApplicationContext context) {
        Stream.of(context.getBeanDefinitionNames()).forEach(System.err::println);
    }
}
