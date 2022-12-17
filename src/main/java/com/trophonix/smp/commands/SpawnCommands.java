package com.trophonix.smp.commands;

import com.trophonix.smp.SMP;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommands implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage(ChatColor.RED + "This command is for players and YOU AIN'T ONE");
      return true;
    }
    SMP plugin = SMP.getPlugin(SMP.class);
    Player player = (Player) sender;
    if (command.getName().equals("spawn")) {
      Location spawnLocation = plugin.getSpawnLocation();
      if (spawnLocation != null) {
        plugin.getPlayers().teleport(player, spawnLocation, 5);
      }
    } else if (command.getName().equals("setspawn")) {
      Location location = player.getLocation();
      location.setX(location.getBlockX() + 0.5);
      location.setZ(location.getBlockZ() + 0.5);
      plugin.setSpawnLocation(location);
      player.sendMessage(ChatColor.GREEN + "Spawn set!");
    }
    return true;
  }
}
