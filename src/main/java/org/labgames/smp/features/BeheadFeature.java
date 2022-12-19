package org.labgames.smp.features;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class BeheadFeature implements Listener {

  @EventHandler
  public void onDeath(PlayerDeathEvent event) {
    Player player = event.getEntity();
    Player killer = player.getKiller();
    if (killer == null) return;
    if (!killer.getInventory().getItemInMainHand().getType().name().endsWith("_AXE")) return;
    ItemStack head = new ItemStack(Material.PLAYER_HEAD);
    SkullMeta meta = (SkullMeta) head.getItemMeta();
    meta.setOwnerProfile(player.getPlayerProfile());
    String displayName = ChatColor.GREEN + player.getName();
    if (displayName.endsWith("s")) displayName += "' Head";
    else displayName += "'s Head";
    meta.setDisplayName(displayName);
    head.setItemMeta(meta);
    player.getWorld().dropItemNaturally(player.getLocation(), head);
  }

}
