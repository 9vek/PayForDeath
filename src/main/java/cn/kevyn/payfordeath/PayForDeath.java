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

    // 提示字符串
    public final String ENABLE = ChatColor.BLUE + "[PayForDeath] Enabled! ";
    public final String DISABLE = ChatColor.RED + "[PayForDeath] Disabled! ";
    public final String RELOAD = ChatColor.GREEN + "[PayForDeath] Config Loaded！";

    private ConfigHelper configHelper;
    private Economy economy;
    private Permission permissions;

    @Override
    public void onEnable() {
        super.onEnable();

        loadConfig();

        // 检查前置
        if (dependenciesReady()) {

            // 如果权限提供者不存在，不影响使用，但影响功能
            if (permissions == null) {
                getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PayForDeath] Permissions Provider Not Found" );
            }

        } else {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // 注册
        this.getCommand("pfd").setExecutor(new PFDCommand());
        this.getServer().getPluginManager().registerEvents(new PFDListener(), this);
        this.getServer().getConsoleSender().sendMessage(ENABLE);

    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.getServer().getConsoleSender().sendMessage(DISABLE);
    }

    public void loadConfig() {
        saveDefaultConfig();
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
            return false;
        }

        economy = rspe.getProvider();

        if (economy == null) {
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
