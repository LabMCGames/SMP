package com.trophonix.smp;

import com.trophonix.smp.commands.HomeCommands;
import com.trophonix.smp.features.FastSleep;
import org.bukkit.plugin.java.JavaPlugin;

public final class SMP extends JavaPlugin {

    private FastSleep fastSleep;

    @Override
    public void onEnable() {
        fastSleep = new FastSleep(this, getServer().getWorld("world"));
        HomeCommands homeCommands = new HomeCommands();
        getCommand("home").setExecutor(homeCommands);
        getCommand("sethome").setExecutor(homeCommands);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
