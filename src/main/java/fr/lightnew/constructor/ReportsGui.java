package fr.lightnew.constructor;

import fr.lightnew.Jobs;
import fr.lightnew.commands.Report;
import fr.lightnew.jobs.JobsManager;
import fr.lightnew.tools.ItemBuilder;
import fr.lightnew.tools.PlayerUUID;
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

public class ReportsGui {
    private final Player player;
    private int page;
    public static WeakHashMap<Player, ReportsGui> playerInReport = new WeakHashMap<>();
    private HashMap<Integer, List<ItemStack>> pages = new HashMap<>();
    public static ItemStack NEXT_PanelGui;
    public static ItemStack BACK_PanelGui;

    public ReportsGui(Player player) {
        pages.clear();
        this.player = player;
        this.page = 0;
        List<ReportEntity> list = Report.reportsList;

        // Calculate pagination
        double page = ((double) list.size() / 36);
        Object a = Math.round(page*100.0)/100.0;
        int result;
        if (a.getClass().getSimpleName().equalsIgnoreCase("Double"))
            result = ((int) page + 1);
        else
            result = (int) page;

        for (int i = 0; i < result; i++) {
            List<ItemStack> items = new ArrayList<>();
            for (int j = i == 0 ? 0 : (i == 0 ? 37 : (i * 37)); j < 37 * (i + 1); j++)
                if ((list.size() > j))
                    if (list.get(j) != null) {
                        ReportEntity report = list.get(j);
                        if (!report.getStatut().equals(ReportStatut.FINISH))
                            items.add(createItem(report));
                    }
            pages.put(i, items);
        }
    }

