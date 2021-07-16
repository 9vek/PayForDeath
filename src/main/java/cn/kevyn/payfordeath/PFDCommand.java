package cn.kevyn.payfordeath;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PFDCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (strings.length == 0)
            return false;

        if (strings[0].equals("reload")) {
            ((PayForDeath)commandSender.getServer().getPluginManager().getPlugin("PayForDeath")).reloadPlugin();
            return true;
        }

        return false;
    }
}
