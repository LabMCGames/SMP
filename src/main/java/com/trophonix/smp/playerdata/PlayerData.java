package com.trophonix.smp.playerdata;

import com.trophonix.smp.SMP;
import com.trophonix.smp.features.Home;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class PlayerData {

  private final Player player;

  private int allowedHomes;
  private List<Home> homes = new ArrayList<>();

  public PlayerData(Player player) {
    this.player = player;
    load();
  }

  public void save() {
    PersistentDataContainer dataContainer = player.getPersistentDataContainer();
    for (Home home : homes) {
      Location location = home.getLocation();
      String homeString =
          home.getName()
              + ";"
              + location.getWorld().getName()
              + ";"
              + (location.getBlockX() + 0.5)
              + ";"
              + location.getBlockY()
              + ";"
              + (location.getBlockZ() + 0.5);
      dataContainer.set(home.getNamespacedKey(), PersistentDataType.STRING, homeString);
    }
  }

  public void load() {
    SMP plugin = SMP.getPlugin(SMP.class);
    for (int i = 10; i > 0; i--) {
      if (player.hasPermission("smp.homes." + i)) {
        allowedHomes = i;
        break;
      }
    }

    PersistentDataContainer dataContainer = player.getPersistentDataContainer();
    int i = 0;
    for (NamespacedKey key : dataContainer.getKeys()) {
      if (!key.getKey().startsWith("smp.homes.")) continue;
      String homeString = dataContainer.get(key, PersistentDataType.STRING);
      String[] split = homeString.split(";");
      String homeName = split[0];
      World world = Bukkit.getWorld(split[1]);
      double x = Double.parseDouble(split[2]);
      double y = Double.parseDouble(split[3]);
      double z = Double.parseDouble(split[4]);
      NamespacedKey namespacedKey = new NamespacedKey(plugin, "smp.homes." + (i++));
      Location location = new Location(world, x, y, z);
      homes.add(new Home(namespacedKey, homeName, location));
    }
  }

  public Player getPlayer() {
    return this.player;
  }

  public int getAllowedHomes() {
    return allowedHomes;
  }

  public List<Home> getHomes() {
    return homes;
  }

  public void addHome(String name, Location home) {
    NamespacedKey namespacedKey =
        new NamespacedKey(SMP.getPlugin(SMP.class), "smp.homes." + homes.size());
    homes.add(new Home(namespacedKey, name, home));
  }

  public Home getHome(String name) {
    for (Home home : homes) {
      if (home.getName().equalsIgnoreCase(name)) {
        return home;
      }
    }
    return null;
  }
}
