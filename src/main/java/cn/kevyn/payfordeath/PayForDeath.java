package cn.kevyn.payfordeath;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class PayForDeath extends JavaPlugin {

    // 提示字符串
    public final String ENABLE = ChatColor.BLUE + "[PayForDeath] Enabled! ";
    public final String DISABLE = ChatColor.RED + "[PayForDeath] Disabled! ";
    public final String RELOAD = ChatColor.GREEN + "[PayForDeath] Reloaded！";

    // 经济管理实例，权限管理实例，监听器实例
    private Economy economy;
    private Permission permissions;
    private PFDListener listener;

    /**
     * 启用插件
     */
    @Override
    public void onEnable() {
        super.onEnable();

        // 保存默认配置文件
        saveDefaultConfig();

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
        this.listener = new PFDListener(this);
        this.getCommand("pfd").setExecutor(new PFDCommand());
        this.getServer().getPluginManager().registerEvents(listener, this);
        this.getServer().getConsoleSender().sendMessage(ENABLE);

    }

    /**
     * 禁用插件
     */
    @Override
    public void onDisable() {
        super.onDisable();
        this.getServer().getConsoleSender().sendMessage(DISABLE);
    }

    /**
     * 重载配置文件
     */
    public void reloadPlugin() {
        reloadConfig();
        listener.loadConfig();
        getServer().getConsoleSender().sendMessage(RELOAD);
    }

    /**
     * 检查前置
     * 一定要有经济提供者
     * 没有权限提供者影响功能
     * @return 不影响使用吗？
     */
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

    public static void main(String[] args) {

    }

}
