package fr.lightnew.sql;

import fr.lightnew.Moderation4u;
import fr.lightnew.Survival;
import fr.lightnew.tools.PlayerUUID;
import fr.lightnew.constructor.ReportEntity;
import fr.lightnew.constructor.ReportStatut;
import fr.lightnew.tools.ObjectLoad;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Requests {

    private static final Connection connection = Moderation4u.instance.getConnection();

    private static void log(String s) {
        Moderation4u.log(ChatColor.YELLOW + "===========[Information]==========");
        Moderation4u.log(ChatColor.GRAY + "[" + ChatColor.YELLOW + "Moderation4u" + ChatColor.GRAY + "] " + s);
        Moderation4u.log(ChatColor.YELLOW + "===========[Information]==========");
    }

    public static void createDatabase() {
        try {
            String data =
                    "CREATE DATABASE IF NOT EXISTS " + ObjectLoad.database;
            PreparedStatement statement1 = connection.prepareStatement(data);
            statement1.executeUpdate();
            log(ChatColor.GREEN + "Database \"" + ObjectLoad.database + "\" is created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String baseReport(String table) {
        return "CREATE TABLE IF NOT EXISTS " + table + "(id int NOT NULL AUTO_INCREMENT," +
                "report_id VARCHAR(50)," +
                "owner_name VARCHAR(50)," +
                "owner_uuid VARCHAR(50)," +
                "target_name VARCHAR(50)," +
                "target_uuid VARCHAR(50)," +
                "reason TEXT," +
                "date_creation date," +
                "report_state VARCHAR(50)," +
                "takeby_name VARCHAR(50)," +
                "takeby_uuid VARCHAR(50)," +
                "dateTakeBy date," +
                "dateEndReport date," +
                "PRIMARY KEY (id));";
    }

    public static void createDefaultsTables() {
        try {
            String use = "USE " + ObjectLoad.database + ";";
            String table = baseReport("report");
            String table_2 = baseReport("report_finish");

            PreparedStatement statement = connection.prepareStatement(use);
            PreparedStatement statement2 = connection.prepareStatement(table);
            PreparedStatement statement3 = connection.prepareStatement(table_2);
            statement.executeUpdate();
            statement2.executeUpdate();
            statement3.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ReportEntity getReport(UUID id) {
        try {
            PreparedStatement statement = Survival.instance.getConnection().prepareStatement("SELECT * FROM report WHERE report_id=?");
            statement.setString(1, id.toString());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new ReportEntity(UUID.fromString(result.getString("report_id")), result.getString("owner_name"), result.getString("target_name"), result.getString("reason"), result.getDate("date_creation"), ReportStatut.valueOf(result.getString("report_state")), result.getString("takeby_name"), result.getDate("dateTakeBy"), result.getDate("dateEndReport"));

            } else
                return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<ReportEntity> getReports() {
        List<ReportEntity> list = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM report");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                ReportEntity report = new ReportEntity(UUID.fromString(result.getString("report_id")), result.getString("owner_name"), result.getString("target_name"), result.getString("reason"), result.getDate("date_creation"), ReportStatut.valueOf(result.getString("report_state")), result.getString("takeby_name"), result.getDate("dateTakeBy"), result.getDate("dateEndReport"));
                list.add(report);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static void updateReport(ReportEntity report) {
        try {
            PreparedStatement insert = connection.prepareStatement("UPDATE report SET report_id=?, owner_name=?, owner_uuid=?, target_name=?, target_uuid=?, reason=?, date_creation=?, report_state=?, takeby_name=?, takeby_uuid=?, dateTakeBy=?, dateEndReport=? WHERE report_id=?");
            insert.setString(1, report.getId().toString());
            insert.setString(2, report.getOwner());
            insert.setString(3, PlayerUUID.uuidWithName(report.getOwner()).toString());
            insert.setString(4, report.getTarget());
            insert.setString(5, PlayerUUID.uuidWithName(report.getTarget()).toString());
            insert.setString(6, report.getReason());
            insert.setDate(7, new java.sql.Date(report.getDate_creation().getTime()));
            insert.setString(8, report.getStatut().name());
            insert.setString(9, report.getTakeBy());
            insert.setString(10, PlayerUUID.uuidWithName(report.getTakeBy()).toString());
            if (report.getDateTakeBy() != null)
                insert.setDate(11, new java.sql.Date(report.getDateTakeBy().getTime()));
            else insert.setDate(11, null);
            if (report.getEndReport() != null)
                insert.setDate(12, new java.sql.Date(report.getEndReport().getTime()));
            else insert.setDate(12,null);

            insert.setString(13, report.getId().toString());

            insert.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeReport(ReportEntity report) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM report WHERE report_id=?");
            statement.setString(1, report.getId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeReportFinish(ReportEntity report) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM report_finish WHERE report_id=?");
            statement.setString(1, report.getId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void moveReportToFinish(ReportEntity report) {
        addReportFinish(report);
        removeReport(report);
    }

    private static void addReport(ReportEntity report, String table) {
        try {
            PreparedStatement insert = connection.prepareStatement("INSERT INTO " + table + " (report_id, owner_name, owner_uuid, target_name, target_uuid, reason, date_creation, report_state, takeby_name, takeby_uuid, dateTakeBy, dateEndReport) values (?,?,?,?,?,?,?,?,?,?,?,?)");
            insert.setString(1, report.getId().toString());
            insert.setString(2, report.getOwner());
            insert.setString(3, PlayerUUID.uuidWithName(report.getOwner()).toString());
            insert.setString(4, report.getTarget());
            insert.setString(5, PlayerUUID.uuidWithName(report.getTarget()).toString());
            insert.setString(6, report.getReason());
            insert.setDate(7, new java.sql.Date(report.getDate_creation().getTime()));
            insert.setString(8, report.getStatut().name());
            insert.setString(9, report.getTakeBy());
            insert.setString(10, PlayerUUID.uuidWithName(report.getTakeBy()).toString());
            if (report.getDateTakeBy() != null)
                insert.setDate(11, new java.sql.Date(report.getDateTakeBy().getTime()));
            else insert.setDate(11, null);
            if (report.getEndReport() != null)
                insert.setDate(12, new java.sql.Date(report.getEndReport().getTime()));
            else insert.setDate(12,null);

            insert.executeUpdate();
        } catch (SQLException e) {e.printStackTrace();}
    }

    public static void addReport(ReportEntity report) {
        addReport(report, "report");
    }

    public static void addReportFinish(ReportEntity report) {
        addReport(report, "report_finish");
    }

}
