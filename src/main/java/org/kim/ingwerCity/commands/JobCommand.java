package org.kim.ingwerCity.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.kim.ingwerCity.jobs.JobInterface;
import org.kim.ingwerCity.jobs.JobService;

import java.util.List;

public class JobCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(commandSender instanceof Player player)) {
            return false;
        }
        List<JobInterface> jobList = JobService.playerJobsListMap.get(player.getUniqueId());
        for(JobInterface job : jobList) {
            player.sendMessage("Job: " + job.getJobName() + " | Level: " + job.getLevel() + " | XP: " + job.getXP() + " | Current Salary: " + JobService.jobMoneyMap.get(job.getJobID()));
        }
        return false;
    }
}
