package org.kim.ingwerCity.money;

import net.kyori.adventure.text.Component;
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

public class BankCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(commandSender instanceof Player player)) {
            return false;
        }
        MoneyMethods moneyMethods = new MoneyMethods();
        if(!moneyMethods.isATMInNear(player.getLocation())) {
            player.sendMessage(PrefixEnum.BANK_PREFIX.getMessage().append(Component.space().append(ErrorEnum.ATM_NOT_NEAR.getMessage())));
            return false;
        }
        ICPlayer icPlayer = ICPlayer.IC_PLAYER_HASH_MAP.get(player.getUniqueId());
        if(strings.length > 2 || strings.length == 1) {
            player.sendMessage(PrefixEnum.BANK_PREFIX.getMessage().append(Component.space().append(UsageEnum.BANK_USAGE.getMessage())));
            return false;
        }
        if(strings.length == 0) {
            new BankGUI(player).openInventory();
            return false;
        }
        double money;
        try {
            money = Double.parseDouble(strings[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(PrefixEnum.BANK_PREFIX.getMessage().append(Component.space().append(ErrorEnum.INVALID_NUMBERS.getMessage())));
            return false;
        }
        if(money < 0) {
            player.sendMessage(PrefixEnum.BANK_PREFIX.getMessage().append(Component.space().append(ErrorEnum.INVALID_NUMBERS_NEGATIVE.getMessage())));
            return false;
        }
        if(strings[0].equals("deposit")) {
            if(icPlayer.getHandmoney() < money) {
                player.sendMessage(PrefixEnum.BANK_PREFIX.getMessage().append(Component.space().append(ErrorEnum.NOT_ENOUGH_MONEY.getMessage())));
                return false;
            }
            icPlayer.setBankmoney(icPlayer.getBankmoney() + money);
            icPlayer.setHandmoney(icPlayer.getHandmoney() - money);
            player.sendMessage(PrefixEnum.BANK_PREFIX.getMessage().append(Component.space().append(SuccessfullEnum.BANK_DEPOSIT_SUCCESSFULL.getMessage(money))));
        } else if (strings[0].equals("withdraw")) {
            if(icPlayer.getBankmoney() < money) {
                player.sendMessage(PrefixEnum.BANK_PREFIX.getMessage().append(Component.space().append(ErrorEnum.NOT_ENOUGH_MONEY.getMessage())));
                return false;
            }
            icPlayer.setBankmoney(icPlayer.getBankmoney() - money);
            icPlayer.setHandmoney(icPlayer.getHandmoney() + money);
            player.sendMessage(PrefixEnum.BANK_PREFIX.getMessage().append(Component.space().append(SuccessfullEnum.BANK_WITHDRAW_SUCCESSFULL.getMessage(money))));
        } else {
            player.sendMessage(PrefixEnum.BANK_PREFIX.getMessage().append(Component.space().append(UsageEnum.BANK_USAGE.getMessage())));
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(strings.length == 1) {
            return List.of("deposit", "withdraw");
        }
        return null;
    }
}
