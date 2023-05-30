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

public class Mute implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String error = ChatColor.RED + "Erreur, faite /mute <player> <time> <longer> <reason>";
            UUID target;
            if (player.hasPermission(ObjectLoad.permission_receive_report)) {
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
                if (!Ban.isInteger(args[1])) {
                    player.sendMessage(ChatColor.RED + "Temps incorrecte.");
                    return false;
                } else
                    amount = Integer.parseInt(args[1]);


                if (Ban.isTimeType(args[2]) == null) {
                    player.sendMessage(ChatColor.RED + "Mauvaise argument -> \"" + ChatColor.GRAY + args[2] +"\"");
                    return false;
                }

                if (args.length == 2) {
                    new BanEntity(target, player.getUniqueId(), "Raison par défaut.", amount, Ban.isTimeType(args[2])).mute();
                    player.sendMessage(ChatColor.YELLOW + "Vous venez de mute " + ChatColor.GOLD + args[0] + ChatColor.YELLOW + " fin de son mute dans " + ChatColor.GOLD + args[1] + " " + args[2]);
                    return true;
                }

                if (args.length >= 3) {
                    String reason = "";
                    for (int i = 3; i < args.length; i++)
                        reason += " " + args[i];
                    new BanEntity(target, player.getUniqueId(), reason, amount, Ban.isTimeType(args[2])).mute();
                    player.sendMessage(ChatColor.YELLOW + "Vous venez de mute " + ChatColor.GOLD + args[0] + ChatColor.YELLOW + " fin de son mute dans " + ChatColor.GOLD + args[1] + " " + args[2]);
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
}
