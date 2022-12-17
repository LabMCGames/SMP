package org.labgames.smp.features;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;

public class Home {

  private NamespacedKey namespacedKey;
  private String name;
  private Location location;

  public Home(NamespacedKey namespacedKey, String name, Location location) {
    this.namespacedKey = namespacedKey;
    this.name = name;
    this.location = location;
  }

  public NamespacedKey getNamespacedKey() {
    return namespacedKey;
  }

  public String getName() {
    return name;
  }

  public Location getLocation() {
    return location;
  }
}
