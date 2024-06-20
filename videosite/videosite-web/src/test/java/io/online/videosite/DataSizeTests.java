package io.online.videosite;

import org.junit.jupiter.api.Test;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

public class DataSizeTests {
    @Test
    public void testParse() {
        try {
            System.err.println(DataSize.parse("100", DataUnit.MEGABYTES));
            System.err.println(DataSize.parse("100MB"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
