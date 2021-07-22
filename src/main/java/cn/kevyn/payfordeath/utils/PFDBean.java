package cn.kevyn.payfordeath.utils;

import cn.kevyn.payfordeath.PayForDeath;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PFDBean {

    private static List<PFDBean> pfdBeans = new ArrayList<>();

    public static PFDBean get(Player player) {
        for (PFDBean pfdBean : pfdBeans) {
            if (pfdBean.getPlayer() == player){
                return pfdBean;
            }
        }
        return null;
    }

    public static void add(PFDBean pfdBean) {
        pfdBeans.add(pfdBean);
    }

    public static void remove(PFDBean pfdBean) {
        pfdBeans.remove(pfdBean);
    }

    private Player player;
    private String deathWorldName;
    private String status;
    private double balance;
    private double ransom;
    private ConfigurationSection config;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        this.deathWorldName = player.getWorld().getName();
        this.balance = PayForDeath.INSTANCE.getEconomy().getBalance(player);
        this.config = PayForDeath.INSTANCE.getConfigHelper().getSection(deathWorldName);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getBalance() {
        return balance;
    }

    public double getRansom() {
        return ransom;
    }

    public void setRansom(double ransom) {
        this.ransom = ransom;
    }

    public String getDeathWorldName() {
        return deathWorldName;
    }

    public ConfigurationSection getConfig() {
        return config;
    }

}
