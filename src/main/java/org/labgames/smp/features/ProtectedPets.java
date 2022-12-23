package org.labgames.smp.features;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.labgames.smp.SMP;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.UUID;

public class ProtectedPets implements Listener {

  private static final NamespacedKey PET_OWNER_UUID = new NamespacedKey(SMP.getPlugin(SMP.class), "smp.pet-owner");

  private void setOwnerData(LivingEntity pet, Player owner) {
    String name = owner.getPlayerListName();
    String possessive = name + (name.endsWith("s") ? "'" : "'s");
    pet.setCustomName(possessive + " " + pet.getType().getName());
    pet.setCustomNameVisible(true);
    PersistentDataContainer pdc = pet.getPersistentDataContainer();
    pdc.set(PET_OWNER_UUID, PersistentDataType.STRING, owner.getUniqueId().toString());
  }

  private UUID getOwnerData(Entity pet) {
    if (!hasOwnerData(pet)) return null;
    PersistentDataContainer pdc = pet.getPersistentDataContainer();
    return UUID.fromString(pdc.get(PET_OWNER_UUID, PersistentDataType.STRING));
  }

  private boolean hasOwnerData(Entity pet) {
    PersistentDataContainer pdc = pet.getPersistentDataContainer();
    return pdc.has(PET_OWNER_UUID, PersistentDataType.STRING);
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
    UUID owner = getOwnerData(event.getEntity());
    if (owner == null) return;
    if (owner.equals(event.getDamager().getUniqueId())) return;
    event.getDamager().sendMessage(ChatColor.RED + "That pet isn't yours!");
    event.setCancelled(true);
  }

}
