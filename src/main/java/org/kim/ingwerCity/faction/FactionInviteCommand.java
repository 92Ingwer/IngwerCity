package org.kim.ingwerCity.faction;

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
import org.kim.ingwerCity.services.PlayerService;

public class FactionInviteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(commandSender instanceof Player player)) {
            return false;
        }
        if(strings.length != 1) {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(UsageEnum.INVITE_FACTION_USAGE.getMessage()));
            return false;
        }
        FactionMethods factionMethods = new FactionMethods();
        if(strings[0].equals("accept")) {
            if(!PlayerService.playerFactionInviteMap.containsKey(player.getUniqueId())) {
                player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(ErrorEnum.NO_INVITE.getMessage()));
                return false;
            }
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(SuccessfullEnum.PLAYER_SUCCESSFULL_ACCEPTED.getMessage()));
            FactionObject factionObject = PlayerService.playerFactionInviteMap.get(player.getUniqueId());
            ICPlayer icPlayer = ICPlayer.IC_PLAYER_HASH_MAP.get(player.getUniqueId());
            icPlayer.setFactionID(factionObject.getId());
            icPlayer.setFactionRank(0);
            PlayerService.playerFactionInviteMap.remove(player.getUniqueId());
            factionMethods.sendFactionMessage(factionObject.getId(), PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(SuccessfullEnum.PLAYER_JOINED_FACTION.getMessage(player.getName())));
            return false;
        }
        ICPlayer icPlayer = ICPlayer.IC_PLAYER_HASH_MAP.get(player.getUniqueId());
        PlayerService playerService = new PlayerService();
        FactionObject factionObject = factionMethods.getPlayerFaction(icPlayer);
        if(factionObject == null) {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(ErrorEnum.NOT_IN_FACTION.getMessage()));
            return false;
        }
        if(icPlayer.getFactionRank() < 5) {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(ErrorEnum.NOT_LEADER.getMessage()));
            return false;
        }
        Player targetPlayer = player.getServer().getPlayer(strings[0]);
        if(targetPlayer == null) {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(ErrorEnum.PLAYER_NOT_FOUND.getMessage(strings[0])));
            return false;
        }
        ICPlayer targetICPlayer = ICPlayer.IC_PLAYER_HASH_MAP.get(targetPlayer.getUniqueId());
        FactionObject targetFaction = factionMethods.getPlayerFaction(targetICPlayer);
        if(targetFaction != null) {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(ErrorEnum.PLAYER_IS_ALREADY_IN_FACTION.getMessage(strings[0])));
            return false;
        }
        if(!playerService.isPlayerInNear(player, targetPlayer)) {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(ErrorEnum.PLAYER_NOT_NEAR.getMessage(strings[0])));
            return false;
        }
        player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(SuccessfullEnum.FACTION_SUCCESSFULL_INVITED.getMessage(strings[0], factionObject.getName())));
        targetPlayer.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(SuccessfullEnum.PLAYER_SUCCESSFULL_INVITED.getMessage(factionObject.getName())));
        PlayerService.playerFactionInviteMap.put(targetPlayer.getUniqueId(),factionObject);
        return false;
    }
}
