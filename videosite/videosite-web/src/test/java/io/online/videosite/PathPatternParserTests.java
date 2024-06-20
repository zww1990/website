package io.online.videosite;

import org.junit.jupiter.api.Test;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

public class PathPatternParserTests {
    @Test
    public void testMatches() {
        try {
            PathPatternParser parser = PathPatternParser.defaultInstance;
            PathPattern pattern = parser.parse("/videohub/**");
            System.err.println(pattern.matches(PathContainer.parsePath("/videohub/audit")));
            System.err.println(pattern.matches(PathContainer.parsePath("/videohub/audit/132131")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
