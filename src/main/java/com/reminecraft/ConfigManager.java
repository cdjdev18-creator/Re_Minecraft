package com.reminecraft;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ConfigManager {
    private final File file;
    private final Map<String,String> values = new LinkedHashMap<>();

    public ConfigManager() {
        file = new File("config/options.txt");
        loadDefaults();
        load();
    }

    private void loadDefaults() {
        values.put("fullscreen","false");
        values.put("resolution","1280x720");
        values.put("masterVolume","1.0");
        values.put("mouseSensitivity","0.5");
        values.put("renderDistance","8");
        values.put("vsync","true");
    }

    public void load() {
        if (!file.exists()) { save(); return; }
        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                int i = line.indexOf('=');
                if (i < 0) continue;
                values.put(line.substring(0,i).trim(), line.substring(i+1).trim());
            }
        } catch (Exception e) { loadDefaults(); save(); }
    }

    public void save() {
        try {
            file.getParentFile().mkdirs();
            try (PrintWriter w = new PrintWriter(new FileWriter(file))) {
                for (Map.Entry<String,String> e : values.entrySet())
                    w.println(e.getKey() + "=" + e.getValue());
            }
        } catch (Exception ignored) {}
    }

    public String get(String k) { return values.getOrDefault(k, ""); }
    public void set(String k, String v) { values.put(k, v); }
    public boolean getBool(String k) { return Boolean.parseBoolean(get(k)); }
    public float getFloat(String k) { try { return Float.parseFloat(get(k)); } catch (Exception e) { return 0.5f; } }
    public int getInt(String k) { try { return Integer.parseInt(get(k)); } catch (Exception e) { return 8; } }
}
