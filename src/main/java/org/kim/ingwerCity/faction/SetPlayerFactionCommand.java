package org.kim.ingwerCity.faction;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kim.ingwerCity.messages.ErrorEnum;
import org.kim.ingwerCity.messages.PrefixEnum;
import org.kim.ingwerCity.messages.SuccessfullEnum;
import org.kim.ingwerCity.messages.UsageEnum;
import org.kim.ingwerCity.objects.ICPlayer;

import java.util.List;

public class SetPlayerFactionCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(commandSender instanceof Player player)) {
            return false;
        }
        if(strings.length != 2) {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(UsageEnum.PLAYER_SET_FACTION_USAGE.getMessage()));
            return false;
        }
        OfflinePlayer targetPlayer = player.getServer().getOfflinePlayer(strings[0]);
        if(targetPlayer == null) {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(ErrorEnum.PLAYER_NOT_FOUND.getMessage(strings[0])));
            return false;
        }
        String factionName = strings[1];
        FactionMethods factionMethods = new FactionMethods();
        if(!factionMethods.isFactionAvaible(factionName)) {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(ErrorEnum.FACTION_NOT_FOUND.getMessage(factionName)));
            return false;
        }
        FactionObject factionObject = factionMethods.getFactionObject(factionName);
        int factionID = factionObject.getId();
        ICPlayer icPlayer = ICPlayer.IC_PLAYER_HASH_MAP.get(targetPlayer.getUniqueId());
        icPlayer.setFactionID(factionID);
        icPlayer.setFactionRank(0);
        player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(SuccessfullEnum.FACTION_SUCCESSFULL_SET.getMessage(targetPlayer.getName(), factionName)));
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(strings.length == 2) {
            return FactionObject.factionList.stream().map(FactionObject::getName).toList();
        }
        return null;
    }
}
