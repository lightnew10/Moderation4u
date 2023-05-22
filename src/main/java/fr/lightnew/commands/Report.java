package fr.lightnew.commands;

import fr.lightnew.constructor.ReportEntity;
import fr.lightnew.tools.Cooldown;
import fr.lightnew.tools.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Report implements CommandExecutor {

    public static WeakHashMap<Player, Player> playerInInv = new WeakHashMap<>();
    public static List<ReportEntity> reportsList = new ArrayList<>();
    public static HashMap<UUID, ReportEntity> reportsListID = new HashMap<>();
    public static HashMap<UUID, ReportEntity> reportsFinishList = new HashMap<>();
    public static HashMap<String, ReportEntity> staffReportTake = new HashMap<>();
    public WeakHashMap<Player, Long> wait = new WeakHashMap<>();

    public static ItemStack REPORT_CHAT = ItemBuilder.create(Material.PAPER, 1, ChatColor.YELLOW + "Chat Abuse/Scam", ChatColor.GRAY + "Détails :", "", ChatColor.GRAY + "Ce Report peut être effectué si un joueur", ChatColor.GRAY + "spam, scam ou bien même irrespectueux.");
    public static ItemStack REPORT_CHEAT = ItemBuilder.create(Material.DIAMOND_SWORD, 1, ChatColor.YELLOW + "Cheat/Hacking", ChatColor.GRAY + "Détails :", "", ChatColor.GRAY + "Un joueur semble suspect ? ", ChatColor.GRAY + "Signalez-le et nous allons le vérifier !");
    public static ItemStack REPORT_NAME = ItemBuilder.create(Material.SKELETON_SKULL, 1, ChatColor.YELLOW + "Nom non respectueux", ChatColor.GRAY + "Détails :", "", ChatColor.GRAY + "Un joueur avec un nom pas correcte ? ", ChatColor.GRAY + "Si son nom est réellement incorrecte signalez-le");
    public static ItemStack REPORT_NAME_VILLAGE = ItemBuilder.create(Material.OAK_SIGN, 1, ChatColor.YELLOW + "Nom de village non respectueux", ChatColor.GRAY + "Détails :", "", ChatColor.GRAY + "Un village avec un nom bizarre ou incorrect ?");
    public static ItemStack REPORT_NAME_ITEM = ItemBuilder.create(Material.NAME_TAG, 1, ChatColor.YELLOW + "Nom d'un item non respectueux", ChatColor.GRAY + "Détails :", "", ChatColor.GRAY + "Un item d'un joueur non correcte ?");
    public static ItemStack REPORT_SKIN = ItemBuilder.create(Material.BOOK, 1, ChatColor.YELLOW + "Skin non respectueux", ChatColor.GRAY + "Détails :", "", ChatColor.GRAY + "Skin non correcte ? Nous allons le sanctionner !");
    public static ItemStack REPORT_BUILD = ItemBuilder.create(Material.GRASS_BLOCK, 1, ChatColor.YELLOW + "Build non respectueux", ChatColor.GRAY + "Détails :", "", ChatColor.GRAY + "Des builds pas correct ?", ChatColor.GRAY + "signalez-le et nous allons le vérifier");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage(ChatColor.RED + "Erreur, faite /report <joueur>");
                return true;
            }
            if (args.length == 1) {
                if (Bukkit.getPlayer(args[0]) == null) {
                    player.sendMessage(ChatColor.RED + "Joueur pas disponible");
                    return true;
                }
                if (!Cooldown.create(player, 60*2, wait)) {
                    return true;
                } else {
                    Player target = Bukkit.getPlayer(args[0]);
                    sendGuiReport(player, target);
                }
            }
        }
        return false;
    }

    public void sendGuiReport(Player player, Player target) {
        Inventory inventory = Bukkit.createInventory(player, 6*9, ChatColor.YELLOW + "Report");
        inventory.setItem(4, ItemBuilder.skull(1, target.getName(), target.getName(), ChatColor.GRAY + "Plus d'infos prochainement"));
        int[] slots = {0,1,7,8,9,17,18,26,27,35,36,44,45,46,52,53};
        for (int i : slots)
            inventory.setItem(i, ItemBuilder.create(Material.CYAN_STAINED_GLASS_PANE, 1, ""));

        inventory.setItem(21, REPORT_NAME_VILLAGE);
        inventory.setItem(22, REPORT_CHAT);
        inventory.setItem(23, REPORT_NAME);

        inventory.setItem(31, REPORT_CHEAT);

        inventory.setItem(39, REPORT_NAME_ITEM);
        inventory.setItem(40, REPORT_SKIN);
        inventory.setItem(41, REPORT_BUILD);
        player.openInventory(inventory);
        playerInInv.put(player, target);
    }
}
