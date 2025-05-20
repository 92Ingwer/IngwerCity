package org.kim.ingwerCity.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.kim.ingwerCity.jobs.elektriker.ElectricerJob;

public class DropListener implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if(event.getItemDrop().getItemStack().equals(ElectricerJob.getZange())) {
            event.setCancelled(true);
        }
    }
}
