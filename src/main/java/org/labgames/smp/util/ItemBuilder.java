package org.labgames.smp.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

public class ItemBuilder {

  private ItemStack stack;
  private ItemMeta meta;

  public ItemBuilder(Material type) {
    this.stack = new ItemStack(type);
    this.meta = this.stack.getItemMeta();
  }

  public ItemStack build() {
    this.stack.setItemMeta(this.meta);
    return this.stack;
  }

  public ItemBuilder amount(int amount) {
    this.stack.setAmount(amount);
    return this;
  }

  public ItemBuilder displayName(String displayName) {
    this.meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
    return this;
  }

  public ItemBuilder lore(String... lore) {
    for (int i = 0; i < lore.length; i++) {
      lore[i] = ChatColor.translateAlternateColorCodes('&', lore[i]);
    }
    this.meta.setLore(new ArrayList<>(Arrays.asList(lore)));
    return this;
  }

  public ItemBuilder hideAttributes() {
    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    return this;
  }

  public ItemBuilder meta(Consumer<ItemMeta> consumer) {
    consumer.accept(meta);
    return this;
  }
}
