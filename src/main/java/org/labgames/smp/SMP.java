package org.labgames.smp;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.labgames.smp.commands.*;
import org.labgames.smp.features.BeheadFeature;
import org.labgames.smp.features.FastSleep;
import org.labgames.smp.features.ProtectedPets;
import org.labgames.smp.features.kits.KitManager;
import org.labgames.smp.playerdata.PlayerManager;

public final class SMP extends JavaPlugin {

  private PlayerManager players;
  private KitManager kits;

  private Location spawnLocation;

  @Override
  public void onEnable() {
    new FastSleep(this, getServer().getWorld("world"));
    players = new PlayerManager();
    kits = new KitManager();
    getCommand("rules").setExecutor(new RulesCommand());
    getCommand("kit").setExecutor(kits);
    getCommand("kits").setExecutor(kits);
    getCommand("givekit").setExecutor(kits);
    spawnLocation = getConfig().getLocation("spawn");
    HomeCommands homeCommands = new HomeCommands();
    getCommand("homes").setExecutor(homeCommands);
    getCommand("home").setExecutor(homeCommands);
    getCommand("sethome").setExecutor(homeCommands);
    getCommand("delhome").setExecutor(homeCommands);
    SpawnCommands spawnCommands = new SpawnCommands();
    getCommand("spawn").setExecutor(spawnCommands);
    getCommand("setspawn").setExecutor(spawnCommands);
    TpaCommands tpaCommands = new TpaCommands();
    getCommand("tpa").setExecutor(tpaCommands);
    getCommand("tpaccept").setExecutor(tpaCommands);
    getCommand("tpdeny").setExecutor(tpaCommands);
    getCommand("tptoggle").setExecutor(tpaCommands);
    getServer().getPluginManager().registerEvents(tpaCommands, this);
    HelpCommand helpCommand = new HelpCommand();
    getCommand("help").setExecutor(helpCommand);
    getServer().getPluginManager().registerEvents(helpCommand, this);
    MessageCommands msgCommands = new MessageCommands();
    getCommand("message").setExecutor(msgCommands);
    getCommand("reply").setExecutor(msgCommands);
    getServer().getPluginManager().registerEvents(new BeheadFeature(), this);
    getServer().getPluginManager().registerEvents(new ProtectedPets(), this);
  }

  @Override
  public void onDisable() {

  }

  public PlayerManager getPlayers() {
    return players;
  }

  public KitManager getKits() {
    return kits;
  }

  public Location getSpawnLocation() {
    return spawnLocation;
  }

  public void setSpawnLocation(Location spawnLocation) {
    this.spawnLocation = spawnLocation;
  }

}
