package org.kim.ingwerCity.listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.kim.ingwerCity.jobs.JobService;
import org.kim.ingwerCity.jobs.elektriker.ElectricerJob;

import java.util.UUID;


public class PlayerInteractEvent implements Listener {

    @EventHandler
    public void onInteract(org.bukkit.event.player.PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Action action = event.getAction();
        if (JobService.PLAYER_IN_A_JOB.get(uuid) instanceof ElectricerJob && action == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            Location blockLocation = block.getLocation();
            ElectricerJob electricerJob = (ElectricerJob) JobService.PLAYER_IN_A_JOB.get(player.getUniqueId());
            if (electricerJob.getLocationHashMap().get(player).equals(blockLocation) && player.getInventory().getItemInMainHand().equals(ElectricerJob.getZange())) {
                electricerJob.openSortGame();
                event.setCancelled(true);
            }
        }
    }
}
