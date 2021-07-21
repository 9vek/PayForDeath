package cn.kevyn.payfordeath.utils;

import org.bukkit.entity.Player;

public class PFDInfo {

    private Player player;
    private String deathWorldName;
    private String status;
    private double ransom;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public void setDeathWorldName(String deathWorldName) {
        this.deathWorldName = deathWorldName;
    }
}
