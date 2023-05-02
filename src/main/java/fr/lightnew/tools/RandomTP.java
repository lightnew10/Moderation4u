package fr.lightnew.tools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class RandomTP {

    public static void teleport(Player player) {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        for (Player p : players) {
            ArrayList<Player> list = new ArrayList<>();
            list.clear();
            list.add(p);
            if (player == p)
                return;
            if (list.size() > 2) {
                if (Vanish.isVanish(player)) {
                    player.teleport(list.get((new Random()).nextInt(list.size() - 1)));
                } else {
                    Vanish.setVanish(player);
                    player.teleport(list.get((new Random()).nextInt(list.size() - 1)));
                }
            } else
                player.sendMessage(ChatColor.YELLOW + "Vous devez être plus que deux sur le serveur pour vous téléporter aléatoirement !");
        }
    }
}
