package org.labgames.smp.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RulesCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    sender.sendMessage(" ",
        ChatColor.YELLOW + "Lab Games wants this SMP to be a",
        ChatColor.YELLOW + "safe and friendly environment!",
        " ",
        ChatColor.YELLOW +  "Please keep these rules in mind as you play:",
        ChatColor.DARK_AQUA + "1. " + ChatColor.AQUA + "No hacking or unfair advantages like auto clickers",
        ChatColor.DARK_AQUA + "2. " + ChatColor.AQUA + "No exploiting bugs, please report them instead!",
        ChatColor.DARK_AQUA + "3. " + ChatColor.AQUA + "No harassment or swearing at other players.",
        " ",
        ChatColor.YELLOW + "These rules may change at any time.",
        ChatColor.YELLOW + "Ignorance isn't an excuse not to follow them!",
        " ");
    return true;
  }

}
