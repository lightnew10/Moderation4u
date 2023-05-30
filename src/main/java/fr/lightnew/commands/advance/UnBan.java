package fr.lightnew.commands.advance;

import fr.lightnew.sql.RequestModeration;
import fr.lightnew.tools.ObjectLoad;
import fr.lightnew.tools.PlayerUUID;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UnBan implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(ObjectLoad.advanced_permission_command_staff)) {
                if (args.length == 0) {
                    player.sendMessage(ChatColor.RED + "Erreur, faite /unban <player>");
                }
                if (args.length == 1) {
                    UUID target = PlayerUUID.uuidWithName(args[0]);
                    if (target == null) {
                        player.sendMessage(ChatColor.RED + "Le joueur n'existe pas.");
                        return false;
                    }
                    if (RequestModeration.isBan(PlayerUUID.uuidWithName(args[0]))) {
                        RequestModeration.getBan(target).unBan();
                        player.sendMessage(ChatColor.YELLOW + "Vous venez de unban " + ChatColor.GOLD + args[0]);
                        return true;
                    } else {
                        player.sendMessage(ChatColor.RED + "Le joueur n'est pas banni.");
                        return false;
                    }
                }
            }
        }
        return false;
    }
}
