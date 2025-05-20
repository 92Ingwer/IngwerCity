package org.kim.ingwerCity.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.kim.ingwerCity.IngwerCity;
import org.kim.ingwerCity.database.SQLMethods;
import org.kim.ingwerCity.faction.FactionMethods;
import org.kim.ingwerCity.faction.FactionObject;
import org.kim.ingwerCity.messages.ErrorEnum;
import org.kim.ingwerCity.messages.PrefixEnum;
import org.kim.ingwerCity.messages.UsageEnum;
import org.kim.ingwerCity.objects.ICPlayer;

import java.util.UUID;

public class StatsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) return false;

        if (args.length > 1) {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(UsageEnum.STATS_USAGE.getMessage()));
            return false;
        }
        SQLMethods sqlMethods = new SQLMethods();
        IngwerCity plugin = IngwerCity.getInstance();
        UUID targetUUID;
        boolean selfLookup = args.length == 0;

        if (selfLookup) {
            targetUUID = player.getUniqueId();
            ICPlayer icPlayer = ICPlayer.IC_PLAYER_HASH_MAP.get(targetUUID);
            if (icPlayer == null) {
                player.sendMessage(PrefixEnum.STATS_PREFIX.getMessage().appendSpace().append(ErrorEnum.PLAYER_NOT_FOUND.getMessage()));
                return false;
            }
            sendStats(player, icPlayer);
        } else {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            targetUUID = offlinePlayer.getUniqueId();
            sqlMethods.userExists(targetUUID).thenAccept(userExsists ->  {
                if(userExsists) {
                    sqlMethods.getICPlayer(targetUUID).thenAccept(icPlayer -> Bukkit.getScheduler().runTask(plugin, () -> sendStats(player, icPlayer)));
                } else {
                    player.sendMessage(PrefixEnum.STATS_PREFIX.getMessage().appendSpace().append(ErrorEnum.PLAYER_NOT_FOUND.getMessage()));
                }
            });
        }

        return true;
    }

    private void sendStats(Player viewer, ICPlayer icPlayer) {
        double handmoney = icPlayer.getHandmoney();
        int level = icPlayer.getLevel();
        int rank = icPlayer.getFactionRank();
        int xp = icPlayer.getXp();
        double payday = icPlayer.getPayday();

        String factionName = "None";
        FactionObject factionObject = new FactionMethods().getPlayerFaction(icPlayer);
        if (factionObject != null) {
            factionName = factionObject.getName();
        }
        viewer.sendMessage(PrefixEnum.STATS_PREFIX.getMessage().appendSpace().append(Component.text("§aStats of " + Bukkit.getOfflinePlayer(icPlayer.getUuid()).getName())));
        viewer.sendMessage("§aHandmoney: §e" + handmoney);
        viewer.sendMessage("§aLevel: §e" + level);
        viewer.sendMessage("§aXP: §e" + xp);
        viewer.sendMessage("§aFaction: §e" + factionName);
        viewer.sendMessage("§aFaction Rank: §e" + rank);
        viewer.sendMessage("§aPayday: §e" + payday);
    }
}