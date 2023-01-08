package org.labgames.smp.features;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.labgames.smp.SMP;
import org.spigotmc.event.entity.EntityMountEvent;

public class ProtectedPets implements Listener {

  private static final NamespacedKey PET_FOLLOW_MODE = new NamespacedKey(SMP.getPlugin(SMP.class), "smp.pets.following");

  private void setOwnerData(LivingEntity pet, Player owner) {
    String name = owner.getPlayerListName();
    String possessive = name + (name.endsWith("s") ? "'" : "'s");
    pet.setCustomName(possessive + " " + pet.getType().getName());
    pet.setCustomNameVisible(true);
  }
  @EventHandler
  public void onTame(EntityTameEvent event) {
    if (!(event.getOwner() instanceof Player)) return;
    setOwnerData(event.getEntity(), (Player)event.getOwner());
  }

  @EventHandler
  public void onInteractWithEntity(PlayerInteractAtEntityEvent event) {
    if (event.getRightClicked() instanceof Tameable) {
      Tameable tamed = (Tameable) event.getRightClicked();
      if (tamed.getOwner() == null) return;
      if (!event.getPlayer().equals(tamed.getOwner())) {
        event.setCancelled(true);
        event.getPlayer().sendMessage(ChatColor.RED + "That pet isn't yours!");
        return;
      }
      setOwnerData(tamed, event.getPlayer());
    }
  }

  @EventHandler
  public void onRide(EntityMountEvent event) {
    if (!(event.getMount() instanceof Tameable)) return;
    Tameable tamed = (Tameable) event.getMount();
    if (tamed.getOwner() == null) return;
    if (!event.getEntity().equals(tamed.getOwner())) {
      event.setCancelled(true);
      event.getEntity().sendMessage(ChatColor.RED + "That pet isn't yours!");
    }
  }

  @EventHandler
  public void onEntityDamage(EntityDamageByEntityEvent event) {
    if (!(event.getEntity() instanceof Tameable)) return;
    Tameable tamed = (Tameable) event.getEntity();
    if (tamed.getOwner() == null || tamed.getOwner().equals(event.getDamager())) return;
    event.getDamager().sendMessage(ChatColor.RED + "That pet isn't yours!");
    event.setCancelled(true);
  }

}
