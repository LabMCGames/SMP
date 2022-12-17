package com.trophonix.smp.features;

import com.trophonix.smp.SMP;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.scheduler.BukkitTask;

public class FastSleep implements Listener {

    private final SMP smp;
    private final World world;

    private float ticksPerTimeSkip;

    private BukkitTask skipTimeTask;

    public FastSleep(SMP smp, World world) {
        this.smp = smp;
        this.world = world;
        smp.getServer().getPluginManager().registerEvents(this, smp);
    }

    private void checkForSleepers() {
        int sleepers = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isSleeping()) sleepers ++;
        }
        int maximumSpeed = (Bukkit.getOnlinePlayers().size() / 5) + 1;
        if (sleepers > maximumSpeed) sleepers = maximumSpeed;
        ticksPerTimeSkip = (maximumSpeed / (float)sleepers) / 2;
        if (skipTimeTask == null) {
            skipTimeTask = Bukkit.getScheduler().runTaskTimer(smp, new Runnable() {
                private int ticksSinceTimeSkip = 0;
                @Override
                public void run() {
                    if (ticksPerTimeSkip < 1) {
                        world.setTime(world.getTime() + (int)(1 / ticksPerTimeSkip));
                    } else if (ticksSinceTimeSkip++ >= ticksPerTimeSkip) {
                        world.setTime(world.getTime() + 1);
                    }
                }
            }, 1L, 1L);
        }
    }

    @EventHandler
    public void onSleep(PlayerBedEnterEvent event) {
        checkForSleepers();
    }

    @EventHandler
    public void onLeaveBed(PlayerBedLeaveEvent event) {
        checkForSleepers();
    }

}
