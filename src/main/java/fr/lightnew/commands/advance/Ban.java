package fr.lightnew.commands.advance;

import fr.lightnew.tools.ObjectLoad;
import fr.lightnew.tools.PlayerUUID;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Ban implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String error = ChatColor.RED + "Erreur, faite /ban <player> <time> <longer> <reason>";
            UUID target;
            if (player.hasPermission(ObjectLoad.advanced_permission_command_staff)) {
                if (args.length < 2) {
                    player.sendMessage(error);
                    return false;
                }
                List<String> times = new ArrayList<>();
                for (TimeType t : TimeType.values())
                    times.add(t.name());

                if (PlayerUUID.uuidWithName(args[0]) == null) {
                    player.sendMessage(ChatColor.RED + "Le Joueur n'existe pas !");
                    return false;
                } else
                    target = PlayerUUID.uuidWithName(args[0]);

                int amount;
                if (!isInteger(args[1])) {
                    player.sendMessage(ChatColor.RED + "Temps incorrecte.");
                    return false;
                } else
                    amount = Integer.parseInt(args[1]);


                if (isTimeType(args[2]) == null) {
                    player.sendMessage(ChatColor.RED + "Mauvaise argument -> \"" + ChatColor.GRAY + args[2] +"\"");
                    return false;
                }

                if (args.length == 2) {
                    new BanEntity(target, player.getUniqueId(), "Raison par défaut.", amount, isTimeType(args[2])).ban();
                    player.sendMessage(ChatColor.YELLOW + "Vous venez de bannir " + ChatColor.GOLD + args[0] + ChatColor.YELLOW + " fin de son ban dans " + ChatColor.GOLD + args[1] + " " + args[2]);
                    return true;
                }

                if (args.length >= 3) {
                    String reason = "";
                    for (int i = 3; i < args.length; i++)
                        reason += " " + args[i];
                    new BanEntity(target, player.getUniqueId(), reason, amount, isTimeType(args[2])).ban();
                    player.sendMessage(ChatColor.YELLOW + "Vous venez de bannir " + ChatColor.GOLD + args[0] + ChatColor.YELLOW + " fin de son ban dans " + ChatColor.GOLD + args[1] + " " + args[2]);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 2)
            return Arrays.asList("1");
        if (args.length == 3) {
            List<String> list = new ArrayList<>();
            for (TimeType type : TimeType.values())
                list.add(type.name());
            return list;
        }
        return null;
    }

    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static TimeType isTimeType(String str) {
        if (str == null)
            return null;
        if (str.equalsIgnoreCase("MIN"))
            return TimeType.MIN;
        if (str.equalsIgnoreCase("HOUR"))
            return TimeType.HOUR;
        if (str.equalsIgnoreCase("DAY"))
            return TimeType.DAY;
        if (str.equalsIgnoreCase("WEEK"))
            return TimeType.WEEK;
        if (str.equalsIgnoreCase("MONTH"))
            return TimeType.MONTH;
        if (str.equalsIgnoreCase("YEAR"))
            return TimeType.YEAR;
        return null;
    }
}
