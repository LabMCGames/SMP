package org.labgames.smp.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.labgames.smp.SMP;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TpaCommands implements CommandExecutor, Listener {

  private List<TpRequest> tpaRequests = new ArrayList<>();

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage(ChatColor.RED + "This command is for players and YOU AIN'T ONE");
      return true;
    }
    Player player = (Player) sender;

    if (command.getName().equals("tpa")) {
      if (args.length == 0) {
        player.sendMessage(ChatColor.RED + "/tpa (player)");
        return true;
      }

      Player receiver = Bukkit.getPlayer(args[0]);
      if (receiver == null) {
        player.sendMessage(ChatColor.RED + "Unknown player " + args[0]);
        return true;
      }

      for (TpRequest requests : tpaRequests) {
        if (requests.sender.equals(player) && requests.receiver.equals(receiver)) {
          player.sendMessage(ChatColor.RED + "You already sent them a request recently!");
          return true;
        }
      }

      TpRequest request = new TpRequest(player, receiver);
      tpaRequests.add(request);
      receiver.sendMessage(player.getPlayerListName() + ChatColor.YELLOW + " would like to teleport to you!", ChatColor.YELLOW + "Type " + ChatColor.AQUA + "/tpaccept" + ChatColor.YELLOW + " to accept their request.");
      player.sendMessage(ChatColor.YELLOW + "Teleport request sent to " + ChatColor.GREEN + receiver.getPlayerListName());
      new BukkitRunnable() {
        @Override
        public void run() {
          if (tpaRequests.contains(request)) {
            expire(request);
          }
          cancel();
        }
      }.runTaskLater(SMP.getPlugin(SMP.class), 20 * 60 * 5);
      return true;
    } else if (command.getName().equals("tpaccept")) {
      TpRequest request = getLastRequest(player);

      if (request == null) {
        player.sendMessage(ChatColor.RED + "No teleport requests found!");
        return true;
      }

      tpaRequests.remove(request);
      request.sender.sendMessage(player.getPlayerListName() + ChatColor.YELLOW + " accepted your teleport request!");
      player.sendMessage(request.sender.getPlayerListName() + ChatColor.YELLOW + " is teleporting to you now!");
      SMP.getPlugin(SMP.class).getPlayers().teleport(request.sender, player.getLocation(), 5);
      return true;
    }
    return true;
  }

  private TpRequest getLastRequest(Player player) {
    TpRequest request = null;
    for (TpRequest tpaRequest : tpaRequests) {
      if (request == null || tpaRequest.timestamp.isAfter(request.timestamp)) {
        request = tpaRequest;
      }
    }
    return request;
  }

  private void expire(TpRequest request) {
    request.sender.sendMessage(ChatColor.RED + "Your teleport request to " + request.receiver.getPlayerListName() + ChatColor.RED + " has expired.");
    request.receiver.sendMessage(ChatColor.RED + "The teleport request from " + request.sender.getPlayerListName() + ChatColor.RED + " has expired.");
    tpaRequests.remove(request);
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event) {
    for (TpRequest request : tpaRequests) {
      if (event.getPlayer().equals(request.sender) || event.getPlayer().equals(request.receiver)) {
        expire(request);
      }
    }
  }

  private class TpRequest {

    protected final Player sender;
    protected final Player receiver;
    protected final Instant timestamp;

    public TpRequest(Player sender, Player receiver) {
      this.sender = sender;
      this.receiver = receiver;
      this.timestamp = Instant.now();
    }
  }

}
