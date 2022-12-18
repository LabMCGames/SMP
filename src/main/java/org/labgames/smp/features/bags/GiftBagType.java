package org.labgames.smp.features.bags;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.labgames.smp.util.ItemBuilder;

public enum GiftBagType {
  VOTER("&aVoter Gift Bag"),
  GOLD("&6Gold Gift Bag"),
  DIAMOND("&bDiamond Gift Bag");

  private String prettyName;
  private ItemStack itemStack;

  GiftBagType(String prettyName) {
    this.prettyName = prettyName;
    this.itemStack =
        new ItemBuilder(Material.BUNDLE)
            .displayName(prettyName + " &7(Right Click)")
            .lore(
                "&fRight Click this item",
                "&fto view possible rewards",
                "&fand open your gift bag!")
            .build();
  }

  public String getPrettyName() {
    return this.prettyName;
  }

  public ItemStack getItemStack() {
    return this.itemStack;
  }
}
