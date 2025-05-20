package org.kim.ingwerCity.faction;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.kim.ingwerCity.messages.ErrorEnum;
import org.kim.ingwerCity.messages.PrefixEnum;
import org.kim.ingwerCity.messages.UsageEnum;
import org.kim.ingwerCity.objects.ICPlayer;


public class FactionChatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player player)) {
            return false;
        }
        if (strings.length == 0) {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(UsageEnum.FACTION_CHAT_USAGE.getMessage()));
            return false;
        }
        ICPlayer icPlayer = ICPlayer.IC_PLAYER_HASH_MAP.get(player.getUniqueId());
        if (icPlayer.getFactionID() == 0) {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(ErrorEnum.NOT_IN_FACTION.getMessage()));
            return false;
        }
        FactionMethods factionMethods = new FactionMethods();
        FactionObject factionObject = factionMethods.getPlayerFaction(icPlayer);
        FChatenum fChatenum = FChatenum.getFChatEnum(factionObject.getChatDyeID());
        if (fChatenum == null) {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(Component.text("Meld dich bei einem Developer: ChatDye nicht gefunden")));
            return false;
        }
        Component message = PrefixEnum.FACTION_PREIFX.getMessage()
                .appendSpace()
                .append(MiniMessage.miniMessage().deserialize(
                        fChatenum.getColor1() + player.getName() + ": " + fChatenum.getColor2() + String.join(" ", strings)
                ));

        factionMethods.sendFactionMessage(icPlayer.getFactionID(), message);
        return false;
    }
}