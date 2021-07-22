package cn.kevyn.payfordeath.utils;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfigHelper {

    private static ConfigHelper instance;
    public static ConfigHelper getInstance(Configuration config, boolean initDefault) {

        instance = new ConfigHelper(config, initDefault);
        return instance;

    }

    private Configuration config;
    private Map<String, ConfigurationSection> sections;

    private ConfigHelper (Configuration config, boolean initDefault) {

        this.config = config;
        this.sections = new HashMap<>();

        if (initDefault) {
            loadSection("default");
        }

    }

    public ConfigurationSection getSection(String name) {

        if (!sections.containsKey(name)) {
            loadSection(name);
        }

        return sections.get(name);
    }

    private void loadSection(String name) {

        ConfigurationSection section = config.getConfigurationSection(name);

        if (section == null) {
            sections.put(name, sections.get("default"));
            return;
        }

        if (!name.equals("default")) {

            ConfigurationSection defaultSection = getSection("default");
            Set<String> defaultKeys = defaultSection.getKeys(true);
            for (String key : defaultKeys) {

                if (section.get(key) == null) {
                    section.set(key, defaultSection.get(key));
                }

            }

        }

        sections.put(name, section);

    }

    public Configuration getConfig() {
        return config;
    }

}
