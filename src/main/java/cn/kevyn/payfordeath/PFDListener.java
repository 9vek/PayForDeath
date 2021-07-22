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
import java.util.Locale;

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

            if(max >= 0) {
                ransom = ransom < max ? ransom : max;
            }

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

        if (pfdBean == null) {
            return;
        }

        noticePlayer(pfdBean);

        PFDBean.remove(pfdBean);

    }

    private void noticePlayer(PFDBean pfdBean) {

        Player player = pfdBean.getPlayer();
        ConfigurationSection config = pfdBean.getConfig();
        String playerName = player.getName();
        String deathWorld = pfdBean.getDeathWorldName();
        String respawnWorld = player.getWorld().getName();
        String oldBalance = String.format("%.2f", pfdBean.getBalance());
        String ransom = String.format("%.2f",pfdBean.getRansom());
        String newBalance = String.format("%.2f", PayForDeath.INSTANCE.getEconomy().getBalance(player));

        String message = config.getString(pfdBean.getStatus()+"-message");
        message = message.replace("[player]", playerName)
                .replace("[death-world]", deathWorld)
                .replace("respawn-world", respawnWorld)
                .replace("[old-balance]", oldBalance)
                .replace("[ransom]", ransom)
                .replace("[new-balance]", newBalance);

        if (config.getBoolean("notice-by-action-bar")) {
            TextComponent textComponent = new TextComponent(message);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);
        }
        if (config.getBoolean("notice-by-console")) {
            player.sendMessage(message);
        }

    }

}

