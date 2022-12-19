package org.labgames.smp.features.kits;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class KitManager implements CommandExecutor, TabCompleter, Listener {

  private final Kit[] kits = {
      new StarterKit()
  };

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (command.getName().equalsIgnoreCase("givekit")) {
      if (args.length < 2) {
        sender.sendMessage(ChatColor.RED + "/givekit (player) (kit) [anonymous true/false]");
        return true;
      }

      Player player = Bukkit.getPlayer(args[0]);
      if (player == null) {
        sender.sendMessage(ChatColor.RED + "Player " + args[0] + " isn't online!");
        return true;
      }

      Kit kit = getKit(args[1]);
      if (kit == null) {
        sender.sendMessage(ChatColor.GREEN + "Kits: " + ChatColor.WHITE + String.join(" ", Arrays.stream(kits).map(Kit::getName).collect(Collectors.toList())));
        return true;
      }

      boolean anonymous = args.length > 2 && args[2].equalsIgnoreCase("true");

      kit.give(player);
      String senderName = anonymous ? ChatColor.GREEN + "Someone" : sender instanceof Player ? ((Player)sender).getPlayerListName() : sender.getName();
      player.sendMessage(senderName + ChatColor.GREEN + " gifted you kit " + kit.getName() + "!");
      return true;
    }

    if (!(sender instanceof Player)) {
      sender.sendMessage(ChatColor.RED + "This command is for players and YOU AIN'T ONE");
      return true;
    }
    Player player = (Player) sender;

    if (command.getName().equals("kit") && args.length > 0) {
      Kit kit = getKit(args[0]);
      if (kit != null) {
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        long lastUsage = getLastUsage(player, kit);
        long nextUsage = lastUsage + kit.getDelaySeconds();
        if (lastUsage > 0 && Instant.now().isBefore(Instant.ofEpochSecond(nextUsage))) {
          player.sendMessage(ChatColor.RED + "You can claim that kit again in " + timeRemaining(nextUsage - Instant.now().getEpochSecond()));
          return true;
        }

        kit.give(player);
        dataContainer.set(kit.getLastUseKey(), PersistentDataType.LONG, Instant.now().getEpochSecond());
        player.sendMessage(ChatColor.GREEN + "You claimed kit " + kit.getName() + "!");
        return true;
      }
    }
    listKits(player);
    return true;
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
    if (command.getName().equals("kits")) return null;
    String started;
    if (command.getName().equals("kit") && args.length > 0) started = args[0];
    else if (command.getName().equals("givekit")) {
      if (args.length > 1) started = args[1];
      else return null;
    }
    else return Arrays.stream(kits).map(Kit::getName).collect(Collectors.toList());
    return Arrays.stream(kits).map(Kit::getName).filter(name -> name.startsWith(started)).collect(Collectors.toList());
  }

  private void listKits(Player player) {
    StringJoiner joiner = new StringJoiner(" ");
    for (Kit kit : kits) {
      String name;
      long lastUsage = getLastUsage(player, kit);
      if (lastUsage > 0 && Instant.now().isBefore(Instant.ofEpochSecond(lastUsage + kit.getDelaySeconds()))) {
        name = ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + kit.getName() + ChatColor.RESET;
      } else {
        name = kit.getName();
      }
      joiner.add(name);
    }
    player.sendMessage(ChatColor.GREEN + "Kits: " + ChatColor.WHITE + joiner);
  }

  private long getLastUsage(Player player, Kit kit) {
    PersistentDataContainer dataContainer = player.getPersistentDataContainer();
    if (!dataContainer.has(kit.getLastUseKey(), PersistentDataType.LONG)) return 0;
    return dataContainer.get(kit.getLastUseKey(), PersistentDataType.LONG);
  }

  public Kit getKit(String name) {
    for (Kit kit : kits) {
      if (kit.getName().equalsIgnoreCase(name)) {
        return kit;
      }
    }
    return null;
  }

  private String timeRemaining(long remainingSeconds) {
    int days = (int) TimeUnit.SECONDS.toDays(remainingSeconds);
    long hours = TimeUnit.SECONDS.toHours(remainingSeconds) - (days *24);
    long minutes = TimeUnit.SECONDS.toMinutes(remainingSeconds) - (TimeUnit.SECONDS.toHours(remainingSeconds)* 60);
    long seconds = TimeUnit.SECONDS.toSeconds(remainingSeconds) - (TimeUnit.SECONDS.toMinutes(remainingSeconds) *60);
    StringBuilder string = new StringBuilder();
    if (days > 0) string.append(days).append("d");
    if (hours > 0) string.append(hours).append("h");
    if (minutes > 0) string.append(minutes).append("m");
    if (seconds > 0) string.append(seconds).append("s");
    return string.toString();
  }

}
