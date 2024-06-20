package io.online.videosite;

import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SnakeyamlTests {
    @Test
    public void testDump() {
        try {
            DumperOptions dumper = new DumperOptions();
            dumper.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(dumper);
            FileWriter writer = new FileWriter("d:\\demo.yml");
            Map<String, Object> map = new HashMap<>();
            map.put("spring.application.name", "demo");
            map.put("spring.application.age", 18);
            Object data = this.setValue(map);
            yaml.dump(data, writer);
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
            System.err.println(this.getValue("spring.application.age", load));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object setValue(String key, Object value) {
        if (key == null) {
            return Collections.emptyMap();
        }
        String[] keys = key.split("\\.");
        int length = keys.length;
        Map<String, Object> data = new HashMap<>();
        if (length == 1) {
            data.put(key, value);
        } else {
            Map<String, Object> temp = data;
            for (int i = 0; i < length - 1; i++) {
                if (!temp.containsKey(keys[i])) {
                    temp.put(keys[i], new HashMap<>());
                }
                temp = (Map) temp.get(keys[i]);
                if (i == length - 2) {
                    temp.put(keys[i + 1], value);
                }
            }
        }
        System.err.println(data);
        return data;
    }

    private Object setValue(Map<String, Object> dataMap) {
        if (dataMap == null) {
            return Collections.emptyMap();
        }
        Map<String, Object> data = new HashMap<>();
        dataMap.forEach((key, value) -> {
            String[] keys = key.split("\\.");
            int length = keys.length;
            if (length == 1) {
                data.put(key, value);
            } else {
                Map<String, Object> temp = data;
                for (int i = 0; i < length - 1; i++) {
                    if (!temp.containsKey(keys[i])) {
                        temp.put(keys[i], new HashMap<>());
                    }
                    temp = (Map) temp.get(keys[i]);
                    if (i == length - 2) {
                        temp.put(keys[i + 1], value);
                    }
                }
            }
        });
        System.err.println(data);
        return data;
    }

    private Object getValue(String key, Map<?, ?> valueMap) {
        if (key == null || valueMap == null) {
            return null;
        }
        String[] keys = key.split("\\.");
        Object value = valueMap.get(keys[0]);
        if (key.contains(".")) {
            if (value instanceof Map<?, ?> newValue) {
                String newKey = key.substring(key.indexOf(".") + 1);
//                System.err.println(newKey + " = " + newValue);
                return this.getValue(newKey, newValue);
            }
        }
        return value;
    }
}
