package org.kim.ingwerCity.database;

import org.bukkit.Bukkit;
import org.kim.ingwerCity.IngwerCity;
import org.kim.ingwerCity.faction.FactionObject;
import org.kim.ingwerCity.jobs.JobEnum;
import org.kim.ingwerCity.jobs.JobInterface;
import org.kim.ingwerCity.jobs.JobService;
import org.kim.ingwerCity.objects.ICPlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import static org.kim.ingwerCity.IngwerCity.sql;

public class SQLMethods {

    private final String CREATE_PUNISHMENT_TABLE = "CREATE TABLE IF NOT EXISTS punishment (uuid VARCHAR(36), type VARCHAR(30), reason VARCHAR(100), until VARCHAR(150))";
    private final String CREATE_PLAYER_TABLE = "CREATE TABLE IF NOT EXISTS player (uuid VARCHAR(36) PRIMARY KEY, playerid INT AUTO_INCREMENT UNIQUE, level INT DEFAULT 0, xp INT DEFAULT 0, payday DOUBLE DEFAULT 0, handmoney DOUBLE(12,2) DEFAULT 0.00, bankmoney DOUBLE(15,2) DEFAULT 0.00, factionID INT DEFAULT null, factionRank INT DEFAULT null)";
    private final String CREATE_FACTION_TABLE = "CREATE TABLE IF NOT EXISTS faction (factionID INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) UNIQUE, factionType VARCHAR(30), factionbank DOUBLE(15,2) DEFAULT 0.00, spawnX DOUBLE(12,2), spawnY DOUBLE(12,2), spawnZ DOUBLE(12,2), chatDyeID INT DEFAULT 1)";
    private final String CREATE_JOBS_TABLE = "CREATE TABLE IF NOT EXISTS job (jobID INT PRIMARY KEY, name VARCHAR(50) UNIQUE)";
    private final String CREATE_PLAYER_JOBS_TABLE = "CREATE TABLE IF NOT EXISTS player_job (uuid VARCHAR(36), jobID INT, jobLevel INT DEFAULT 0, jobXP INT DEFAULT 0, PRIMARY KEY (uuid, jobID), FOREIGN KEY (uuid) REFERENCES player(uuid) ON DELETE CASCADE, FOREIGN KEY (jobID) REFERENCES job(jobID) ON DELETE CASCADE)";
    private final String SELECT_PLAYER_BY_UUID = "SELECT * FROM player WHERE uuid = ?";
    private final String SELECT_FACTIONID_BY_NAME = "SELECT factionID FROM faction WHERE name = ?";
    private final String SELECT_ALL_FACTIONS = "SELECT * FROM faction";
    private final String INSERT_PLAYER = "INSERT INTO player (uuid) VALUES (?)";
    private final String SELECT_PUNISHMENT_BY_UUID = "SELECT until FROM punishment WHERE uuid = ?";
    private final String INSERT_PUNISHMENT = "INSERT INTO punishment (uuid, type, reason, until) VALUES (?, ?, ?, ?)";
    private final String SELECT_REASON_BY_UUID = "SELECT reason FROM punishment WHERE uuid = ?";
    private final String DELETE_PUNISHMENT_BY_UUID = "DELETE FROM punishment WHERE uuid = ?";
    private final String INSERT_FACTION = "INSERT INTO faction (name, factionType, spawnX, spawnY, spawnZ) VALUES (?, ?, ?, ?, ?)";
    private final String SELECT_FACTIONID_BY_UUID = "SELECT factionID FROM player WHERE uuid = ?";
    private final String UNINVITE_PLAYER = "UPDATE player SET factionID = 0, factionRank = 0 WHERE uuid = ?";

    public void create() {
        IngwerCity.getSql().update(CREATE_PUNISHMENT_TABLE);
        IngwerCity.getSql().update(CREATE_PLAYER_TABLE);
        IngwerCity.getSql().update(CREATE_FACTION_TABLE);
        IngwerCity.getSql().update(CREATE_JOBS_TABLE);
        IngwerCity.getSql().update(CREATE_PLAYER_JOBS_TABLE);
    }

