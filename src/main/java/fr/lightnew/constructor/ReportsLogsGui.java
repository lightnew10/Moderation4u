package fr.lightnew.constructor;

import fr.lightnew.commands.Report;
import fr.lightnew.tools.ItemBuilder;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagString;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.*;

public class ReportsLogsGui {
    private final Player player;
    private Inventory inventory;
    public static HashMap<Player, ReportsLogsGui> playerInReportsLogs = new HashMap<>();
    private HashMap<Integer, List<ItemStack>> pages = new HashMap<>();
    public static WeakHashMap<Player, Integer> playerPage = new WeakHashMap<>();
    public static ItemStack NEXT_PanelGui;
    public static ItemStack BACK_PanelGui;

    public ReportsLogsGui(Player player) {
        this.player = player;
        pages.clear();
        playerPage.clear();
        playerInReportsLogs.put(player, this);

        // Calculate pagination
        double page = ((double) Report.reportsFinishList.size() / 36);
        Object a = Math.round(page*100.0)/100.0;
        int result;
        if (a.getClass().getSimpleName().equalsIgnoreCase("Double"))
            result = ((int) page + 1);
        else
            result = (int) page;

        for (int i = 0; i < result; i++) {
            List<ItemStack> items = new ArrayList<>();
            for (int j = i == 0 ? 0 : (i == 0 ? 37 : (i * 37)); j < 37 * (i + 1); j++) {
                if ((Report.reportsFinishList.size() > j)) {
                    if (Report.reportsFinishList.get(j) != null) {
                        ReportEntity report = Report.reportsFinishList.get(j);
                            items.add(createItem(report));
                    }
                }
                if ((Report.reportsList.size() > j)) {
                    if (Report.reportsList.get(j) != null) {
                        ReportEntity report = Report.reportsList.get(j);
                            if (report.getStatut() == ReportStatut.FINISH)
                                items.add(createItem(report));
                    }
                }
            }
            pages.put(i, items);
        }

        playerPage.put(player, 0);
    }

    private ItemStack createItem(ReportEntity report) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy hh:mm:ss");
        ItemStack itemStack = ItemBuilder.create(Material.BOOK, 1, ChatColor.GREEN + report.getReason(),
                ChatColor.YELLOW + "Créer par : " + ChatColor.GOLD + report.getOwner(),
                ChatColor.YELLOW + "contre : " + ChatColor.GOLD + report.getTarget(),
                ChatColor.YELLOW + "Raison : " + ChatColor.GOLD + report.getReason(),
                ChatColor.YELLOW + "Prit par : " + ChatColor.GOLD + report.getTakeBy(),
                ChatColor.YELLOW + "Statut : " + ChatColor.GOLD + report.getStatut(),
                ChatColor.YELLOW + "Fini le : " + ChatColor.GOLD + sdf.format(report.getEndReport()),
                "",
                ChatColor.RED + "REPORT FINI",
                "",
                ChatColor.YELLOW + "ID : " + ChatColor.GRAY + report.getId());

        net.minecraft.server.v1_16_R3.ItemStack item = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound itemCompound = (item.hasTag()) ? item.getTag() : new NBTTagCompound();
        itemCompound.set("id", NBTTagString.a(report.getId().toString()));
        item.setTag(itemCompound);
        itemStack = CraftItemStack.asBukkitCopy(item);

        return itemStack;
    }

    public static UUID getIDItem(ItemStack itemStack) {
        net.minecraft.server.v1_16_R3.ItemStack item = CraftItemStack.asNMSCopy(itemStack);
        if (!item.hasTag())
            return null;
        NBTTagCompound itemCompound = item.getTag();
        String string = itemCompound.getString("id");
        return UUID.fromString(string);
    }

    public void send() {
        // 36 places
        NEXT_PanelGui = ItemBuilder.create(Material.ARROW, 1, ChatColor.RED + "Page suivante");
        inventory = Bukkit.createInventory(null, 6*9, ChatColor.YELLOW + "Logs des reports");
        int[] slotsPane = new int[]{0,1,2,3,4,5,6,7,8,45,46,47,48, 49,50,51,52};
        for (int j : slotsPane)
            inventory.setItem(j, new ItemStack(Material.BLUE_STAINED_GLASS_PANE,1));

        if (playerPage.get(player) < (pages.size()-1))
            inventory.setItem(53, NEXT_PanelGui);
        else
            inventory.setItem(53, new ItemStack(Material.BLUE_STAINED_GLASS_PANE,1));


        for (int i = 0; i < pages.get(playerPage.get(player)).size(); i++)
            inventory.addItem(pages.get(playerPage.get(player)).get(i));
        player.openInventory(inventory);
    }

    public void sendNextPage() {
        if (playerPage.get(player) < (pages.size()-1))
            playerPage.put(player, (playerPage.get(player)+1));
        NEXT_PanelGui = ItemBuilder.create(Material.ARROW, 1, ChatColor.RED + "Page suivante");
        BACK_PanelGui = ItemBuilder.create(Material.ARROW, 1, ChatColor.RED + "Page Précédente");
        inventory = Bukkit.createInventory(null, 6*9, ChatColor.YELLOW + "Liste des Quêtes");
        int[] slotsPane = new int[]{0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52};
        for (int j : slotsPane)
            inventory.setItem(j, new ItemStack(Material.BLUE_STAINED_GLASS_PANE,1));
        inventory.setItem(45, BACK_PanelGui);

        if (playerPage.get(player) < (pages.size()-1))
            inventory.setItem(53, NEXT_PanelGui);
        else
            inventory.setItem(53, new ItemStack(Material.BLUE_STAINED_GLASS_PANE,1));

        for (int i = 0; i < pages.get(playerPage.get(player)).size(); i++)
            inventory.addItem(pages.get(playerPage.get(player)).get(i));
        player.openInventory(inventory);
    }

    public void sendBackPage() {
        playerPage.put(player, (playerPage.get(player)-1));
        NEXT_PanelGui = ItemBuilder.create(Material.ARROW, 1, ChatColor.RED + "Page suivante");
        BACK_PanelGui = ItemBuilder.create(Material.ARROW, 1, ChatColor.RED + "Page Précédente");
        inventory = Bukkit.createInventory(null, 6*9, ChatColor.YELLOW + "Liste des Quêtes");
        int[] slotsPane = new int[]{0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52};
        for (int j : slotsPane)
            inventory.setItem(j, new ItemStack(Material.BLUE_STAINED_GLASS_PANE,1));
        inventory.setItem(45, BACK_PanelGui);

        if (playerPage.get(player) < (pages.size()-1))
            inventory.setItem(53, NEXT_PanelGui);
        else
            inventory.setItem(53, new ItemStack(Material.BLUE_STAINED_GLASS_PANE,1));

        for (int i = 0; i < pages.get(playerPage.get(player)).size(); i++)
            inventory.addItem(pages.get(playerPage.get(player)).get(i));
        player.openInventory(inventory);
    }
}
