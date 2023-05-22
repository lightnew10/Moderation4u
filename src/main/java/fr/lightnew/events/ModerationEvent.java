package fr.lightnew.events;

import fr.lightnew.commands.Freeze;
import fr.lightnew.commands.Moderation;
import fr.lightnew.commands.Report;
import fr.lightnew.constructor.ReportEntity;
import fr.lightnew.constructor.ReportStatut;
import fr.lightnew.constructor.ReportsGui;
import fr.lightnew.constructor.ReportsLogsGui;
import fr.lightnew.tools.ItemBuilder;
import fr.lightnew.tools.ObjectLoad;
import fr.lightnew.tools.Vanish;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ModerationEvent implements Listener {

    @EventHandler
    public void interactPlayer(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (event.getRightClicked() instanceof Player) {
            Player target = (Player) event.getRightClicked();
            ItemStack item = player.getItemInHand();
            if (!Moderation.mod_list_players.contains(player))
                return;
            if (item.getType() == Material.COMPASS) {
                Moderation.sendGuiInfoPlayer(PlayerManager.cachePlayer.get(target.getUniqueId()), player, target);
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void interactItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (Moderation.mod_list_players.contains(player)) {
            // open chest
            if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (event.getClickedBlock().getState() instanceof Container) {
                    event.setCancelled(true);
                    Container container = (Container) event.getClickedBlock().getState();
                    Inventory inventory = Bukkit.createInventory(player, container.getInventory().getSize(), ChatColor.YELLOW + "Coffre visuel");
                    ItemStack[] stack = container.getInventory().getContents();
                    for (int i = 0; i < stack.length; i++)
                        inventory.setItem(i, stack[i]);
                    player.openInventory(inventory);
                }
            }

            // use item
            ItemStack item = event.getItem();
            if (item == null)
                return;
            if (item.getType() == Material.COMPASS) {
                if (Bukkit.getOnlinePlayers().size() < 2) {
                    player.sendMessage(ChatColor.RED + "Pas assez de monde pour se téléporter aléatoirement.");
                    return;
                }
                Player target = (Player) Bukkit.getOnlinePlayers().toArray()[new Random().nextInt(Bukkit.getOnlinePlayers().size())];
                if (target != player) {
                    player.teleport(target);
                    player.sendMessage(ChatColor.YELLOW + "Téléportation vers " + ChatColor.GOLD + target.getName());
                } else
                    player.sendMessage(ChatColor.RED + "Impossible de vous téléporter sur vous même.");
            }
            if (item.getType() == Material.GRAY_DYE) {
                Vanish.addVanish(player);
                player.setItemInHand(ItemBuilder.create(Material.LIME_DYE, 1, ChatColor.YELLOW + "Vanish " + ChatColor.GRAY + "[" + ChatColor.GREEN + "Activé" + ChatColor.GRAY + "]" + ChatColor.GRAY + " | (Clique droit)"));
            }
            if (item.getType() == Material.LIME_DYE) {
                Vanish.removeVanish(player);
                player.setItemInHand(ItemBuilder.create(Material.GRAY_DYE, 1, ChatColor.YELLOW + "Vanish " + ChatColor.GRAY + "[" + ChatColor.RED + "Désactivé" + ChatColor.GRAY + "]" + ChatColor.GRAY + " | (Clique droit)"));
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void interactInventory(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equals(ChatColor.YELLOW + "Coffre visuel")) {
            ItemStack item = event.getCurrentItem();
            if (item == null)
                return;
            event.setCancelled(true);
        }

        // Report inventory interaction
        if (event.getView().getTitle().equals(ChatColor.YELLOW + "Report")) {
            ItemStack item = event.getCurrentItem();
            if (item == null)
                return;
            ItemStack[] items = {Report.REPORT_NAME_VILLAGE, Report.REPORT_CHAT, Report.REPORT_NAME, Report.REPORT_CHEAT, Report.REPORT_NAME_ITEM, Report.REPORT_SKIN, Report.REPORT_BUILD};
            if (Arrays.asList(items).contains(item))
                sendReport(player, Report.playerInInv.get(player), item);
            event.setCancelled(true);
        }

        if (event.getView().getTitle().equalsIgnoreCase(ChatColor.YELLOW + "Tout les reports")) {
            ItemStack item = event.getCurrentItem();
            if (item == null)
                return;
            if (event.getCurrentItem().isSimilar(ReportsGui.NEXT_PanelGui))
                ReportsGui.playerInReport.get(player).sendNextPage();
            if (event.getCurrentItem().isSimilar(ReportsGui.BACK_PanelGui))
                ReportsGui.playerInReport.get(player).sendBackPage();
            if (event.getCurrentItem().getType().equals(Material.BOOK)) {
                ReportEntity entity = Report.reportsListID.get(ReportsGui.getIDItem(event.getCurrentItem()));
                if (entity == null) {
                    player.sendMessage(ChatColor.YELLOW + "Report n'existe pas contacter le développeur.");
                    event.setCancelled(true);
                    return;
                }
                if (event.getClick().isRightClick()) {
                    if (entity.finish()) {
                        player.sendMessage(ChatColor.GREEN + "Vous avez bien supprimer le report");
                        player.closeInventory();
                    }
                }
                if (event.getClick().isLeftClick()) {
                    if (entity.take(player)) {
                        player.closeInventory();
                        player.sendMessage(ChatColor.YELLOW + "Vous avez prit le report de " + ChatColor.GOLD + entity.getOwner() + ChatColor.YELLOW + " envers " + ChatColor.GOLD + entity.getTarget() + ChatColor.YELLOW + " pour " + ChatColor.GOLD + entity.getReason());
                        Report.staffReportTake.put(entity.getTakeBy(), entity);
                    }
                }
            }
            event.setCancelled(true);
        }

        if (event.getView().getTitle().equalsIgnoreCase(ChatColor.YELLOW + "Logs des reports")) {
            ItemStack item = event.getCurrentItem();
            if (item == null)
                return;
            ReportsLogsGui gui = ReportsLogsGui.playerInReportsLogs.get(player);
            if (event.getCurrentItem().isSimilar(ReportsGui.NEXT_PanelGui))
                gui.sendNextPage();
            if (event.getCurrentItem().isSimilar(ReportsGui.BACK_PanelGui))
                gui.sendBackPage();
            event.setCancelled(true);
        }

        if (Report.staffReportTake.containsKey(player.getName())) {
            ReportEntity report = Report.staffReportTake.get(player.getName());
            if (event.getView().getTitle().equalsIgnoreCase(ChatColor.YELLOW + "Fiche de " + ChatColor.GOLD + report.getOwner())) {
                if (event.getCurrentItem().getType() == Material.COMPASS) {
                    player.closeInventory();
                    if (Bukkit.getPlayer(report.getTarget()) == null) {
                        player.sendMessage(ChatColor.RED + "Le joueur n'est pas connecter.");
                    } else
                        player.teleport(Bukkit.getPlayer(report.getTarget()).getLocation());
                }
                if (event.getCurrentItem().getType() == Material.GREEN_WOOL) {
                    report.finish();
                    player.sendMessage(ChatColor.GREEN + "Vous venez de fermer le report. " + ChatColor.GRAY + "(/reports logs pour voir les logs)");
                    player.closeInventory();
                    Report.staffReportTake.remove(player.getName());
                }
                if (event.getCurrentItem().getType() == Material.GOLDEN_AXE) {
                    // TODO BAN TARGET FOR "REPORT ABUSIF" 3 DAYS
                }
                if (event.getCurrentItem().getType() == Material.RED_WOOL) {
                    // TODO BAN TARGET FOR REASON REPORT
                }
            }
        }
    }

    @EventHandler
    public void close(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(ChatColor.YELLOW + "Report"))
            Report.playerInInv.remove((Player) event.getPlayer());

        if (event.getView().getTitle().equalsIgnoreCase(ChatColor.YELLOW + "Tout les reports"))
            ReportsGui.playerInReport.get((Player) event.getPlayer()).remove();

        if (event.getView().getTitle().equalsIgnoreCase(ChatColor.YELLOW + "Logs des reports")) {
            ReportsLogsGui.playerInReportsLogs.remove((Player) event.getPlayer());
            ReportsLogsGui.playerPage.remove((Player) event.getPlayer());
        }
    }

    private void sendReport(Player player, Player target, ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        String r = ChatColor.RED + "==========" + ChatColor.DARK_RED + "[" + ChatColor.GRAY + "Report" + ChatColor.DARK_RED + "]" + ChatColor.RED + "==========\n" +
                ChatColor.RED + "Nouveau report de " + ChatColor.GRAY + player.getName() + ChatColor.RED + " envers " + ChatColor.GRAY + target.getName();
        for (Player p : Bukkit.getOnlinePlayers())
            if (p.hasPermission(ObjectLoad.permission_receive_report))
                p.sendMessage(r);
        player.sendMessage(ChatColor.YELLOW + "Votre report à bien été envoyé \"" + ChatColor.GOLD + meta.getDisplayName() + ChatColor.YELLOW + "\"");
        ReportEntity report = new ReportEntity(player.getName(), target.getName(), meta.getDisplayName(), new Date(), ReportStatut.WAIT, null, null, null);
        report.create();
        player.closeInventory();
    }

    @EventHandler
    public void freeze(PlayerMoveEvent event) {
        if (Freeze.freeze_players.contains(event.getPlayer())) {
            Location to = event.getFrom();
            to.setPitch(event.getTo().getPitch());
            to.setYaw(event.getTo().getYaw());
            event.setTo(to);
        }
    }

    @EventHandler
    public void pickUp(PlayerPickupItemEvent event) {
        event.setCancelled(Moderation.mod_list_players.contains(event.getPlayer()));
    }
    @EventHandler
    public void pickUp(PlayerDropItemEvent event) {
        event.setCancelled(Moderation.mod_list_players.contains(event.getPlayer()));
    }
}
