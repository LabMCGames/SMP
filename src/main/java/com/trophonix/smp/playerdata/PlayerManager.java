package com.trophonix.smp.playerdata;

import com.trophonix.smp.SMP;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager implements Listener {

  private List<PlayerData> playerData = new ArrayList<>();
  private Map<Player, BukkitTask> teleportTasks = new HashMap<>();

  public PlayerManager() {
    SMP plugin = SMP.getPlugin(SMP.class);
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  public PlayerData getPlayerData(Player player) {
    for (PlayerData playerDatum : playerData) {
      if (playerDatum.getPlayer().equals(player)) {
        return playerDatum;
      }
    }
    return null;
  }

  public void teleport(Player player, Location location, int delaySeconds) {
    player.sendMessage(ChatColor.GREEN + "You will teleport in " + ChatColor.DARK_GREEN + ChatColor.BOLD + "5 seconds");
    cancelTeleport(player);
    teleportTasks.put(player, Bukkit.getScheduler().runTaskTimer(
            SMP.getPlugin(SMP.class),
            new Runnable() {
              private int countdown = delaySeconds;
              @Override
              public void run() {
                if (countdown == 0) {
                  player.teleport(location);
                  cancelTeleport(player);
                } else
                  player.sendMessage(ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + countdown-- + "..");
              }
            }, 0L, 20L
    ));
  }

  public void cancelTeleport(Player player) {
    BukkitTask teleportTask = teleportTasks.get(player);
    if (teleportTask != null) {
      player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Cancelled!");
      teleportTask.cancel();
    }
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    playerData.add(new PlayerData(event.getPlayer()));
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    cancelTeleport(event.getPlayer());
    playerData.remove(getPlayerData(event.getPlayer()));
  }

  @EventHandler
  public void onDamaged(EntityDamageByEntityEvent event) {
    if (event.getEntity() instanceof Player) {
      cancelTeleport((Player)event.getEntity());
    }
  }

  @EventHandler
  public void onMove(PlayerMoveEvent event) {
    if (event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockZ() == event.getTo().getBlockZ() && event.getFrom().getBlockY() == event.getTo().getBlockY()) {
      return;
    }
    BukkitTask teleportTask = teleportTasks.get(event.getPlayer());
    if (teleportTask != null) teleportTask.cancel();
  }

  @EventHandler
  public void onTeleport(PlayerTeleportEvent event) {
    cancelTeleport(event.getPlayer());
  }

}
