package com.trophonix.smp.features.bags.rewards;

import org.bukkit.entity.Player;

public interface GiftBagReward {

    void giveReward(Player player);

    String getName();

}
