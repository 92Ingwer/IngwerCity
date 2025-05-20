package org.kim.ingwerCity.faction;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.kim.ingwerCity.messages.ErrorEnum;
import org.kim.ingwerCity.messages.PrefixEnum;
import org.kim.ingwerCity.messages.SuccessfullEnum;
import org.kim.ingwerCity.messages.UsageEnum;
import org.kim.ingwerCity.objects.ICPlayer;

public class SetRankCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(commandSender instanceof Player player)) {
            return false;
        }
        if(strings.length != 2) {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(UsageEnum.SET_RANK_USAGE.getMessage()));
            return false;
        }
        Player targetPlayer = Bukkit.getServer().getPlayer(strings[0]);
        if(targetPlayer == null) {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(ErrorEnum.PLAYER_NOT_FOUND.getMessage()));
            return false;
        }
        ICPlayer icPlayer = ICPlayer.IC_PLAYER_HASH_MAP.get(player.getUniqueId());
        if(icPlayer.getFactionRank() < 5) {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(ErrorEnum.NOT_LEADER.getMessage()));
            return false;
        }
        FactionMethods factionMethods = new FactionMethods();
        if(factionMethods.isPlayerYourFaction(player.getUniqueId(),targetPlayer.getUniqueId())) {
            ICPlayer targetICPlayer = ICPlayer.IC_PLAYER_HASH_MAP.get(targetPlayer.getUniqueId());
            int rank;
            try {
                rank = Integer.parseInt(strings[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(ErrorEnum.INVALID_NUMBERS.getMessage()));
                return false;
            }
            if(rank < 0 || rank > 6) {
                player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(ErrorEnum.RANK_NOT_FOUND.getMessage()));
                return false;
            }
            targetICPlayer.setFactionRank(rank);
            factionMethods.sendFactionMessage(icPlayer.getFactionID(), PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(SuccessfullEnum.FACTION_SUCCESSFULL_SET_RANK.getMessage(targetPlayer.getName(), rank)));
        }


        return false;
    }
}
