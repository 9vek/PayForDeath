package cn.kevyn.payfordeath;

import cn.kevyn.payfordeath.utils.PFDBean;
import cn.kevyn.payfordeath.utils.StringFormula;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.ArrayList;
import java.util.List;

public class PFDListener implements Listener {

    public static PFDListener INSTANCE;

    private Economy economy;
    private Permission permission;

    public PFDListener() {
        PFDListener.INSTANCE = this;
        economy = PayForDeath.INSTANCE.getEconomy();
        permission = PayForDeath.INSTANCE.getPermissions();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();
        PFDBean pfdBean = new PFDBean();
        pfdBean.setPlayer(player);

        ConfigurationSection config = pfdBean.getConfig();
        String worldName = pfdBean.getDeathWorldName();
        double balance = pfdBean.getBalance();

        if (!config.getBoolean("enable")) {
            return;
        }

        if (permission != null) {

            if (permission.has(player, "pfd.ignore." + worldName)) {
                pfdBean.setStatus("ignore");
            }

            else if (permission.has(player, "pfd.exempt." + worldName)) {
                event.setKeepInventory(true);
                event.setKeepLevel(true);
                event.getDrops().clear();
                event.setDroppedExp(0);
                pfdBean.setStatus("exempt");
            }

        }

        if (pfdBean.getStatus() == null) {

            String deductFormula = config.getString("deduct-formula");
            double ransom = StringFormula.calculate(deductFormula, pfdBean);

            String upperLimitFormula = config.getString("upper-limit-formula");
            double max = StringFormula.calculate(upperLimitFormula, pfdBean);
            ransom = ransom < max ? ransom : max;

            pfdBean.setRansom(ransom);

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
                pfdBean.setStatus("kept");

            }
            else {

                event.setKeepInventory(false);
                event.setKeepLevel(false);
                pfdBean.setStatus("unkept");

                if (config.getBoolean("clear-instead-of-drop")) {
                    event.getDrops().clear();
                    event.setDroppedExp(0);
                }

            }
        }

        PFDBean.add(pfdBean);

    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        PFDBean pfdBean = PFDBean.get(player);
        String worldName = pfdBean.getDeathWorldName();

        if (pfdBean == null) {
            return;
        }

        ConfigurationSection config = pfdBean.getConfig();
        double ransom = pfdBean.getRansom();
        String message;

        switch (pfdBean.getStatus()) {
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

        PFDBean.remove(pfdBean);

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

