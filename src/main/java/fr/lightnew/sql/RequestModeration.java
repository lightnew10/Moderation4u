package fr.lightnew.sql;

import fr.lightnew.Moderation4u;
import fr.lightnew.commands.advance.BanEntity;
import fr.lightnew.commands.advance.TimeType;
import fr.lightnew.tools.ObjectLoad;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

public class RequestModeration {

    private static final Connection connection = Moderation4u.instance.getConnection();

    private static String baseReport(String table) {
        return "CREATE TABLE IF NOT EXISTS " + table + "(id int NOT NULL AUTO_INCREMENT," +
                "ban_id VARCHAR(50)," +
                "user_id VARCHAR(50)," +
                "by_user_id VARCHAR(50)," +
                "ban_reason VARCHAR(255)," +
                "time_amount int," +
                "time_type VARCHAR(50)," +
                "start_ban_date DATE," +
                "ban_date TIMESTAMP," +
                "PRIMARY KEY (id));";
    }

    public static void createDefaultsTables() {
        try {
            String use = "USE " + ObjectLoad.database + ";";
            String ban = baseReport("ban");
            String banperm = baseReport("ban_perm");
            String unbanlog = baseReport("unban_log");

            String mute = baseReport("mute");
            String unmute = baseReport("unmute_log");

            PreparedStatement statement_use = connection.prepareStatement(use);
            PreparedStatement statement_ban = connection.prepareStatement(ban);
            PreparedStatement statement_banperm = connection.prepareStatement(banperm);
            PreparedStatement statement_unban_log = connection.prepareStatement(unbanlog);

            PreparedStatement statement_mute = connection.prepareStatement(mute);
            PreparedStatement statement_unmute_log = connection.prepareStatement(unmute);
            statement_use.executeUpdate();
            statement_ban.executeUpdate();
            statement_banperm.executeUpdate();
            statement_unban_log.executeUpdate();
            statement_mute.executeUpdate();
            statement_unmute_log.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    *
    * BAN SECTION
    *
    */

    public static boolean isBan(UUID uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT ban_id FROM ban WHERE user_id=?");
            statement.setString(1, uuid.toString());
            ResultSet result = statement.executeQuery();
            return result.next();
        } catch (SQLException e) {e.printStackTrace();}
        return false;
    }

    public static BanEntity getBan(UUID uuid) {
        if (!isBan(uuid))
            return null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM ban WHERE user_id=?");
            statement.setString(1, uuid.toString());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new BanEntity(
                        UUID.fromString(result.getString("ban_id")),
                        UUID.fromString(result.getString("user_id")),
                        UUID.fromString(result.getString("by_user_id")),
                        result.getString("ban_reason"),
                        result.getInt("time_amount"),
                        TimeType.valueOf(result.getString("time_type")),
                        new Date(result.getDate("start_ban_date").getTime()),
                        new Date(result.getDate("ban_date").getTime())
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static void addBan(BanEntity ban) {
        try {
            PreparedStatement insert = connection.prepareStatement("INSERT INTO ban (ban_id, user_id, by_user_id, ban_reason, time_amount, time_type, start_ban_date, ban_date) values (?,?,?,?,?,?,?,?)");
            insert.setString(1, ban.getBanID().toString());
            insert.setString(2, ban.getPlayerBanned().toString());
            insert.setString(3, ban.getBannedByPlayer().toString());
            insert.setString(4, ban.getReason());
            insert.setInt(5, ban.getTime());
            insert.setString(6, ban.getTimeType().name());
            insert.setDate(7, new Date(ban.getStartBan().getTime()));
            insert.setTimestamp(8, new Timestamp(ban.getEndBan().getTime()));
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeBan(BanEntity ban) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM ban WHERE ban_id=?");
            statement.setString(1, ban.getBanID().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addUnBanLog(BanEntity ban) {
        try {
            PreparedStatement insert = connection.prepareStatement("INSERT INTO unban_log (ban_id, user_id, by_user_id, ban_reason, time_amount, time_type, start_ban_date, ban_date) values (?,?,?,?,?,?,?,?)");
            insert.setString(1, ban.getBanID().toString());
            insert.setString(2, ban.getPlayerBanned().toString());
            insert.setString(3, ban.getBannedByPlayer().toString());
            insert.setString(4, ban.getReason());
            insert.setInt(5, ban.getTime());
            insert.setString(6, ban.getTimeType().name());
            insert.setDate(7, new Date(ban.getStartBan().getTime()));
            insert.setTimestamp(8, new Timestamp(ban.getEndBan().getTime()));
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     *
     * MUTE SECTION
     *
     */


    public static boolean isMute(UUID uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT ban_id FROM mute WHERE user_id=?");
            statement.setString(1, uuid.toString());
            ResultSet result = statement.executeQuery();
            return result.next();
        } catch (SQLException e) {e.printStackTrace();}
        return false;
    }

    public static BanEntity getMute(UUID uuid) {
        if (!isBan(uuid))
            return null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM mute WHERE user_id=?");
            statement.setString(1, uuid.toString());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new BanEntity(
                        UUID.fromString(result.getString("ban_id")),
                        UUID.fromString(result.getString("user_id")),
                        UUID.fromString(result.getString("by_user_id")),
                        result.getString("ban_reason"),
                        result.getInt("time_amount"),
                        TimeType.valueOf(result.getString("time_type")),
                        new Date(result.getDate("start_ban_date").getTime()),
                        new Date(result.getDate("ban_date").getTime())
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static void addMute(BanEntity ban) {
        try {
            PreparedStatement insert = connection.prepareStatement("INSERT INTO mute (ban_id, user_id, by_user_id, ban_reason, time_amount, time_type, start_ban_date, ban_date) values (?,?,?,?,?,?,?,?)");
            insert.setString(1, ban.getBanID().toString());
            insert.setString(2, ban.getPlayerBanned().toString());
            insert.setString(3, ban.getBannedByPlayer().toString());
            insert.setString(4, ban.getReason());
            insert.setInt(5, ban.getTime());
            insert.setString(6, ban.getTimeType().name());
            insert.setDate(7, new Date(ban.getStartBan().getTime()));
            insert.setTimestamp(8, new Timestamp(ban.getEndBan().getTime()));
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeMute(BanEntity ban) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM mute WHERE ban_id=?");
            statement.setString(1, ban.getBanID().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addUnMuteLog(BanEntity ban) {
        try {
            PreparedStatement insert = connection.prepareStatement("INSERT INTO unmute_log (ban_id, user_id, by_user_id, ban_reason, time_amount, time_type, start_ban_date, ban_date) values (?,?,?,?,?,?,?,?)");
            insert.setString(1, ban.getBanID().toString());
            insert.setString(2, ban.getPlayerBanned().toString());
            insert.setString(3, ban.getBannedByPlayer().toString());
            insert.setString(4, ban.getReason());
            insert.setInt(5, ban.getTime());
            insert.setString(6, ban.getTimeType().name());
            insert.setDate(7, new Date(ban.getStartBan().getTime()));
            insert.setTimestamp(8, new Timestamp(ban.getEndBan().getTime()));
            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static HashMap<UUID, BanEntity> getMutedPlayers() {
        HashMap<UUID, BanEntity> resultList = new HashMap<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM mute");
            ResultSet result = statement.executeQuery();
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "===========================");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Chargement des joueurs mute...");
            while (result.next()) {
                BanEntity ban = new BanEntity(
                        UUID.fromString(result.getString("ban_id")),
                        UUID.fromString(result.getString("user_id")),
                        UUID.fromString(result.getString("by_user_id")),
                        result.getString("ban_reason"),
                        result.getInt("time_amount"),
                        TimeType.valueOf(result.getString("time_type")),
                        new Date(result.getDate("start_ban_date").getTime()),
                        new Date(result.getDate("ban_date").getTime())
                );
                resultList.put(ban.getPlayerBanned(), ban);
            }

            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "TOUT LES JOUEURS MUTE SONT CHARGER ! ");
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "===========================");
            return resultList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
