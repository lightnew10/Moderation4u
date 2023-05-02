package fr.lightnew.commands;

import fr.lightnew.tools.ObjectLoad;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Fly implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(ObjectLoad.permission_command_staff)) {
                if (player.getAllowFlight()) {
                    player.sendMessage(ChatColor.GRAY + "[" + ChatColor.RED + "Fly" + ChatColor.GRAY + "] " + ChatColor.RED + "Fly Désactivé");
                    player.setAllowFlight(false);
                    player.setFlying(false);
                } else {
                    player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "Fly" + ChatColor.GRAY + "] " + ChatColor.GREEN + "Fly Activé");
                    player.setAllowFlight(true);
                    player.setFlying(true);
                }
            } else {
                player.sendMessage(ObjectLoad.no_perm);
            }
        }
        return false;
    }
}
