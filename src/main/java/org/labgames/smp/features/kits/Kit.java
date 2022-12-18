package org.labgames.smp.features.kits;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.labgames.smp.SMP;

public abstract class Kit {

  private final String name;
  private final long delaySeconds;

  private final NamespacedKey lastUseKey;

  public Kit(String name, long delaySeconds) {
    this.name = name;
    this.delaySeconds = delaySeconds;
    this.lastUseKey = new NamespacedKey(SMP.getPlugin(SMP.class), "smp.kits.last-use." + name);
  }

  public abstract void give(Player player);

  protected void giveItem(Player player, Material type, int amount) {
    player.getInventory().addItem(new ItemStack(type, amount)).forEach((_i, i) ->
        player.getWorld().dropItemNaturally(player.getLocation(), i));
  }

  protected void giveItem(Player player, Material type) {
    this.giveItem(player, type, 1);
  }

  public String getName() {
    return name;
  }

  public long getDelaySeconds() {
    return delaySeconds;
  }

  public NamespacedKey getLastUseKey() {
    return lastUseKey;
  }
}
