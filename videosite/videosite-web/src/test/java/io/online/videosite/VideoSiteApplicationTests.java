package io.online.videosite;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.Filter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.cors.CorsConfigurationSource;

@SpringBootTest
public class VideoSiteApplicationTests {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testContextLoads() throws Exception {
        this.applicationContext.getBeansOfType(Filter.class).forEach((k, v) -> System.err.println(k + " = " + v));
//        System.err.println(this.applicationContext.getBeansOfType(CorsConfigurationSource.class));
//        System.err.println(this.applicationContext.getBeansOfType(UserDetailsService.class));
//        ObjectWriter json = applicationContext.getBean(ObjectMapper.class).writerWithDefaultPrettyPrinter();
//        System.err.println(json.writeValueAsString(ResponseEntity.ok("hello world")));
//        System.err.println(json.writeValueAsString(ResponseEntity.internalServerError().body("what the fuck")));
//        System.err.println(json.writeValueAsString(ResponseEntity.notFound().build()));
//        System.err.println(json.writeValueAsString(
//                ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(HttpStatus.NOT_FOUND.toString())
//        ));
//        System.err.println(json.writeValueAsString(
//                ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(new EntityNotFoundException("此视频不存在").getLocalizedMessage())
//        ));
//        HttpClientErrorException ex = new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
//        System.err.println(json.writeValueAsString(
//                ResponseEntity.status(ex.getStatusCode())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(ex.getLocalizedMessage())
//        ));
    }

    @Test
    public void testErrorPage() throws Exception {
        System.err.println(this.applicationContext.getBeansOfType(ErrorPageRegistrar.class));
        System.err.println(this.applicationContext.getBean(ErrorPageRegistry.class));
    }

}
