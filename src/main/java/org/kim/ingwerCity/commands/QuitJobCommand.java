package org.kim.ingwerCity.commands;

import com.destroystokyo.paper.event.player.PlayerReadyArrowEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.kim.ingwerCity.jobs.JobInterface;
import org.kim.ingwerCity.jobs.JobService;

public class QuitJobCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(commandSender instanceof Player player)) {
            return false;
        }
        if(!JobService.PLAYER_IN_A_JOB.containsKey(player.getUniqueId())) {
            player.sendMessage("Du hast keinen Job!");
            return false;
        }
        JobInterface job = JobService.PLAYER_IN_A_JOB.get(player.getUniqueId());
        job.onJobEnd();
        player.sendMessage("Du hast deinen Job gekündigt!");
        return false;
    }
}
