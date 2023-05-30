package fr.lightnew.commands.advance;

import fr.lightnew.sql.RequestModeration;
import fr.lightnew.tools.ObjectLoad;
import fr.lightnew.tools.PlayerUUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UnMute implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(ObjectLoad.permission_receive_report)) {
                if (args.length == 0) {
                    player.sendMessage(ChatColor.RED + "Erreur, faite /unmute <player>");
                }
                if (args.length == 1) {
                    UUID target = PlayerUUID.uuidWithName(args[0]);
                    if (target == null) {
                        player.sendMessage(ChatColor.RED + "Le joueur n'existe pas.");
                        return false;
                    }
                    if (RequestModeration.isMute(PlayerUUID.uuidWithName(args[0]))) {
                        ObjectLoad.mutedPlayers.get(target).unmute();
                        player.sendMessage(ChatColor.YELLOW + "Vous venez de unmute " + ChatColor.GOLD + args[0]);
                        if (Bukkit.getPlayer(target) != null)
                            Bukkit.getPlayer(target).sendMessage(ChatColor.GRAY + "[" + ChatColor.YELLOW + "Information" + ChatColor.GRAY + "] " + ChatColor.GREEN + "Vous êtes dé-mute !");
                        return true;
                    } else {
                        player.sendMessage(ChatColor.RED + "Le joueur n'est pas mute.");
                        return false;
                    }
                }
            }
        }
        return false;
    }
}
