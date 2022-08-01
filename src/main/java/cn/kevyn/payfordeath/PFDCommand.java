package cn.kevyn.payfordeath;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PFDCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (command.getName().equals("pfd-reload")) {
            PayForDeath.INSTANCE.onReload();
            return true;
        }

        return false;
    }
}
