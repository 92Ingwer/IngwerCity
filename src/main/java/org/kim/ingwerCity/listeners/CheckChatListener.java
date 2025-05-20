package org.kim.ingwerCity.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kim.ingwerCity.chat.DistanceEnum;
import org.kim.ingwerCity.messages.ErrorEnum;
import org.kim.ingwerCity.messages.PrefixEnum;
import org.kim.ingwerCity.messages.SuccessfullEnum;
import org.kim.ingwerCity.money.MoneyMethods;
import org.kim.ingwerCity.objects.ICPlayer;
import org.kim.ingwerCity.services.BankService;

public class CheckChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncChatEvent event) {

        //IF PLAYER IS IN THE EINGABE OF BANK
        if(BankService.BANK_ACTION_HASH_MAP.containsKey(event.getPlayer())) {
            MoneyMethods moneyMethods = new MoneyMethods();
            event.setCancelled(true);
            Player player = event.getPlayer();
            ICPlayer icPlayer = ICPlayer.IC_PLAYER_HASH_MAP.get(player.getUniqueId());
            String action = BankService.BANK_ACTION_HASH_MAP.get(player);
            double money;
            String plainMessage = PlainTextComponentSerializer.plainText().serialize(event.message());
            BankService.BANK_ACTION_HASH_MAP.remove(event.getPlayer());
            if(!moneyMethods.isATMInNear(player.getLocation())) {
                player.sendMessage(PrefixEnum.BANK_PREFIX.getMessage().append(Component.space().append(ErrorEnum.ATM_NOT_NEAR.getMessage())));
                return;
            }
            try {
                money = Double.parseDouble(plainMessage);
            } catch (NumberFormatException e) {
                player.sendMessage(PrefixEnum.BANK_PREFIX.getMessage().append(Component.space().append(ErrorEnum.INVALID_NUMBERS.getMessage())));
                return;
            }
            if(money < 0) {
                player.sendMessage(PrefixEnum.BANK_PREFIX.getMessage().append(Component.space().append(ErrorEnum.INVALID_NUMBERS_NEGATIVE.getMessage())));
                return;
            }
            if(action.equals("withdraw")) {
                if(icPlayer.getBankmoney() < money) {
                    player.sendMessage(PrefixEnum.BANK_PREFIX.getMessage().append(Component.space().append(ErrorEnum.NOT_ENOUGH_MONEY.getMessage())));
                    return;
                }
                icPlayer.setBankmoney(icPlayer.getBankmoney() - money);
                icPlayer.setHandmoney(icPlayer.getHandmoney() + money);
                player.sendMessage(PrefixEnum.BANK_PREFIX.getMessage().append(Component.space().append(SuccessfullEnum.BANK_WITHDRAW_SUCCESSFULL.getMessage(money))));
            } else if (action.equals("deposit")) {
                if(icPlayer.getHandmoney() < money) {
                    player.sendMessage(PrefixEnum.BANK_PREFIX.getMessage().append(Component.space().append(ErrorEnum.NOT_ENOUGH_MONEY.getMessage())));
                    return;
                }
                icPlayer.setBankmoney(icPlayer.getBankmoney() + money);
                icPlayer.setHandmoney(icPlayer.getHandmoney() - money);
                player.sendMessage(PrefixEnum.BANK_PREFIX.getMessage().append(Component.space().append(SuccessfullEnum.BANK_DEPOSIT_SUCCESSFULL.getMessage(money))));
            }
        }
    }
}