    public CompletableFuture<Boolean> userExists(UUID uuid) {
        try (PreparedStatement ps = IngwerCity.getSql().getCon().prepareStatement(SELECT_PLAYER_BY_UUID)) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                return CompletableFuture.completedFuture(rs.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(false);
    }

    public void insertUser(UUID uuid) {
        try (PreparedStatement ps = IngwerCity.getSql().getCon().prepareStatement(INSERT_PLAYER)) {
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (JobEnum jobEnum : JobEnum.values()) {
            try (PreparedStatement ps = IngwerCity.getSql().getCon().prepareStatement("INSERT INTO player_job (uuid, jobID) VALUES (?, ?)")) {
                ps.setString(1, uuid.toString());
                ps.setInt(2, jobEnum.getJobID());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertAllJobs() {
        for (JobEnum job : JobEnum.values()) {
            if (!jobExists(job.getJobID())) {
                try (PreparedStatement ps = IngwerCity.getSql().getCon().prepareStatement("INSERT INTO job (jobID, name) VALUES (?, ?)")) {
                    ps.setInt(1, job.getJobID());
                    ps.setString(2, job.getName());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean jobExists(int jobID) {
        try (PreparedStatement ps = IngwerCity.getSql().getCon().prepareStatement("SELECT jobID FROM job WHERE jobID = ? ")) {
            ps.setInt(1, jobID);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public CompletableFuture<List<JobInterface>> getJobs(UUID uuid) {
        List<JobInterface> jobs = new ArrayList<>();
        for (JobEnum jobEnum : JobEnum.values()) {
            try (PreparedStatement ps = IngwerCity.getSql().getCon().prepareStatement("SELECT * FROM player_job WHERE uuid = ? AND jobID = ?")) {
                ps.setString(1, uuid.toString());
                ps.setInt(2, jobEnum.getJobID());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int jobLevel = rs.getInt("jobLevel");
                    int jobXP = rs.getInt("jobXP");
                    JobInterface job = jobEnum.create(jobLevel, jobXP, Bukkit.getPlayer(uuid));
                    jobs.add(job);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return CompletableFuture.completedFuture(jobs);
    }
    public void updateNewJobPlayer(UUID uuid) {
        for (JobEnum jobEnum : JobEnum.values()) {
            try (PreparedStatement ps = IngwerCity.getSql().getCon().prepareStatement("SELECT * FROM player_job WHERE uuid = ? AND jobID = ?")) {
                ps.setString(1, uuid.toString());
                ps.setInt(2, jobEnum.getJobID());
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    insertPlayerJob(uuid, jobEnum.getJobID());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void insertPlayerJob(UUID uuid, int jobID) {
        try (PreparedStatement ps = IngwerCity.getSql().getCon().prepareStatement("INSERT INTO player_job (uuid, jobID) VALUES (?, ?)")) {
            ps.setString(1, uuid.toString());
            ps.setInt(2, jobID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertFaction(String name, String type, double spawnX, double spawnY, double spawnZ) {
        try (PreparedStatement ps = IngwerCity.getSql().getCon().prepareStatement(INSERT_FACTION)) {
            ps.setString(1, name);
            ps.setString(2, type);
            ps.setDouble(3, spawnX);
            ps.setDouble(4, spawnY);
            ps.setDouble(5, spawnZ);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long getDate(UUID uuid) {
        try (PreparedStatement ps = IngwerCity.getSql().getCon().prepareStatement(SELECT_PUNISHMENT_BY_UUID)) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("until");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void punishPlayer(UUID uuid, String type, String reason, long until) {
        try (PreparedStatement ps = IngwerCity.getSql().getCon().prepareStatement(INSERT_PUNISHMENT)) {
            ps.setString(1, uuid.toString());
            ps.setString(2, type);
            ps.setString(3, reason);
            ps.setLong(4, until);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlayerBanned(UUID uuid) {
        try (PreparedStatement ps = IngwerCity.getSql().getCon().prepareStatement(SELECT_PUNISHMENT_BY_UUID)) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("until") > System.currentTimeMillis();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getReason(UUID uuid) {
        try (PreparedStatement ps = IngwerCity.getSql().getCon().prepareStatement(SELECT_REASON_BY_UUID)) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("reason");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deletePlayer(UUID uuid) {
        try (PreparedStatement ps = IngwerCity.getSql().getCon().prepareStatement(DELETE_PUNISHMENT_BY_UUID)) {
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void savePlayerData(UUID uuid) {
        ICPlayer icPlayer = ICPlayer.IC_PLAYER_HASH_MAP.get(uuid);
        savePlayerJobs(uuid);
        if (icPlayer != null) {
            try (PreparedStatement ps = IngwerCity.getSql().getCon().prepareStatement("UPDATE player SET level = ?, xp = ?, payday = ?, handmoney = ?, bankmoney = ?, factionID = ?, factionRank = ? WHERE uuid = ?")) {
                ps.setInt(1, icPlayer.getLevel());
                ps.setInt(2, icPlayer.getXp());
                ps.setDouble(3, icPlayer.getPayday());
                ps.setDouble(4, icPlayer.getHandmoney());
                ps.setDouble(5, icPlayer.getBankmoney());
                ps.setInt(6, icPlayer.getFactionID());
                ps.setInt(7, icPlayer.getFactionRank());
                ps.setString(8, uuid.toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void savePlayerJobs(UUID uuid) {
        List<JobInterface> jobList = JobService.playerJobsListMap.get(uuid);
        JobService jobService = new JobService();
        if (jobList != null) {
            for (JobInterface job : jobList) {
                try (PreparedStatement ps = IngwerCity.getSql().getCon().prepareStatement("UPDATE player_job SET jobLevel = ?, jobXP = ? WHERE uuid = ? AND jobID = ?")) {
                    ps.setInt(1, job.getLevel());
                    ps.setInt(2, job.getXP());
                    ps.setString(3, uuid.toString());
                    ps.setInt(4, jobService.getJobID(job.getJobName()));
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int getFactionID(String name) {
        try (PreparedStatement ps = IngwerCity.getSql().getCon().prepareStatement(SELECT_FACTIONID_BY_NAME)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("factionID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void initFactions() {
        try (PreparedStatement ps = IngwerCity.getSql().getCon().prepareStatement(SELECT_ALL_FACTIONS)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int factionID = rs.getInt("factionID");
                String name = rs.getString("name");
                String type = rs.getString("factionType");
                double spawnX = rs.getDouble("spawnX");
                double spawnY = rs.getDouble("spawnY");
                double spawnZ = rs.getDouble("spawnZ");
                double bank = rs.getDouble("factionbank");
                int chatDyeID = rs.getInt("chatDyeID");
                FactionObject factionObject = new FactionObject(factionID, name, type, spawnX, spawnY, spawnZ, bank, chatDyeID);
                FactionObject.factionList.add(factionObject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CompletableFuture<Integer> getPlayerFactionID(UUID uuid) {
        try (PreparedStatement ps = IngwerCity.getSql().getCon().prepareStatement(SELECT_FACTIONID_BY_UUID)) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int factionID = rs.getInt("factionID");
                return CompletableFuture.completedFuture(factionID);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture<ICPlayer> getICPlayer(UUID uuid) {
        try (PreparedStatement stmt = IngwerCity.getSql().getCon().prepareStatement(SELECT_PLAYER_BY_UUID)) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return CompletableFuture.completedFuture(new ICPlayer(uuid,
                        Bukkit.getPlayer(uuid),
                        rs.getInt("playerid"),
                        rs.getInt("level"),
                        rs.getInt("payday"),
                        rs.getInt("xp"),
                        rs.getDouble("handmoney"),
                        rs.getDouble("bankmoney"),
                        rs.getInt("factionID"),
                        rs.getInt("factionRank")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateChatDye(int factionID, int chatDyeID) {
        try (PreparedStatement ps = IngwerCity.getSql().getCon().prepareStatement("UPDATE faction SET chatDyeID = ? WHERE factionID = ?")) {
            ps.setInt(1, chatDyeID);
            ps.setInt(2, factionID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void uninvitePlayer(UUID uuid) {
        try (PreparedStatement ps = IngwerCity.getSql().getCon().prepareStatement(UNINVITE_PLAYER)) {
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CompletableFuture<List<UUID>> getFactionMembers(int factionID) {
        return CompletableFuture.supplyAsync(() -> {
            List<UUID> members = new ArrayList<>();
            try (PreparedStatement ps = IngwerCity.getSql().getCon().prepareStatement("SELECT uuid FROM player WHERE factionID = ?")) {
                ps.setInt(1, factionID);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    members.add(UUID.fromString(rs.getString("uuid")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return members;
        });
    }
}