    private ItemStack createItem(ReportEntity report) {
        String takeBy;
        String state = "";
        if (report.getTakeBy() == null)
            takeBy = "Personne";
        else
            takeBy = report.getTakeBy();
        if (report.getStatut() == ReportStatut.WAIT)
            state = ChatColor.GRAY + "CLIQUE GAUCHE POUR §l§aPRENDRE§r§7 LE REPORT";
        if (report.getStatut() == ReportStatut.TAKE)
            state = ChatColor.RED + "REPORT PRIT";
        if (report.getStatut() == ReportStatut.FINISH)
            state = ChatColor.RED + "REPORT FINI";
        String dateTake;
        if (report.getStatut() == ReportStatut.TAKE) {
            dateTake = ChatColor.YELLOW + "Date report prit : " + ChatColor.GOLD + new SimpleDateFormat("dd/MM/yyy hh:mm:ss").format(report.getDateTakeBy());
        } else
            dateTake = "";
        ItemStack itemStack = ItemBuilder.create(Material.BOOK, 1, ChatColor.GREEN + report.getReason(),
                ChatColor.YELLOW + "Créer par : " + ChatColor.GOLD + report.getOwner(),
                ChatColor.YELLOW + "contre : " + ChatColor.GOLD + report.getTarget(),
                ChatColor.YELLOW + "Raison : " + ChatColor.GOLD + report.getReason(),
                ChatColor.YELLOW + "Prit par : " + ChatColor.GOLD + takeBy,
                ChatColor.YELLOW + "Statut : " + ChatColor.GOLD + report.getStatut(),
                dateTake,
                "",
                state,
                ChatColor.GRAY + "CLIQUE DROIT POUR §l§cSUPPRIMER§r§7 LE REPORT",
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
        playerInReport.put(player, this);
        if (Report.staffReportTake.containsKey(player.getName())) {
            openInfoPlayer(player);
            return;
        }
        // 36 places
        NEXT_PanelGui = ItemBuilder.create(Material.ARROW, 1, ChatColor.RED + "Page suivante");
        Inventory inventory = Bukkit.createInventory(null, 6*9, ChatColor.YELLOW + "Tout les reports");
        int[] slotsPane = new int[]{0,1,2,3,4,5,6,7,8,45,46,47,48, 49,50,51,52};
        for (int j : slotsPane)
            inventory.setItem(j, new ItemStack(Material.BLUE_STAINED_GLASS_PANE,1));

        if (page < (pages.size()-1))
            inventory.setItem(53, NEXT_PanelGui);
        else
            inventory.setItem(53, new ItemStack(Material.BLUE_STAINED_GLASS_PANE,1));


        for (int i = 0; i < pages.get(page).size(); i++)
            inventory.addItem(pages.get(page).get(i));
        player.openInventory(inventory);
    }

    public void sendNextPage() {
        if (page < (pages.size()-1))
            page = (page+1);
        NEXT_PanelGui = ItemBuilder.create(Material.ARROW, 1, ChatColor.RED + "Page suivante");
        BACK_PanelGui = ItemBuilder.create(Material.ARROW, 1, ChatColor.RED + "Page Précédente");
        Inventory inventory = Bukkit.createInventory(null, 6*9, ChatColor.YELLOW + "Liste des Quêtes");
        int[] slotsPane = new int[]{0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52};
        for (int j : slotsPane)
            inventory.setItem(j, new ItemStack(Material.BLUE_STAINED_GLASS_PANE,1));
        inventory.setItem(45, BACK_PanelGui);

        if (page < (pages.size()-1))
            inventory.setItem(53, NEXT_PanelGui);
        else
            inventory.setItem(53, new ItemStack(Material.BLUE_STAINED_GLASS_PANE,1));

        for (int i = 0; i < pages.get(page).size(); i++)
            inventory.addItem(pages.get(page).get(i));
        player.openInventory(inventory);
    }

    public void sendBackPage() {
        page = (page-1);
        NEXT_PanelGui = ItemBuilder.create(Material.ARROW, 1, ChatColor.RED + "Page suivante");
        BACK_PanelGui = ItemBuilder.create(Material.ARROW, 1, ChatColor.RED + "Page Précédente");
        Inventory inventory = Bukkit.createInventory(null, 6*9, ChatColor.YELLOW + "Liste des Quêtes");
        int[] slotsPane = new int[]{0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52};
        for (int j : slotsPane)
            inventory.setItem(j, new ItemStack(Material.BLUE_STAINED_GLASS_PANE,1));
        inventory.setItem(45, BACK_PanelGui);

        if (page < (pages.size()-1))
            inventory.setItem(53, NEXT_PanelGui);
        else
            inventory.setItem(53, new ItemStack(Material.BLUE_STAINED_GLASS_PANE,1));

        for (int i = 0; i < pages.get(page).size(); i++)
            inventory.addItem(pages.get(page).get(i));
        player.openInventory(inventory);
    }

    public void openInfoPlayer(Player player) {
        ReportEntity report = Report.staffReportTake.get(player.getName());
        Player target;
        if (Bukkit.getPlayer(report.getTarget()) != null)
            target = Bukkit.getPlayer(report.getTarget());
        else
            target = Bukkit.getOfflinePlayer(PlayerUUID.uuidWithName(report.getTarget())).getPlayer();
        //JobsManager job = Jobs.playersJobs.get(target);
        Inventory inv = Bukkit.createInventory(player, 6*9, ChatColor.YELLOW + "Fiche de " + ChatColor.GOLD + report.getOwner());

        inv.setItem(4, ItemBuilder.skull(1, report.getOwner(), report.getOwner(), ChatColor.GRAY + "Prochainement plus d'informations"));
        inv.setItem(20, ItemBuilder.create(Material.GOLDEN_AXE, 1, ChatColor.RED + "Report abusif", ChatColor.GRAY + "Cette action ban le joueur pour :", ChatColor.YELLOW + "Report abusif " + ChatColor.GREEN + "(3j)"));
        inv.setItem(22, ItemBuilder.create(Material.COMPASS, 1, ChatColor.AQUA + "Se téléporter", ChatColor.GRAY + "Clique pour te téléporter."));
        inv.setItem(24, ItemBuilder.create(Material.ENCHANTED_BOOK, 1, "Données du joueur", ChatColor.GRAY + "Prochainement plus d'informations"));

        inv.setItem(39, ItemBuilder.create(Material.RED_WOOL, 1, ChatColor.RED + "BANNIR LE JOUEUR", ChatColor.GRAY + "Cette action ban le joueur pour :", ChatColor.YELLOW + report.getReason()));
        inv.setItem(41, ItemBuilder.create(Material.GREEN_WOOL, 1, ChatColor.GREEN + "FERMER LE REPORT", ChatColor.GRAY + "Cette action ferme le report."));
        player.openInventory(inv);
    }

    public void remove() {
        playerInReport.remove(player);
        pages.clear();
    }
}
