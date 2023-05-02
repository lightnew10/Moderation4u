package fr.lightnew;

import fr.lightnew.commands.Fly;
import fr.lightnew.commands.Freeze;
import fr.lightnew.commands.Invsee;
import fr.lightnew.commands.Moderation;
import fr.lightnew.events.ModerationEvents;
import fr.lightnew.events.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Moderation4u extends JavaPlugin {

    public static Moderation4u instance;

    @Override
    public void onEnable() {
        // Start logic
        instance = this;
        log(ChatColor.YELLOW + "===========[Information]==========");
        log(ChatColor.GREEN + "Plugin enabling");
        log(ChatColor.YELLOW + "===========[Information]==========");
        // Events
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new ModerationEvents(), this);
        manager.registerEvents(new PlayerManager(), this);
        // Commands
        getCommand("fly").setExecutor(new Fly());
        getCommand("freeze").setExecutor(new Freeze());
        getCommand("invsee").setExecutor(new Invsee());
        getCommand("moderation").setExecutor(new Moderation());
        // Other
    }

    @Override
    public void onDisable() {
        log(ChatColor.YELLOW + "===========[Information]==========");
        log(ChatColor.RED + "Plugin disabling");
        log(ChatColor.YELLOW + "===========[Information]==========");
    }

    public static void log(String s) {
        Bukkit.getConsoleSender().sendMessage(s);
    }
}