package cn.kevyn.payfordeath;

import cn.kevyn.payfordeath.utils.ConfigHelper;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class PayForDeath extends JavaPlugin {

    public static PayForDeath INSTANCE;

    public PayForDeath() {
        PayForDeath.INSTANCE = this;
    }

    public final String ENABLE = "[PayForDeath]" + ChatColor.BLUE + " plugin is now enabled！";
    public final String DISABLE = "[PayForDeath]" + ChatColor.RED + " plugin is now disabled！";
    public final String RELOAD = "[PayForDeath]" + ChatColor.GREEN + " config is now successfully reloaded！";

    private ConfigHelper configHelper;
    private Economy economy;
    private Permission permissions;

    @Override
    public void onEnable() {
        super.onEnable();

        saveDefaultConfig();
        configHelper = ConfigHelper.getInstance(getConfig(), true);

        if (dependenciesReady()) {
            if (permissions == null) {
                getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PayForDeath] permissions provider not found" );
            }
        } 
        else {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.getCommand("pfd-reload").setExecutor(new PFDCommand());
        this.getServer().getPluginManager().registerEvents(new PFDListener(), this);
        this.getServer().getConsoleSender().sendMessage(ENABLE);

    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.getServer().getConsoleSender().sendMessage(DISABLE);
    }

    public void onReload() {
        reloadConfig();
        configHelper = ConfigHelper.getInstance(getConfig(), true);
        getServer().getConsoleSender().sendMessage(RELOAD);
    }

    private boolean dependenciesReady() {

        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rspe = getServer().getServicesManager().getRegistration(Economy.class);
        RegisteredServiceProvider<Permission> rspp = getServer().getServicesManager().getRegistration(Permission.class);

        if (rspe == null) {
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PayForDeath] Economy provider is required but not found" );
            return false;
        }

        economy = rspe.getProvider();

        if (economy == null) {
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PayForDeath] Economy provider is required but not found" );
            return false;
        }

        permissions = rspp == null ? null : rspp.getProvider();

        return true;

    }

    public Economy getEconomy() {
        return economy;
    }

    public Permission getPermissions() {
        return permissions;
    }

    public ConfigHelper getConfigHelper() {
        return configHelper;
    }

    public static void main(String[] args) {

    }

}
