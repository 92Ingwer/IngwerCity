package org.kim.ingwerCity.objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
public class ICPlayer {
    private UUID uuid;
    private Player player;
    private int playerid;
    private int level;
    private double payday;
    private int xp;
    private double handmoney;
    private double bankmoney;
    private Integer factionID;
    private Integer factionRank;
    public static final HashMap<UUID, ICPlayer> IC_PLAYER_HASH_MAP = new HashMap<>();

    public ICPlayer(UUID uuid, Player player, int playerid, int level, double payday, int xp, double handmoney, double bankmoney, Integer factionID, Integer factionRank) {
        this.uuid = uuid;
        this.player = player;
        this.playerid = playerid;
        this.level = level;
        this.payday = payday;
        this.xp = xp;
        this.handmoney = handmoney;
        this.bankmoney = bankmoney;
        this.factionID = factionID;
        this.factionRank = factionRank;
    }
    public ICPlayer(Player player) {
        this.player = player;
        this.playerid = 0;
        this.level = 0;
        this.payday = 0;
        this.xp = 0;
        this.handmoney = 0.00;
        this.bankmoney = 0.00;
        this.factionID = 0;
        this.factionRank = 0;
    }

}
