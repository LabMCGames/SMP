package org.labgames.smp.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.labgames.smp.util.ItemBuilder;

public class HelpCommand implements CommandExecutor, Listener {

  private static final ItemStack BACKGROUND_ITEM = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).displayName(" ").hideAttributes().build();

  private static final ItemStack TELEPORT_COMMANDS_ITEM = new ItemBuilder(Material.ENDER_PEARL).hideAttributes()
      .displayName(ChatColor.GREEN + "Teleport Commands")
      .lore(
          " ",
          ChatColor.YELLOW + "/sethome (name) and /home (name)",
          ChatColor.WHITE + "Use to set a home and get back to it later.",
          ChatColor.WHITE + "If you don't provide a name, default is 'home'.",
          ChatColor.WHITE + "Everyone can use two sethomes with different names!",
          " ",
          ChatColor.YELLOW + "/tpa (player) and /tpaccept",
          ChatColor.WHITE + "Use /tpa (player) and ask to teleport to another player.",
          ChatColor.WHITE + "They can use /tpaccept to accept your request.",
          " "
      ).build();

  private static final ItemStack JOBS_ITEM = new ItemBuilder(Material.STONE_PICKAXE).hideAttributes()
      .displayName(ChatColor.GREEN + "Coins Economy")
      .lore(
          " ",
          ChatColor.YELLOW + "/jobs browse",
          ChatColor.WHITE + "Select your 3 favorite jobs and start earning!",
          " ",
          ChatColor.YELLOW + "/balance and /pay (player) (amount)",
          ChatColor.WHITE + "Check your balance and send other players money.",
          " ",
          ChatColor.YELLOW + "/trade (player)",
          ChatColor.WHITE + "Ask a player within 10 blocks to trade.",
          ChatColor.WHITE + "The trade menu, powered by Trade+, lets you",
          ChatColor.WHITE + "exchange items, money and other resources!",
          " "
      ).build();
  private Inventory helpMenu;

  public HelpCommand() {
    helpMenu = Bukkit.createInventory(null, 27, ChatColor.GREEN + "The Lab SMP");
    for (int i = 0; i < helpMenu.getSize(); i++) {
      helpMenu.setItem(i, BACKGROUND_ITEM);
    }
    helpMenu.setItem(10, TELEPORT_COMMANDS_ITEM);
    helpMenu.setItem(12, JOBS_ITEM);
  }

  @EventHandler
  public void onClick(InventoryClickEvent event) {
    if (helpMenu.getViewers().contains(event.getWhoClicked()) || helpMenu.equals(event.getClickedInventory())) {
      event.setCancelled(true);
    }
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage(ChatColor.RED + "This command is for players and YOU AIN'T ONE");
      return true;
    }
    Player player = (Player) sender;
    player.openInventory(helpMenu);
    return true;
  }
}
