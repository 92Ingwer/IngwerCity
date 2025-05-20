package org.kim.ingwerCity.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.kim.ingwerCity.database.SQLMethods;
import org.kim.ingwerCity.jobs.JobInterface;
import org.kim.ingwerCity.jobs.JobService;
import org.kim.ingwerCity.jobs.elektriker.ElectricerJob;
import org.kim.ingwerCity.objects.ICPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class OnJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        SQLMethods sqlMethods = new SQLMethods();
        sqlMethods.userExists(uuid).thenAcceptAsync(userExists -> {
            if (!userExists) {
                sqlMethods.insertUser(uuid);
                ICPlayer icPlayer = new ICPlayer(player);
                ICPlayer.IC_PLAYER_HASH_MAP.put(uuid, icPlayer);
                insertFirstJobsInList(uuid);
            } else {
                sqlMethods.getICPlayer(uuid).thenAcceptAsync(icPlayer -> {
                    ICPlayer.IC_PLAYER_HASH_MAP.put(uuid, icPlayer);
                    sqlMethods.updateNewJobPlayer(uuid);
                    insertJobsInList(uuid);
                });
            }
        });
    }

    public void insertFirstJobsInList(UUID uuid) {
        List<JobInterface> jobList = new ArrayList<>();
        JobInterface electricerJob = new ElectricerJob(0, 0, Bukkit.getPlayer(uuid));
        jobList.add(electricerJob);
        JobService.playerJobsListMap.put(uuid, jobList);
    }
    public void insertJobsInList(UUID uuid) {
        SQLMethods sqlMethods = new SQLMethods();
        sqlMethods.getJobs(uuid).thenAcceptAsync(jobs -> JobService.playerJobsListMap.put(uuid, jobs));
    }
}
