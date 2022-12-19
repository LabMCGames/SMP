package org.labgames.smp.features.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class StarterKit extends Kit {

  public StarterKit() {
    super("starter", 60 * 60 * 12);
  }

  @Override
  public void give(Player player) {
    giveItem(player, Material.WOODEN_SWORD);
    giveItem(player, Material.BREAD, 24);
    giveItem(player, Material.LEATHER_CHESTPLATE);
    giveItem(player, Material.LEATHER_BOOTS);
  }

}
