package org.labgames.smp.playerdata;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitTask;
import org.labgames.smp.SMP;

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
    cancelTeleport(player, true);
    teleportTasks.put(player, Bukkit.getScheduler().runTaskTimer(
        SMP.getPlugin(SMP.class),
        new Runnable() {
          private int countdown = delaySeconds;

          @Override
          public void run() {
            if (countdown == 0) {
              cancelTeleport(player, false);
              player.teleport(location);
            } else
              player.sendMessage(ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + countdown-- + "..");
          }
        }, 0L, 20L
    ));
  }

  public void cancelTeleport(Player player, boolean sendMessage) {
    BukkitTask teleportTask = teleportTasks.get(player);
    if (teleportTask != null) {
      if (sendMessage) player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Cancelled!");
      teleportTask.cancel();
      teleportTasks.remove(player);
    }
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    playerData.add(new PlayerData(event.getPlayer()));
    if (!event.getPlayer().hasPlayedBefore()) {
      SMP.getPlugin(SMP.class).getKits().getKit("starter").give(event.getPlayer());
    }
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    cancelTeleport(event.getPlayer(), false);
    playerData.remove(getPlayerData(event.getPlayer()));
  }

  @EventHandler
  public void onDamaged(EntityDamageByEntityEvent event) {
    if (event.getEntity() instanceof Player) {
      cancelTeleport((Player) event.getEntity(), true);
    }
    if (event.getDamager() instanceof Player) {
      cancelTeleport((Player) event.getDamager(), true);
    }
  }

  @EventHandler
  public void onMove(PlayerMoveEvent event) {
    if (event.getTo() == null) return;
    if (event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockZ() == event.getTo().getBlockZ() && event.getFrom().getBlockY() == event.getTo().getBlockY()) {
      return;
    }
    cancelTeleport(event.getPlayer(), true);
  }

  @EventHandler
  public void onTeleport(PlayerTeleportEvent event) {
    cancelTeleport(event.getPlayer(), true);
  }

  @EventHandler
  public void onDeath(PlayerDeathEvent event) {

  }

}
