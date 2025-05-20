package org.kim.ingwerCity.punishment;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kim.ingwerCity.IngwerCity;
import org.kim.ingwerCity.database.SQLMethods;
import org.kim.ingwerCity.messages.ErrorEnum;
import org.kim.ingwerCity.messages.PrefixEnum;
import org.kim.ingwerCity.messages.SuccessfullEnum;
import org.kim.ingwerCity.messages.UsageEnum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PunishCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        SQLMethods sqlMethods = new SQLMethods();
        if (!(commandSender instanceof Player player)) {
            return false;
        }
        if (args.length != 3) {
            player.sendMessage(UsageEnum.PUNISH_USAGE.getMessage());
            return false;
        }
        IngwerCity plugin = IngwerCity.getInstance();
        if (args[0].equals("ban")) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            if (target == null) {
                player.sendMessage(PrefixEnum.PUNISH_PREFIX.getMessage().append(ErrorEnum.PLAYER_NOT_FOUND.getMessage()));
                return false;
            }
            long millis = BanReasonEnum.getDate(args[2]);
            if (millis == -1) {
                player.sendMessage(PrefixEnum.PUNISH_PREFIX.getMessage().append(ErrorEnum.BAN_REASON_NOT_FOUND.getMessage()));
                return false;
            }
            plugin.getServer().getAsyncScheduler().runNow(plugin, task -> {
                if (sqlMethods.isPlayerBanned(target.getUniqueId())) {
                    plugin.getServer().getGlobalRegionScheduler().execute(plugin, () -> {
                        player.sendMessage(PrefixEnum.PUNISH_PREFIX.getMessage().append(ErrorEnum.PLAYER_ALREADY_BANNED.getMessage(player.getName())));
                    });
                    return;
                }
                sqlMethods.deletePlayer(target.getUniqueId());
                sqlMethods.punishPlayer(target.getUniqueId(), "Ban", args[2], millis);
                Date date = new Date(millis);
                plugin.getServer().getGlobalRegionScheduler().execute(plugin, () -> {
                    player.sendMessage(PrefixEnum.PUNISH_PREFIX.getMessage().append(Component.space()).append(SuccessfullEnum.PLAYER_SUCCESSFULL_BAN.getMessage(player.getName(), date)));
                    Player targetPlayer = Bukkit.getPlayer(target.getUniqueId());
                    if (targetPlayer != null && targetPlayer.isOnline()) {
                        targetPlayer.kick(PrefixEnum.PUNISH_PREFIX.getMessage().append(SuccessfullEnum.PLAYER_SUCCESSFULL_BAN_PLAYER.getMessage(args[2], date)));
                    }
                });
            });
        } else if (args[0].equals("kick")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(PrefixEnum.PUNISH_PREFIX.getMessage().append(ErrorEnum.PLAYER_NOT_FOUND.getMessage()));
                return false;
            }
            target.kick(SuccessfullEnum.PLAYER_KICK_MESSAGE.getMessage(args[2], player.getName()));
        } else {
            player.sendMessage(PrefixEnum.PUNISH_PREFIX.getMessage().append(ErrorEnum.INVALID_PUNISHMENT_TYPE.getMessage()));
        }
        return false;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (strings.length == 1) {
            return List.of("ban", "kick");
        }
        if (strings.length == 3 && strings[0].equals("ban")) {
            List<String> reasons = new ArrayList<>();
            for (BanReasonEnum banReasonEnum : BanReasonEnum.values()) {
                reasons.add(banReasonEnum.getReason());
            }
            return reasons;
        }
        return null;
    }
}
