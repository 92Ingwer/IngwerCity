package org.kim.ingwerCity.faction;

import com.google.common.base.Strings;
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
import org.kim.ingwerCity.money.MoneyMethods;
import org.kim.ingwerCity.objects.ICPlayer;

import java.util.List;
import java.util.UUID;

public class FactionBankCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(commandSender instanceof Player player)) {
            return false;
        }
        if(strings.length != 2 || (!strings[0].equals("deposit") && !strings[0].equals("withdraw"))) {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(UsageEnum.FACTION_BANK_USAGE.getMessage()));
            return false;
        }
        FactionMethods factionMethods = new FactionMethods();
        MoneyMethods moneyMethods = new MoneyMethods();
        if(!moneyMethods.isATMInNear(player.getLocation())) {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(ErrorEnum.ATM_NOT_NEAR.getMessage()));
            return false;
        }
        UUID playerUUID = player.getUniqueId();
        ICPlayer icPlayer = ICPlayer.IC_PLAYER_HASH_MAP.get(playerUUID);
        int factionID = icPlayer.getFactionID();
        if(factionID == 0) {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(ErrorEnum.NOT_IN_FACTION.getMessage()));
            return false;
        }
        FactionObject factionObject = factionMethods.getPlayerFaction(icPlayer);
        double amount;
        try {
            amount = Double.parseDouble(strings[1]);
        } catch (Exception e) {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(ErrorEnum.INVALID_NUMBERS.getMessage()));
            return false;
        }
        if(amount == 0) {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(ErrorEnum.INVALID_NUMBERS.getMessage()));
            return false;
        }
        if(amount < 0) {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(ErrorEnum.INVALID_NUMBERS_NEGATIVE.getMessage()));
            return false;
        }
        double fbank = factionObject.getBank();
        double handmoney = icPlayer.getHandmoney();
        if(strings[0].equals("withdraw")) {
            if(icPlayer.getFactionRank() > 5) {
                player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(ErrorEnum.NOT_LEADER.getMessage()));
                return false;
            }
            if(fbank < amount) {
                player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(ErrorEnum.FACTION_NOT_ENOUGH_MONEY.getMessage()));
                return false;
            }
            fbank -= amount;
            factionObject.setBank(fbank);
            factionMethods.sendFactionMessage(factionID, PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(SuccessfullEnum.FACTION_BANK_SUCCESSFULL_WITHDRAWN.getMessage(player.getName(), amount,fbank)));
            handmoney += amount;
            icPlayer.setHandmoney(handmoney);
        } else if (strings[0].equals("deposit")) {
            if(handmoney < amount) {
                player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(ErrorEnum.NOT_ENOUGH_MONEY.getMessage()));
                return false;
            }
            handmoney -= amount;
            icPlayer.setHandmoney(handmoney);
            fbank += amount;
            factionObject.setBank(fbank);
            factionMethods.sendFactionMessage(factionID, PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(SuccessfullEnum.FACTION_BANK_SUCCESSFULL_DEPOSIT.getMessage(player.getName(), amount,fbank)));
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        List<String> tabList = List.of("deposit", "withdraw");
        if(strings.length == 1) {
            return tabList;
        }
        return null;
    }
}
