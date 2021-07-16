package cn.kevyn.payfordeath;

import cn.kevyn.payfordeath.utils.PFDConfig;
import cn.kevyn.payfordeath.utils.PFDInfo;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
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
    private List<String> enabledWorlds;
    private List<PFDInfo> pfdPlayerList;
    private List<PFDConfig> pfdConfigList;

    public PFDListener(PayForDeath pfd) {
        this.pfd = pfd;
        economy = pfd.getEconomy();
        enabledWorlds = new ArrayList<>();
        pfdPlayerList = new ArrayList<>();
        pfdConfigList = new ArrayList<>();
        loadConfig();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        String worldName = player.getWorld().getName();
        if (!enabledWorlds.contains(worldName)) {
            return;
        }

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


        PFDInfo pfdInfo = new PFDInfo();
        pfdInfo.setPlayer(player);
        pfdInfo.setDeathWorldName(worldName);
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
            pfdInfo.setKept(true);
        }
        else {
            event.setKeepInventory(false);
            event.setKeepLevel(false);
            pfdInfo.setKept(false);
            if (config.getBoolean("clear-instead-of-drop")) {
                event.getDrops().clear();
                event.setDroppedExp(0);
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

        if (pfdInfo.isKept()){
            String kmsg = config.getString("kept-message").replace("%s", String.format("%.2f", ransom));
            if (config.getBoolean("notice-by-action-bar")) {
                TextComponent textComponent = new TextComponent(kmsg);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);
            }
            if (config.getBoolean("notice-by-message")) {
                player.sendMessage(kmsg);
            }
        }

        else {
            String ukmsg = config.getString("unkept-message").replace("%s", String.format("%.2f", ransom));
            if (config.getBoolean("notice-by-action-bar")) {
                TextComponent textComponent = new TextComponent(ukmsg);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);
            }
            if (config.getBoolean("notice-by-message")) {
                player.sendMessage(ukmsg);
            }
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

}

