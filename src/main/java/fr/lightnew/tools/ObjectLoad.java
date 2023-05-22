package fr.lightnew.tools;

import fr.lightnew.Moderation4u;
import org.bukkit.ChatColor;

public class ObjectLoad {

    public static String permission_command_staff, advanced_permission_command_staff, permission_receive_report, no_perm;
    public static String host, database, username, password;
    public static Integer port;

    public static void init() {
        permission_command_staff = "base.staff";
        advanced_permission_command_staff = "advanced.staff";
        permission_receive_report = "reportreceive.staff";
        no_perm = ChatColor.RED + "Vous n'avez la permission de faire ceci !";

        host = Moderation4u.instance.getConfig().getString("Database.host");
        database = Moderation4u.instance.getConfig().getString("Database.database");
        username = Moderation4u.instance.getConfig().getString("Database.username");
        password = Moderation4u.instance.getConfig().getString("Database.password");
        port = Moderation4u.instance.getConfig().getInt("Database.port");
    }
}
