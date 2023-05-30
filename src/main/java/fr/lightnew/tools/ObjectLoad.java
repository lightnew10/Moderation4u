package fr.lightnew.tools;

import fr.lightnew.Moderation4u;
import fr.lightnew.commands.advance.BanEntity;
import fr.lightnew.sql.RequestModeration;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ObjectLoad {

    public static String permission_command_staff, advanced_permission_command_staff, permission_receive_report, no_perm;
    public static String host, database, username, password;
    public static Integer port;
    public static HashMap<UUID, BanEntity> mutedPlayers = new HashMap<>();
    public static String banMessage(String reason, String by, String end) {
        return centerVertical(
                ChatColor.RED + "-=§lVOUS ÊTES BANI§r§c=-\n\n"
                        + ChatColor.YELLOW + "Vous avez été " + ChatColor.RED + "banni" + ChatColor.YELLOW + " par " + ChatColor.GOLD + by + "\n\n"
                        + ChatColor.YELLOW + "Raison : " + ChatColor.GOLD + reason + "\n"
                        + ChatColor.YELLOW + "Fin du ban le " + ChatColor.GOLD + end + "\n\n"
                        + ChatColor.GRAY + "Pour toute réclamation » " + ChatColor.GOLD + "www.site.fr"
                , 50);
    }

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

    public static String centerVertical(String text, int lineLength) {
        StringBuilder builder = new StringBuilder(text);
        char[] chars = text.toCharArray();
        char space = ' ';
        int midText = chars.length/2;
        int midLine = lineLength/2;

        for (int i = 0; i < lineLength; i++) {

            if (midText <= midLine) {
                midText = midText+1;
                builder.insert(0, space);
                builder.append(space);
            } else {
                i=lineLength;
            }
        }
        return builder.toString();
    }
}
