package fr.lightnew.commands;

import fr.lightnew.Moderation4u;
import fr.lightnew.tools.ObjectLoad;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Kick implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(ObjectLoad.permission_command_staff)) {
                if (args.length == 0) {
                    player.sendMessage(ChatColor.YELLOW + "Erreur, faite /kick <player> <raison>");
                    return true;
                }

                Player target;
                if (Bukkit.getPlayer(args[0]) == null) {
                    player.sendMessage(ChatColor.RED + "Le Joueur est pas sur le serveur");
                    return false;
                } else
                    target = Bukkit.getPlayer(args[0]);

                if (args.length == 1) {
                    String defaultKick = ChatColor.YELLOW + "Vous avez été kick par un modérateur";
                    player.sendMessage(ChatColor.YELLOW + "Vous venez de kick " + ChatColor.GOLD + target.getName() + ChatColor.YELLOW + " pour \"" + ChatColor.GOLD + defaultKick + "\"");
                    target.kickPlayer(baseKick(defaultKick));
                    return true;
                }
                if (args.length >= 2) {
                    String reason = "";
                    for (int i = 1; i < args.length; i++)
                        reason += " " + args[i];
                    target.kickPlayer(baseKick(reason));
                    player.sendMessage(ChatColor.YELLOW + "Vous venez de kick " + ChatColor.GOLD + target.getName() + ChatColor.YELLOW + " pour \"" + ChatColor.GOLD + reason + "\"");
                    return true;
                }
            }
        }
        return false;
    }

    private String baseKick(String s) {
        return
                ChatColor.RED + "§l" + Moderation4u.nameServer + "\n\n" +
                        ChatColor.RED + "Raison » §l" + ChatColor.RESET + s + "\n\n" +
                        ChatColor.GRAY + "Pour plus d'information » §l" + ChatColor.AQUA + "https://discord.gg/joblife";
    }
}
