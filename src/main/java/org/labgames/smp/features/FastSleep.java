package org.labgames.smp.features;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.labgames.smp.SMP;

public class FastSleep implements Listener {

  private final SMP smp;
  private final World world;


  public FastSleep(SMP smp, World world) {
    this.smp = smp;
    this.world = world;
    smp.getServer().getPluginManager().registerEvents(this, smp);
  }

  private void checkForSleepers() {
    int sleepers = 0;
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (player.isSleeping()) sleepers++;
    }
    int third = Bukkit.getOnlinePlayers().size() / 3;
    if (sleepers < third) {
      for (Player player : Bukkit.getOnlinePlayers()) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "Sleep to skip night! " + ChatColor.WHITE + sleepers + ChatColor.GREEN + "/" + third));
      }
    } else {
      for (Player player : Bukkit.getOnlinePlayers()) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "Skipping night..."));
      }
    }
  }

  @EventHandler
  public void onSleep(PlayerBedEnterEvent event) {
    checkForSleepers();
  }

  @EventHandler
  public void onLeaveBed(PlayerBedLeaveEvent event) {
    if (world.getTime() > 998 && world.getTime() < 13000) return;
    checkForSleepers();
  }
}
