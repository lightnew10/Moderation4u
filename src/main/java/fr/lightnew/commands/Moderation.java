package fr.lightnew.commands;

import fr.lightnew.Jobs;
import fr.lightnew.Survival;
import fr.lightnew.jobs.JobsManager;
import fr.lightnew.sql.UserData;
import fr.lightnew.tools.ItemBuilder;
import fr.lightnew.tools.ObjectLoad;
import fr.lightnew.tools.Vanish;
import fr.lightnew.village.VillageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Moderation implements CommandExecutor {

    public static List<Player> mod_list_players = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(ObjectLoad.permission_command_staff)) {
                if (mod_list_players.contains(player)) {
                    mod_list_players.remove(player);
                    player.getInventory().clear();
                    setInventory(player);
                    player.sendMessage(ChatColor.GRAY + "[" + ChatColor.YELLOW + "Staff" + ChatColor.GRAY + "] " + ChatColor.RED + "Vous n'êtes plus \"MODE\" modération");
                } else {
                    mod_list_players.add(player);
                    saveInventory(player);
                    sendModeration(player);
                    player.sendMessage(ChatColor.GRAY + "[" + ChatColor.YELLOW + "Staff" + ChatColor.GRAY + "] " + ChatColor.GREEN + "Vous êtes en \"MODE\" modération");
                }
            }
        }
        return false;
    }

    public void sendModeration(Player player) {
        ItemStack rtp = ItemBuilder.create(Material.COMPASS, 1, ChatColor.YELLOW + "Téléportation aléatoire" + ChatColor.GRAY + " | (Clique droit)");
        ItemStack infoPlayer = ItemBuilder.create(Material.PACKED_ICE, 1, ChatColor.GOLD + "Informations joueur" + ChatColor.GRAY + " | (Clique droit)");
        ItemStack vanish = ItemBuilder.create(Material.LIME_DYE, 1, ChatColor.YELLOW + "Vanish " + ChatColor.GRAY + "[" + ChatColor.GREEN + "Activé" + ChatColor.GRAY + "]" + ChatColor.GRAY + " | (Clique droit)");

        saveInventory(player);
        player.getInventory().clear();

        player.getInventory().setItem(2, infoPlayer);
        player.getInventory().setItem(4, vanish);
        player.getInventory().setItem(6, rtp);
        Vanish.addVanish(player);
    }

    public void saveInventory(Player player) {
        File file = new File("plugins/Moderation4u/", player.getUniqueId() + ".yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        // Create File
        try {
            file.createNewFile();
            configuration.set("inventory.items", player.getInventory().getContents());
            configuration.save(file);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public void setInventory(Player player) {
        File file = new File("plugins/Moderation4u/", player.getUniqueId() + ".yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        List<?> list = configuration.getList("inventory.items");
        for (int i = 0; i < list.size(); i++)
            player.getInventory().setItem(i, (ItemStack) list.get(i));
        file.delete();
    }

    public static void sendGuiInfoPlayer(UserData data, Player player, Player target) {
        Inventory inventory = Bukkit.createInventory(player, 6*9, ChatColor.AQUA + "Informations " + ChatColor.BLUE + target.getName());

        data.getMoney();
        VillageManager village = Survival.villages.get(UUID.fromString(data.getVillageID()));
        village.getName();
        village.getDescription();
        village.getLevel();
        JobsManager jobs = Jobs.playersJobs.get(target);
        jobs.toString();

        inventory.setItem(0, ItemBuilder.create(Material.STONE_AXE, 1, ChatColor.YELLOW + "Information métier " + ChatColor.GOLD + target.getName(), ""));

        player.openInventory(inventory);
    }
}
