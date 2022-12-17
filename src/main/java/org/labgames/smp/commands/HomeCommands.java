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
import java.util.stream.Collectors;

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
    if (command.getName().equals("homes")) {
      listHomes(datum);
    } else if (command.getName().equals("home")) {
      Home home;
      if (args.length > 0)
        home = datum.getHome(args[0]);
      else if (datum.getHomes().size() == 1) home = datum.getHomes().get(0);
      else home = datum.getHome("home");
      if (home == null) {
        listHomes(datum);
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
      } else {
        homeName = "home";
        String originalHomeName = homeName;
        int i = 1;
        while (datum.getHome(homeName) != null) {
          homeName = originalHomeName + i++;
        }
      }
      Home oldHome = datum.getHome(homeName);
      if (oldHome == null) {
        if (datum.getHomes().size() >= datum.getAllowedHomes()) {
          player.sendMessage(ChatColor.RED + "You've reached your maximum allowed homes!");
          return true;
        }
      } else {
        datum.removeHome(oldHome);
      }
      Location homeLocation = player.getLocation();
      datum.addHome(homeName, homeLocation);
      datum.save();
      player.sendMessage(ChatColor.GREEN + "Set home " + ChatColor.DARK_GREEN + homeName);
    } else if (command.getName().equals("delhome")) {
      Home home;
      if (args.length > 0)
        home = datum.getHome(args[0]);
      else if (datum.getHomes().size() == 1) home = datum.getHomes().get(0);
      else {
        listHomes(datum);
        return true;
      }
      if (home == null) {
        listHomes(datum);
        return true;
      }
      datum.removeHome(home);
      player.sendMessage(ChatColor.GREEN + "Deleted home " + home.getName());
    }
    return true;
  }

  @Override
  public List<String> onTabComplete(
      CommandSender sender, Command command, String label, String[] args) {
    if (sender instanceof Player) {
      if (command.getName().equals("home")) {
        PlayerData datum = SMP.getPlugin(SMP.class).getPlayers().getPlayerData(((Player) sender).getPlayer());
        if (datum.getHomes().isEmpty()) return null;
        if (args.length == 0 || args[0].equals("")) {
          return datum.getHomes().stream().map(Home::getName).collect(Collectors.toList());
        } else if (args.length == 1) {
          String beginning = args[0];
          return datum.getHomes().stream().map(Home::getName).filter(name -> name.startsWith(beginning)).collect(Collectors.toList());
        }
      }
    }
    return null;
  }

  private void listHomes(PlayerData datum) {
    if (datum.getHomes().isEmpty()) {
      datum.getPlayer().sendMessage(ChatColor.GREEN + "Set a home wtih " + ChatColor.DARK_GREEN + "/home [name (default=home)]");
      return;
    }
    StringJoiner joiner = new StringJoiner(", ");
    for (Home homes : datum.getHomes()) {
      joiner.add(homes.getName());
    }
    datum.getPlayer().sendMessage(ChatColor.GREEN + "Homes: " + ChatColor.WHITE + joiner);
  }

}
