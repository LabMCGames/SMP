package com.trophonix.smp.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.List;

public class HomeCommands implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is for players and YOU AIN'T ONE");
            return true;
        }
        Player player = (Player) sender;
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        if (command.getName().equals("home")) {

        } else if (command.getName().equals("sethome")) {
            String homeName;
            if (args.length > 0) homeName = args[0];
            else homeName = "home";

            int homeCount = 0;
            for (NamespacedKey key : dataContainer.getKeys()) {
                if (key.getKey().startsWith("smp.homes")) {
                    homeCount++;
                }
            }
            Location homeLocation = player.getLocation();
            String homeString =
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
