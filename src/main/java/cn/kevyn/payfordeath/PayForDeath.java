package cn.kevyn.payfordeath;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class PayForDeath extends JavaPlugin {

    public final String ENABLE = ChatColor.BLUE + "[PayForDeath] 插件已经成功启用！";
    public final String DISABLE = ChatColor.RED + "[PayForDeath] 插件已经成功禁用！";
    public final String RELOAD = ChatColor.GREEN + "[PayForDeath] 插件配置文件已重新功载入！";

    private Economy economy;
    private PFDListener listener;

    @Override
    public void onEnable() {
        super.onEnable();

        saveDefaultConfig();

        if (!setupEconomy()) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.listener = new PFDListener(this);
        this.getCommand("pfd").setExecutor(new PFDCommand());
        this.getServer().getPluginManager().registerEvents(listener, this);
        this.getServer().getConsoleSender().sendMessage(ENABLE);

    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.getServer().getConsoleSender().sendMessage(DISABLE);
    }

    public void reloadPlugin() {
        reloadConfig();
        listener.loadConfig();
        getServer().getConsoleSender().sendMessage(RELOAD);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public Economy getEconomy() {
        return economy;
    }

    public static void main(String[] args) {

    }

}
