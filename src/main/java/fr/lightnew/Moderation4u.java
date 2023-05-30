package fr.lightnew;

import fr.lightnew.commands.*;
import fr.lightnew.commands.advance.Ban;
import fr.lightnew.commands.advance.Mute;
import fr.lightnew.commands.advance.UnBan;
import fr.lightnew.commands.advance.UnMute;
import fr.lightnew.constructor.ReportEntity;
import fr.lightnew.constructor.ReportStatut;
import fr.lightnew.events.ModerationEvent;
import fr.lightnew.events.PlayerEvent;
import fr.lightnew.events.SanctionEvent;
import fr.lightnew.sql.RequestModeration;
import fr.lightnew.sql.Requests;
import fr.lightnew.tools.ObjectLoad;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Moderation4u extends JavaPlugin {

    public static Moderation4u instance;
    private Connection connection;
    public static String nameServer = "JobLife";

    @Override
    public void onEnable() {
        // Start logic
        instance = this;
        saveDefaultConfig();
        ObjectLoad.init();
        log(ChatColor.YELLOW + "===========[Information]==========");
        log(ChatColor.GRAY + "[" + ChatColor.YELLOW + "Moderation4u" + ChatColor.GRAY + "] " + ChatColor.GREEN + "Plugin enabling");
        log(ChatColor.YELLOW + "===========[Information]==========");
        // Events
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new PlayerEvent(), this);
        manager.registerEvents(new ModerationEvent(), this);
        manager.registerEvents(new SanctionEvent(), this);
        // Commands
        getCommand("fly").setExecutor(new Fly());
        getCommand("freeze").setExecutor(new Freeze());
        getCommand("invsee").setExecutor(new Invsee());
        getCommand("moderation").setExecutor(new Moderation());
        getCommand("kick").setExecutor(new Kick());
        getCommand("report").setExecutor(new Report());
        getCommand("reports").setExecutor(new Reports());
        getCommand("tban").setTabCompleter(new Ban());
        getCommand("tban").setExecutor(new Ban());
        getCommand("unban").setExecutor(new UnBan());
        getCommand("mute").setExecutor(new Mute());
        getCommand("mute").setTabCompleter(new Mute());
        getCommand("unmute").setExecutor(new UnMute());
        // Database
        final String url = "jdbc:mysql://" + ObjectLoad.host + ":" + ObjectLoad.port + "/" + /*ObjectsPreset.database +*/ "?user=" + ObjectLoad.username + "&password=" + ObjectLoad.password; // Enter URL with db name
        try {
            connection = DriverManager.getConnection(url);
            Requests.createDatabase();
            Requests.createDefaultsTables();
            RequestModeration.createDefaultsTables();
            log(ChatColor.YELLOW + "===========[Information]==========");
            log(ChatColor.GRAY + "[" + ChatColor.YELLOW + "Moderation4u" + ChatColor.GRAY + "] " + ChatColor.GREEN + "DataBase et tables chargées");
            log(ChatColor.YELLOW + "===========[Information]==========");
        } catch (SQLException e) {e.printStackTrace();}
        Report.reportsList.addAll(Requests.getReports());
        for (ReportEntity entity : Requests.getReports()) {
            if (!entity.getStatut().equals(ReportStatut.FINISH)) {
                if (entity.getStatut().equals(ReportStatut.TAKE))
                    Report.staffReportTake.put(entity.getTakeBy(), entity);
                Report.reportsListID.put(entity.getId(), entity);
            } else
                Report.reportsFinishList.put(entity.getId(), entity);
        }
        ObjectLoad.mutedPlayers = RequestModeration.getMutedPlayers();
    }

    @Override
    public void onDisable() {
        for (ReportEntity report : Report.reportsList) {
            Bukkit.getConsoleSender().sendMessage(report.toString());
            Requests.updateReport(report);
            if (report.getStatut().equals(ReportStatut.FINISH))
                Requests.moveReportToFinish(report);
        }
        for (ReportEntity report : Report.reportsFinishList.values()) {
            Requests.updateReport(report);
            Requests.moveReportToFinish(report);
        }
        log(ChatColor.YELLOW + "===========[Information]==========");
        log(ChatColor.GRAY + "[" + ChatColor.YELLOW + "Moderation4u" + ChatColor.GRAY + "] " + ChatColor.RED + "Plugin disabling");
        log(ChatColor.YELLOW + "===========[Information]==========");
    }

    public static void log(String s) {
        Bukkit.getConsoleSender().sendMessage(s);
    }

    public Connection getConnection() {
        return connection;
    }
}