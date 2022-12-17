package org.labgames.smp.features;

import org.labgames.smp.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TeleportScrolls {

  public static final ItemStack SINGLE_TELEPORT_SCROLL =
      new ItemBuilder(Material.PAPER)
          .displayName("&bTeleport Scroll")
          .lore("&7Single-use item to", "&7teleport you to your chosen", "&7location!")
          .build();
}
