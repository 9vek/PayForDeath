package cn.kevyn.payfordeath;

import cn.kevyn.payfordeath.utils.PFDConfig;
import cn.kevyn.payfordeath.utils.PFDInfo;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.ArrayList;
import java.util.List;

public class PFDListener implements Listener {

    private PayForDeath pfd;
    private Economy economy;
    private Permission permissions;
    private List<String> enabledWorlds;
    private List<PFDInfo> pfdPlayerList;
    private List<PFDConfig> pfdConfigList;

    public PFDListener(PayForDeath pfd) {
        this.pfd = pfd;
        economy = pfd.getEconomy();
        permissions = pfd.getPermissions();
        enabledWorlds = new ArrayList<>();
        pfdPlayerList = new ArrayList<>();
        pfdConfigList = new ArrayList<>();
        loadConfig();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();
        String worldName = player.getWorld().getName();

        PFDInfo pfdInfo = new PFDInfo();
        pfdInfo.setPlayer(player);
        pfdInfo.setDeathWorldName(worldName);

        if (!enabledWorlds.contains(worldName)) {
            return;
        }

        if (permissions != null) {

            if (permissions.has(player, "pfd.ignore."+worldName)) {
                pfdInfo.setStatus("ignore");
            }

            else if (permissions.has(player, "pfd.exempt."+worldName)) {
                event.setKeepInventory(true);
                event.setKeepLevel(true);
                event.getDrops().clear();
                event.setDroppedExp(0);
                pfdInfo.setStatus("exempt");
            }

        }

        if (pfdInfo.getStatus() == null) {
            ConfigurationSection config = getConfig(worldName);
            double balance = economy.getBalance(player);
            double ransom = 0;

            if (config.getBoolean("deduct-by-amount")) {
                ransom += config.getDouble("base-amount");
                ransom += config.getDouble("level-increase-amount") * player.getLevel();
            }

            if (config.getBoolean("deduct-by-percent")) {
                double percent = (config.getDouble("base-percent")/100 + config.getDouble("level-increase-percent")/100 * player.getLevel());
                ransom += percent * balance;
            }

            if (config.getBoolean("deduct-by-amount") && ransom > config.getDouble("max-amount") && config.getDouble("max-amount") != -1) {
                ransom = config.getDouble("max-amount");
            }

            if (config.getBoolean("deduct-by-percent") && ransom / balance > config.getDouble("max-percent")/100 && config.getDouble("max-percent") != -1) {
                ransom = balance * (config.getDouble("max-percent")/100);
            }

            pfdInfo.setRansom(ransom);

            if (balance >= ransom || ransom == 0) {

                if (config.getBoolean("keep-inventory")) {
                    event.setKeepInventory(true);
                }
                if (config.getBoolean("keep-level")) {
                    event.setKeepLevel(true);
                }

                economy.withdrawPlayer(player, ransom);
                event.getDrops().clear();
                event.setDroppedExp(0);
                pfdInfo.setStatus("kept");
            }
            else {
                event.setKeepInventory(false);
                event.setKeepLevel(false);
                pfdInfo.setStatus("unkept");
                if (config.getBoolean("clear-instead-of-drop")) {
                    event.getDrops().clear();
                    event.setDroppedExp(0);
                }
            }
        }

        pfdPlayerList.add(pfdInfo);

    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        PFDInfo pfdInfo = checkPlayerList(player);

        if (pfdInfo == null) {
            return;
        }

        ConfigurationSection config = getConfig(pfdInfo.getDeathWorldName());
        double ransom = pfdInfo.getRansom();
        String message;

        switch (pfdInfo.getStatus()) {
            case "kept":
                message = config.getString("kept-message").replace("%s", String.format("%.2f", ransom));
                break;
            case "unkept":
                message = config.getString("unkept-message").replace("%s", String.format("%.2f", ransom));
                break;
            case "exempt":
                message = config.getString("exempt-message");
                break;
            default:
                message = null;
        }

        if (message != null ) {
            sendToPlayer(player, config, message);
        }

        pfdPlayerList.remove(pfdInfo);

    }

    public PFDInfo checkPlayerList(Player player) {
        for (PFDInfo pfdInfo : pfdPlayerList) {
            if (pfdInfo.getPlayer() == player){
                return pfdInfo;
            }
        }
        return null;
    }

    public void loadConfig() {
        pfdConfigList.clear();
        FileConfiguration config = pfd.getConfig();
        enabledWorlds = config.getStringList("enabled-worlds");
        for (String worldName : enabledWorlds) {
            pfdConfigList.add(new PFDConfig(worldName, pfd));
        }
    }

    public ConfigurationSection getConfig(String worldName) {
        for (PFDConfig config : pfdConfigList) {
            if (config.getWorldName().equals(worldName)) {
                return config.getWorldConfig();
            }
        }
        return null;
    }

    private void sendToPlayer(Player player, ConfigurationSection config, String message) {

        if (config.getBoolean("notice-by-action-bar")) {
            TextComponent textComponent = new TextComponent(message);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);
        }
        if (config.getBoolean("notice-by-message")) {
            player.sendMessage(message);
        }

    }

}

