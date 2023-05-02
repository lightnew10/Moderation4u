package fr.lightnew.commands;

import fr.lightnew.tools.ObjectLoad;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Freeze implements CommandExecutor {

    public static List<Player> freeze_players = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(ObjectLoad.permission_command_staff)) {
                if (args.length == 0) {
                    player.sendMessage(ChatColor.YELLOW + "Il vous manque d'arguments");
                    return false;
                }
                if (args.length == 1) {
                    if (Bukkit.getPlayer(args[0]) == null) {
                        player.sendMessage(ChatColor.YELLOW + "Ce joueur n'existe pas");
                        return false;
                    }
                    Player target = Bukkit.getPlayer(args[0]);
                    if (freeze_players.contains(target)) {
                        player.sendMessage(ChatColor.YELLOW + "Le joueur " + ChatColor.GOLD + target.getName() + ChatColor.YELLOW + " est bien " + ChatColor.GOLD + "dé-freeze");
                        target.sendMessage(ChatColor.YELLOW + "Vous êtes dé-freeze");
                        freeze_players.remove(target);
                    } else {
                        player.sendMessage(ChatColor.YELLOW + "Le joueur " + ChatColor.GOLD + target.getName() + ChatColor.YELLOW + " est bien " + ChatColor.GOLD + "dé-freeze");
                        target.sendMessage(ChatColor.YELLOW + "Vous êtes dé-freeze");
                        freeze_players.add(target);
                    }
                }
            } else {
                player.sendMessage(ObjectLoad.no_perm);
            }
        }
        return false;
    }
}
