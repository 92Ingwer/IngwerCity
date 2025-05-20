package org.kim.ingwerCity.faction;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.kim.ingwerCity.database.SQLMethods;
import org.kim.ingwerCity.messages.ErrorEnum;
import org.kim.ingwerCity.messages.PrefixEnum;
import org.kim.ingwerCity.messages.UsageEnum;
import org.kim.ingwerCity.objects.ICPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FactionUninviteCommand implements CommandExecutor {
    public static Map<UUID, UUID> uninviteMap = new HashMap<>();
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(commandSender instanceof Player player)) {
            return false;
        }
        if(strings.length != 1) {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(UsageEnum.FACTION_UNINVITE_USAGE.getMessage()));
            return false;
        }
        ICPlayer icPlayer = ICPlayer.IC_PLAYER_HASH_MAP.get(player.getUniqueId());
        int factionID = icPlayer.getFactionID();
        if(factionID == 0) {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(ErrorEnum.NOT_IN_FACTION.getMessage()));
        } else {
            int factionRank = icPlayer.getFactionRank();
            if (factionRank < 5) {
                player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(ErrorEnum.NOT_LEADER.getMessage()));
                return false;
            }
            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(strings[0]);
            if(!targetPlayer.hasPlayedBefore() && !targetPlayer.isOnline()) {
                player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace()
                        .append(ErrorEnum.PLAYER_NOT_FOUND.getMessage()));
                return false;
            }
            UUID targetUUID = targetPlayer.getUniqueId();
            SQLMethods sqlMethods = new SQLMethods();
            sqlMethods.getPlayerFactionID(targetUUID).thenAccept(factionIDTarget -> {
                if (factionID != factionIDTarget) {
                    player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace()
                            .append(ErrorEnum.PLAYER_NOT_YOUR_FACTION.getMessage(strings[0])));
                } else {
                    uninviteMap.put(player.getUniqueId(), targetUUID);
                    ConfirmUninviteGUI confirmUninviteGUI = new ConfirmUninviteGUI();
                    confirmUninviteGUI.openInventory(player);
                }
            }).exceptionally(ex -> {
                player.sendMessage("Ein Fehler ist aufgetreten: " + ex.getMessage());
                ex.printStackTrace();
                return null;
            });
        }

        return false;
    }
}
