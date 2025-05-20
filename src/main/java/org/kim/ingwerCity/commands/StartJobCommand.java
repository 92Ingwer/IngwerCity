package org.kim.ingwerCity.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.kim.ingwerCity.jobs.JobEnum;
import org.kim.ingwerCity.jobs.JobInterface;
import org.kim.ingwerCity.jobs.JobService;

import java.util.List;
import java.util.UUID;

public class StartJobCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(commandSender instanceof Player player)) {
            return false;
        }
        UUID uuid = player.getUniqueId();
        if(JobService.PLAYER_IN_A_JOB.containsKey(uuid)) {
            player.sendMessage("Du hast bereits einen Job!");
            return false;
        }
        List<JobInterface> jobList = JobService.playerJobsListMap.get(uuid);
        Location location = player.getLocation();
        Location jobLocation = JobEnum.nearestLocation(location);
        if(jobLocation == null) {
            player.sendMessage("Es gibt keinen Job in deiner Nähe!");
            return false;
        }
        String name = JobEnum.getJobName(jobLocation);
        JobInterface job = jobList.stream()
                .filter(jobInterface -> jobInterface.getJobName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
        if(job == null) {
            player.sendMessage("Es gibt keinen Job in deiner Nähe!");
            return false;
        }
        JobService.PLAYER_IN_A_JOB.put(uuid, job);
        job.onJobStart();

        return false;
    }
}
