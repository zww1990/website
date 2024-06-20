package io.online.videosite;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

public class ApplicationModulesTests {
    @Test
    public void verifiesModularStructure() {
        ApplicationModules modules = ApplicationModules.of(VideoSiteApplication.class);
        modules.verify();
        modules.forEach(System.out::println);
    }

}
