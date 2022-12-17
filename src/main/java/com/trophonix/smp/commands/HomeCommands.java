package com.trophonix.smp.commands;

import com.trophonix.smp.SMP;
import com.trophonix.smp.features.Home;
import com.trophonix.smp.playerdata.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Pattern;

public class HomeCommands implements CommandExecutor, TabCompleter {

  private static final Pattern VALID_NAME = Pattern.compile("[a-z0-9/._-]+");

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage(ChatColor.RED + "This command is for players and YOU AIN'T ONE");
      return true;
    }
    SMP plugin = SMP.getPlugin(SMP.class);
    Player player = (Player) sender;
    PlayerData datum = plugin.getPlayers().getPlayerData(player);
    if (command.getName().equals("home")) {
      Home home = null;
      if (args.length > 0)
        home = datum.getHome(args[0]);
      if (home == null) {
        StringJoiner joiner = new StringJoiner(", ");
        for (Home homes : datum.getHomes()) {
          joiner.add(homes.getName());
        }
        player.sendMessage(ChatColor.GREEN + "Homes: " + ChatColor.WHITE + joiner);
        return true;
      }
      plugin.getPlayers().teleport(player, home.getLocation(), 5);
    } else if (command.getName().equals("sethome")) {
      String homeName;
      if (args.length > 0) {
        homeName = args[0];
        if (!VALID_NAME.matcher(homeName).matches()) {
          player.sendMessage(ChatColor.RED + "Invalid name. Must match [a-z0-9/._-]");
          return true;
        }
      } else homeName = "home";
      Location homeLocation = player.getLocation();
      datum.addHome(homeName, homeLocation);
      datum.save();
    }
    return true;
  }

  @Override
  public List<String> onTabComplete(
      CommandSender sender, Command command, String label, String[] args) {
    return null;
  }
}
