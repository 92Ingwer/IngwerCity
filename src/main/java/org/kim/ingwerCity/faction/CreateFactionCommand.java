package org.kim.ingwerCity.faction;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.kim.ingwerCity.IngwerCity;
import org.kim.ingwerCity.database.SQLMethods;
import org.kim.ingwerCity.messages.ErrorEnum;
import org.kim.ingwerCity.messages.PrefixEnum;
import org.kim.ingwerCity.messages.SuccessfullEnum;
import org.kim.ingwerCity.messages.UsageEnum;
import org.kim.ingwerCity.services.PlayerService;

import java.util.concurrent.CompletableFuture;

public class CreateFactionCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(commandSender instanceof Player player)) {
            return false;
        }
        IngwerCity plugin = IngwerCity.getInstance();
        if (strings.length != 5) {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(UsageEnum.FACTION_CREATE_USAGE.getMessage()));
            return false;
        }
        String name = strings[0];
        String type = strings[1];
        double spawnX;
        double spawnY;
        double spawnZ;
        try {
            spawnX = Double.parseDouble(strings[2]);
            spawnY = Double.parseDouble(strings[3]);
            spawnZ = Double.parseDouble(strings[4]);
        } catch (NumberFormatException e) {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(ErrorEnum.INVALID_NUMBERS.getMessage()));
            return false;
        }
        SQLMethods sqlMethods = new SQLMethods();
        CompletableFuture.runAsync(() -> {
            sqlMethods.insertFaction(name, type, spawnX, spawnY, spawnZ);
            FactionObject factionObject = new FactionObject(sqlMethods.getFactionID(name), name, type, spawnX, spawnY, spawnZ,0,1);
            FactionObject.factionList.add(factionObject);
        }).thenRun(() -> Bukkit.getScheduler().runTask(plugin, () -> {
            player.sendMessage(PrefixEnum.FACTION_PREIFX.getMessage().appendSpace().append(SuccessfullEnum.FACTON_SUCCESSFULL_CREATE.getMessage(name)));
        }));
        return false;
    }
}
