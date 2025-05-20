package org.kim.ingwerCity;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.kim.ingwerCity.commands.JobCommand;
import org.kim.ingwerCity.commands.QuitJobCommand;
import org.kim.ingwerCity.commands.StartJobCommand;
import org.kim.ingwerCity.commands.StatsCommand;
import org.kim.ingwerCity.database.SQL;
import org.kim.ingwerCity.database.SQLMethods;
import org.kim.ingwerCity.faction.*;
import org.kim.ingwerCity.listeners.*;
import org.kim.ingwerCity.money.BankCommand;
import org.kim.ingwerCity.money.MoneyMethods;
import org.kim.ingwerCity.navigator.NavigatorCommand;
import org.kim.ingwerCity.punishment.PunishCommand;
import org.kim.ingwerCity.punishment.UnbanCommand;
import org.kim.ingwerCity.schedulers.PayDayScheduler;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public final class IngwerCity extends JavaPlugin {
    @Getter
    public static IngwerCity instance;
    @Getter
    public static SQL sql;
    @Override
    public void onEnable() {
        instance = this;
        initdb();
        initATMs();
        initFaction();
        startPayDayScheduler();
        insertDB();
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new OnLoginListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new OnJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new CheckChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new OnQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new DropListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractEvent(), this);
        Objects.requireNonNull(this.getCommand("punish")).setExecutor(new PunishCommand());
        Objects.requireNonNull(this.getCommand("punish")).setTabCompleter(new PunishCommand());
        Objects.requireNonNull(this.getCommand("unban")).setExecutor(new UnbanCommand());
        Objects.requireNonNull(this.getCommand("navi")).setExecutor(new NavigatorCommand());
        Objects.requireNonNull(this.getCommand("bank")).setExecutor(new BankCommand());
        Objects.requireNonNull(this.getCommand("bank")).setTabCompleter(new BankCommand());
        Objects.requireNonNull(this.getCommand("fcreate")).setExecutor(new CreateFactionCommand());
        Objects.requireNonNull(this.getCommand("fset")).setExecutor(new SetPlayerFactionCommand());
        Objects.requireNonNull(this.getCommand("fset")).setTabCompleter(new SetPlayerFactionCommand());
        Objects.requireNonNull(this.getCommand("finvite")).setExecutor(new FactionInviteCommand());
        Objects.requireNonNull(this.getCommand("f")).setExecutor(new FactionChatCommand());
        Objects.requireNonNull(this.getCommand("fbank")).setExecutor(new FactionBankCommand());
        Objects.requireNonNull(this.getCommand("fbank")).setTabCompleter(new FactionBankCommand());
        Objects.requireNonNull(this.getCommand("funinvite")).setExecutor(new FactionUninviteCommand());
        Objects.requireNonNull(this.getCommand("setrank")).setExecutor(new SetRankCommand());
        Objects.requireNonNull(this.getCommand("stats")).setExecutor(new StatsCommand());
        Objects.requireNonNull(this.getCommand("fsettings")).setExecutor(new FactionSettingsCommand());
        Objects.requireNonNull(this.getCommand("job")).setExecutor(new JobCommand());
        Objects.requireNonNull(this.getCommand("quitjob")).setExecutor(new QuitJobCommand());
        Objects.requireNonNull(this.getCommand("startjob")).setExecutor(new StartJobCommand());
        //TODO: weitere Minigames einbauen für Elektriker
    }

    @Override
    public void onDisable() {

    }
    private void initdb() {
        SQLMethods sqlMethods = new SQLMethods();
        //
        //
        sqlMethods.create();
    }
    private void initATMs() {
        MoneyMethods moneyMethods = new MoneyMethods();
        moneyMethods.initATMS();
    }
    private void initFaction() {
        SQLMethods sqlMethods = new SQLMethods();
        sqlMethods.initFactions();
    }
    private void startPayDayScheduler() {
        PayDayScheduler payDayScheduler = new PayDayScheduler();
        payDayScheduler.startScheduler(this);
    }
    private void insertDB() {
        SQLMethods sqlMethods = new SQLMethods();
        CompletableFuture.runAsync(sqlMethods::insertAllJobs);
    }
}
