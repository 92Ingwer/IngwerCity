package org.kim.ingwerCity.services;

import org.bukkit.entity.Player;
import org.kim.ingwerCity.faction.FactionObject;

import java.util.HashMap;
import java.util.UUID;

public class PlayerService {

    public boolean isPlayerInNear(Player player, Player target) {
        return player.getLocation().distance(target.getLocation()) <= 5;
    }
    public static HashMap<UUID, FactionObject> playerFactionInviteMap = new HashMap<>();
}
