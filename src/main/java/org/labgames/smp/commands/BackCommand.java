package org.labgames.smp.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.labgames.smp.SMP;

import java.util.HashMap;
import java.util.Map;

public class BackCommand implements CommandExecutor, Listener {

  private Map<Player, Location> lastLocations = new HashMap<>();

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage(ChatColor.RED + "This command is for players and YOU AIN'T ONE");
      return true;
    }
    Player player = (Player) sender;
    Location last = lastLocations.get(player);
    if (last == null) {
      player.sendMessage(ChatColor.RED + "No location to send you back to!");
      return true;
    }

    player.sendMessage(ChatColor.YELLOW + "Teleporting you to previous location!");
    SMP.getPlugin(SMP.class).getPlayers().teleport(player, last, 5);
    return true;
  }

  @EventHandler
  public void onDeath(PlayerDeathEvent event) {
    lastLocations.put(event.getEntity(), event.getEntity().getLocation());
  }

  @EventHandler
  public void onTeleport(PlayerTeleportEvent event) {
    lastLocations.put(event.getPlayer(), event.getFrom());
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    lastLocations.remove(event.getPlayer());
  }

}
