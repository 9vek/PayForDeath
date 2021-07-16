package cn.kevyn.payfordeath.utils;

import cn.kevyn.payfordeath.PayForDeath;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class PFDConfig {

    private String worldName;
    private ConfigurationSection worldConfig;

    private String[] configPath = new String[] {
            "deduct-by-amount",
            "deduct-by-percent",
            "base-amount",
            "level-increase-amount",
            "max-amount",
            "base-percent",
            "level-increase-percent",
            "max-percent",
            "keep-inventory",
            "keep-level",
            "clear-instead-of-drop",
            "notice-by-action-bar",
            "notice-by-message",
            "kept-message",
            "unkept-message"
    };

    public PFDConfig(String worldName, PayForDeath pfd) {

        this.worldName = worldName;
        FileConfiguration config = pfd.getConfig();

        ConfigurationSection defaultConfig = config.getConfigurationSection("default");
        worldConfig = config.getConfigurationSection(worldName);

        if (worldConfig == null) {
            worldConfig = defaultConfig;
        }

        for (String path : configPath) {
            if (worldConfig.contains(path))
                continue;
            else worldConfig.set(path, defaultConfig.get(path));
        }

    }

    public String getWorldName() {
        return worldName;
    }

    public ConfigurationSection getWorldConfig() {
        return worldConfig;
    }

}
