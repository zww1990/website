package io.online.videosite;

import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;

public class SnakeyamlTests {
    @Test
    public void testDump() {
        try {
            DumperOptions dumper = new DumperOptions();
            dumper.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(dumper);
            yaml.dump(Map.of("spring", Map.of("application", Map.of("name", "demo"))), new FileWriter("d:\\demo.yml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoad() {
        try {
            Yaml yaml = new Yaml();
            Map<String, Object> load = yaml.load(new FileReader("d:\\demo.yml"));
            System.err.println(load);
            System.err.println(this.getValue("spring.application.name", load));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Object getValue(String key, Map<?, ?> valueMap) {
        if (key == null || valueMap == null) {
            return null;
        }
        String[] keys = key.split("[.]");
        Object value = valueMap.get(keys[0]);
        if (key.contains(".")) {
            if (value instanceof Map<?, ?> tmp) {
                return this.getValue(key.substring(key.indexOf(".") + 1), tmp);
            }
        }
        return value;
    }
}
