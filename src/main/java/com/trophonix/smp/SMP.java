package com.trophonix.smp;

import com.trophonix.smp.commands.HomeCommands;
import com.trophonix.smp.commands.SpawnCommands;
import com.trophonix.smp.features.FastSleep;
import com.trophonix.smp.playerdata.PlayerManager;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public final class SMP extends JavaPlugin {

  private FastSleep fastSleep;

  private PlayerManager players;

  private Location spawnLocation;

  @Override
  public void onEnable() {
    fastSleep = new FastSleep(this, getServer().getWorld("world"));
    players = new PlayerManager();
    spawnLocation = getConfig().getLocation("spawn");
    HomeCommands homeCommands = new HomeCommands();
    getCommand("homes").setExecutor(homeCommands);
    getCommand("home").setExecutor(homeCommands);
    getCommand("sethome").setExecutor(homeCommands);
    getCommand("delhome").setExecutor(homeCommands);
    SpawnCommands spawnCommands = new SpawnCommands();
    getCommand("spawn").setExecutor(spawnCommands);
    getCommand("setspawn").setExecutor(spawnCommands);
  }

  @Override
  public void onDisable() {

  }

  public PlayerManager getPlayers() {
    return players;
  }

  public Location getSpawnLocation() {
    return spawnLocation;
  }

  public void setSpawnLocation(Location spawnLocation) {
    this.spawnLocation = spawnLocation;
  }

}
