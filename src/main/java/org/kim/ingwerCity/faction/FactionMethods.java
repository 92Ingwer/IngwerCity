package org.kim.ingwerCity.faction;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.kim.ingwerCity.database.SQLMethods;
import org.kim.ingwerCity.objects.ICPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class FactionMethods {

    public boolean isFactionAvaible(String name) {
        for (FactionObject faction : FactionObject.factionList) {
            if (faction.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    public FactionObject getPlayerFaction(ICPlayer icPlayer) {
        int factionID = icPlayer.getFactionID();
        for (FactionObject faction : FactionObject.factionList) {
            if (faction.getId() == factionID) {
                return faction;
            }
        }
        return null;
    }
    public CompletableFuture<FactionObject> getPlayerFactionAsync(UUID uuid) {
        SQLMethods sqlMethods = new SQLMethods();
        ICPlayer icPlayer = sqlMethods.getICPlayer(uuid).join();
        int factionID = icPlayer.getFactionID();
        for (FactionObject faction : FactionObject.factionList) {
            if (faction.getId() == factionID) {
                return CompletableFuture.completedFuture(faction);
            }
        }
        return CompletableFuture.completedFuture(null);
    }
    public FactionObject getFactionObject(String name) {
        for (FactionObject faction : FactionObject.factionList) {
            if (faction.getName().equalsIgnoreCase(name)) {
                return faction;
            }
        }
        return null;
    }
    public List<Player> getOnlineFactionMember(int factionID) {
        List<Player> onlineFactionMember = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            ICPlayer icPlayer = ICPlayer.IC_PLAYER_HASH_MAP.get(player.getUniqueId());
            if (icPlayer.getFactionID() == factionID) {
                onlineFactionMember.add(player);
            }
        }
        return onlineFactionMember;
    }
    public void sendFactionMessage(int factionID, Component message) {
        List<Player> memberList = getOnlineFactionMember(factionID);
        for(Player player : memberList) {
            player.sendMessage(message);
        }
    }
    public boolean isPlayerYourFaction(UUID playerUUID, UUID targetUUID) {
        ICPlayer icPlayer = ICPlayer.IC_PLAYER_HASH_MAP.get(playerUUID);
        ICPlayer targetICPlayer = ICPlayer.IC_PLAYER_HASH_MAP.get(targetUUID);
        int factionID = icPlayer.getFactionID();
        int targetFactionID = targetICPlayer.getFactionID();
        return factionID == targetFactionID;
    }
}
