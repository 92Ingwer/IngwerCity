package org.kim.ingwerCity.punishment;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.kim.ingwerCity.IngwerCity;
import org.kim.ingwerCity.database.SQLMethods;
import org.kim.ingwerCity.messages.ErrorEnum;
import org.kim.ingwerCity.messages.PrefixEnum;
import org.kim.ingwerCity.messages.SuccessfullEnum;
import org.kim.ingwerCity.messages.UsageEnum;

import java.util.concurrent.CompletableFuture;

public class UnbanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player player)) {
            return false;
        }
        IngwerCity plugin = IngwerCity.getInstance();
        SQLMethods sqlMethods = new SQLMethods();
        if (strings.length != 1) {
            player.sendMessage(PrefixEnum.PUNISH_PREFIX.getMessage().append(UsageEnum.PUNISH_USAGE.getMessage()));
            return false;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(strings[0]);
        if (target == null) {
            player.sendMessage(PrefixEnum.PUNISH_PREFIX.getMessage().append(ErrorEnum.PLAYER_NOT_FOUND.getMessage()));
            return false;
        }

        CompletableFuture.supplyAsync(() -> sqlMethods.isPlayerBanned(target.getUniqueId()))
                .thenAcceptAsync(isBanned -> {
                    if (!isBanned) {
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            player.sendMessage(PrefixEnum.PUNISH_PREFIX.getMessage()
                                    .append(ErrorEnum.PLAYER_NOT_BANNED.getMessage(target.getName())));
                        });
                        return;
                    }
                    sqlMethods.deletePlayer(target.getUniqueId());
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        player.sendMessage(PrefixEnum.PUNISH_PREFIX.getMessage()
                                .append(SuccessfullEnum.PLAYER_SUCCESSFULL_UNBAN.getMessage(target.getName())));
                    });
                });
        return false;
    }
}
