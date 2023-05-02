package fr.lightnew.commands;

import fr.lightnew.tools.ObjectLoad;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Invsee implements CommandExecutor {

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
                    Inventory inventory = Bukkit.createInventory(player, 6*9, ChatColor.YELLOW + "Inventaire de " + ChatColor.GOLD + target.getName());
                    for (ItemStack item : player.getInventory().getContents())
                        inventory.addItem(item);

                    player.openInventory(inventory);
                }
            }
        }
        return false;
    }
}
