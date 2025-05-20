package org.kim.ingwerCity.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.kim.ingwerCity.database.SQLMethods;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class OnQuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        SQLMethods sqlMethods = new SQLMethods();
        UUID uuid = event.getPlayer().getUniqueId();
        CompletableFuture.runAsync(() -> {
            sqlMethods.savePlayerData(uuid);
            sqlMethods.updateNewJobPlayer(uuid);
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
    }
}
