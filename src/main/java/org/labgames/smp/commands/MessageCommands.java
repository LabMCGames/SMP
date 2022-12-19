package org.labgames.smp.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.labgames.smp.SMP;
import org.labgames.smp.playerdata.PlayerData;

import java.util.Arrays;

public class MessageCommands implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (command.getName().equals("message")) {
      if (args.length < 2) {
        sender.sendMessage(ChatColor.RED + "/" + label + " (player) (message)");
        return true;
      }

      CommandSender receiver;
      if (args[0].equalsIgnoreCase("console"))
        receiver = Bukkit.getConsoleSender();
      else receiver = Bukkit.getPlayer(args[0]);

      if (receiver == null) {
        sender.sendMessage(ChatColor.RED + "Player " + args[0] + " isn't online!");
        return true;
      }

      String senderName = getSenderName(sender);
      String receiverName = getSenderName(receiver);

      String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
      receiver.sendMessage(
          ChatColor.YELLOW
              + "["
              + senderName
              + ChatColor.YELLOW
              + " -> "
              + ChatColor.GREEN
              + "me"
              + ChatColor.YELLOW
              + "] "
              + ChatColor.WHITE
              + message);
      sender.sendMessage(
              ChatColor.YELLOW
              + "["
              + ChatColor.GREEN
              + "me"
              + ChatColor.YELLOW
              + " -> "
              + receiverName
              + ChatColor.YELLOW
              + "] "
              + ChatColor.WHITE
              + message);

      if (receiver instanceof Player) {
        PlayerData receiverData = SMP.getPlugin(SMP.class).getPlayers().getPlayerData((Player)receiver);
        receiverData.setLastMessenger(sender.getName());
      }

      if (sender instanceof Player) {
        PlayerData senderData = SMP.getPlugin(SMP.class).getPlayers().getPlayerData(((Player) sender));
        senderData.setLastMessenger(receiver.getName());
      }
    } else if (command.getName().equals("reply")) {
      if (!(sender instanceof Player)) {
        sender.sendMessage(ChatColor.RED + "This command is for players and YOU AIN'T ONE");
        return true;
      }
      Player player = (Player) sender;

      if (args.length == 0) {
        player.sendMessage(ChatColor.RED + "/" + label + " (message)");
        return true;
      }

      PlayerData datum = SMP.getPlugin(SMP.class).getPlayers().getPlayerData(player);
      player.performCommand("msg " + datum.getLastMessenger() + " " + String.join(" ", args));
      return true;
    }
    return true;
  }

  private String getSenderName(CommandSender sender) {
    return sender instanceof Player ? ((Player) sender).getPlayerListName() : sender.getName();
  }
}
