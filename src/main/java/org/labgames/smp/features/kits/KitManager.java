package org.labgames.smp.features.kits;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

public class KitManager implements CommandExecutor, Listener {

  private final Kit[] kits = {
      new StarterKit()
  };

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage(ChatColor.RED + "This command is for players and YOU AIN'T ONE");
      return true;
    }
    Player player = (Player) sender;

    if (command.getName().equals("kit") && args.length > 0) {
      Kit kit = getKit(args[0]);
      if (kit != null) {
        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
        if (dataContainer.has(kit.getLastUseKey(), PersistentDataType.LONG)) {
          long lastUsage = dataContainer.get(kit.getLastUseKey(), PersistentDataType.LONG);
          long nextUsage = lastUsage + kit.getDelaySeconds();
          if (lastUsage > 0 && Instant.now().isBefore(Instant.ofEpochSecond(nextUsage))) {
            player.sendMessage(ChatColor.RED + "You can claim that kit again in " + timeRemaining(nextUsage - Instant.now().getEpochSecond()));
            return true;
          }
        }

        kit.give(player);
        dataContainer.set(kit.getLastUseKey(), PersistentDataType.LONG, Instant.now().getEpochSecond());
        player.sendMessage(ChatColor.GREEN + "You claimed kit " + kit.getName() + "!");
        return true;
      }
    }

    StringJoiner joiner = new StringJoiner(", ");
    for (Kit kit : kits) {
      joiner.add(kit.getName());
    }
    player.sendMessage(ChatColor.GREEN + "Kits: " + ChatColor.WHITE + joiner);
    return true;
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
