package org.labgames.smp.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.labgames.smp.SMP;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TpaCommands implements CommandExecutor, Listener {

  private static final NamespacedKey ALLOW_REQUESTS = new NamespacedKey(SMP.getPlugin(SMP.class), "smp.settings.allows-tpa-requests");

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

      if (player.equals(receiver)) {
        player.sendMessage(ChatColor.RED + "You don't need to tp to yourself.");
        return true;
      }

      if (!allowsTpRequests(receiver)) {
        player.sendMessage(ChatColor.RED + "That player doesn't allow tp requests.");
        return true;
      }

      if (getRequest(player, receiver) != null) {
        player.sendMessage(ChatColor.RED + "You already sent them a request recently!");
        return true;
      }

      TpRequest request = new TpRequest(player, receiver);
      tpaRequests.add(request);
      receiver.sendMessage(player.getPlayerListName() + ChatColor.YELLOW + " would like to teleport to you!", ChatColor.AQUA + "Use /tpaccept or /tpdeny to respond, and /tptoggle to disallow future requests!");
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
      TpRequest request;

      if (args.length == 0) request = getLastRequest(player);
      else {
        Player requester = Bukkit.getPlayer(args[0]);
        if (requester == null) {
          player.sendMessage(ChatColor.RED + "Unknown player " + args[0]);
          return true;
        }

        request = getRequest(requester, player);
      }

      if (request == null) {
        player.sendMessage(ChatColor.RED + "No teleport requests found!");
        return true;
      }

      tpaRequests.remove(request);
      request.sender.sendMessage(player.getPlayerListName() + ChatColor.YELLOW + " accepted your teleport request!");
      player.sendMessage(request.sender.getPlayerListName() + ChatColor.YELLOW + " is teleporting to you now!");
      SMP.getPlugin(SMP.class).getPlayers().teleport(request.sender, player.getLocation(), 5);
      return true;
    } else if (command.getName().equals("tpdeny")) {
      TpRequest request;
      if (args.length == 0) request = getLastRequest(player);
      else {
        Player requester = Bukkit.getPlayer(args[0]);
        if (requester == null) {
          player.sendMessage(ChatColor.RED + "Unknown player " + args[0]);
          return true;
        }

        request = getRequest(requester, player);
      }

      if (request == null) {
        player.sendMessage(ChatColor.RED + "No teleport requests found!");
        return true;
      }

      tpaRequests.remove(request);
      request.sender.sendMessage(player.getPlayerListName() + ChatColor.RED + " declined your teleport request.");
      player.sendMessage(ChatColor.GREEN + "You declined a teleport request from " + request.sender.getPlayerListName());
      return true;
    } else if (command.getName().equals("tpatoggle")) {
      toggleTpRequests(player);
      if (allowsTpRequests(player)) {
        player.sendMessage(ChatColor.GREEN + "Tp requests enabled - players can /tpa you now!");
      } else {
        player.sendMessage(ChatColor.GREEN + "Tp requests disabled - players can no longer /tpa to you");
      }
    }
    return true;
  }

  private boolean allowsTpRequests(Player player) {
    PersistentDataContainer pdc = player.getPersistentDataContainer();
    return pdc.getOrDefault(ALLOW_REQUESTS, PersistentDataType.BYTE, (byte)1) == (byte)1;
  }

  private void toggleTpRequests(Player player) {
    PersistentDataContainer pdc = player.getPersistentDataContainer();
    pdc.set(ALLOW_REQUESTS, PersistentDataType.BYTE, allowsTpRequests(player) ? (byte) 0 : (byte) 1);
  }

  private TpRequest getLastRequest(Player player) {
    TpRequest request = null;
    for (TpRequest tpaRequest : tpaRequests) {
      if (!tpaRequest.receiver.equals(player)) continue;
      if (request == null || tpaRequest.timestamp.isAfter(request.timestamp)) {
        request = tpaRequest;
      }
    }
    return request;
  }

  private TpRequest getRequest(Player sender, Player receiver) {
    for (TpRequest tpaRequest : tpaRequests) {
      if (tpaRequest.sender.equals(sender) && tpaRequest.receiver.equals(receiver)) {
        return tpaRequest;
      }
    }
    return null;
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
