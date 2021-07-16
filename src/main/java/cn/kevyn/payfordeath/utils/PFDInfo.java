package cn.kevyn.payfordeath.utils;

import org.bukkit.entity.Player;

public class PFDInfo {

    private Player player;
    private String deathWorldName;
    private boolean kept;
    private double ransom;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isKept() {
        return kept;
    }

    public void setKept(boolean kept) {
        this.kept = kept;
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
