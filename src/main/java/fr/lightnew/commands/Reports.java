package fr.lightnew.commands;

import fr.lightnew.constructor.ReportEntity;
import fr.lightnew.constructor.ReportStatut;
import fr.lightnew.constructor.ReportsGui;
import fr.lightnew.constructor.ReportsLogsGui;
import fr.lightnew.tools.ItemBuilder;
import fr.lightnew.tools.ObjectLoad;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagString;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Reports implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && sender.hasPermission(ObjectLoad.permission_receive_report)) {
            Player player= (Player) sender;
            if (args.length == 0) {
                ReportsGui gui = new ReportsGui(player);
                gui.send();
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("logs") || args[0].equalsIgnoreCase("log")) {
                    ReportsLogsGui gui = new ReportsLogsGui(player);
                    gui.send();
                }
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("take")) {
                    if (UUID.fromString(args[1]) == null) {
                        player.sendMessage(ChatColor.RED + "ID incorrecte.");
                        return false;
                    }
                    UUID id = UUID.fromString(args[1]);
                    ReportEntity entity = Report.reportsListID.get(id);
                    if (entity.take(player)) {
                        player.sendMessage(ChatColor.YELLOW + "Vous avez prit le report de " + ChatColor.GOLD + entity.getOwner() + ChatColor.YELLOW + " envers " + ChatColor.GOLD + entity.getTarget() + ChatColor.YELLOW + " pour " + ChatColor.GOLD + entity.getReason());
                        Report.staffReportTake.put(entity.getTakeBy(), entity);
                        new ReportsGui(player).send();
                    }
                    return true;
                }
            }
        }
        return false;
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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1)
            return Arrays.asList("logs");
        return null;
    }
}
