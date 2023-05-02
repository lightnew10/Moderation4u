package fr.lightnew.tools;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class Vanish {
    static ArrayList<Player> vanish = new ArrayList<>();
    private static Collection<? extends Player> players = Bukkit.getOnlinePlayers();

    public static Boolean isVanish(Player player) {
        if (vanish.contains(player))
            return true;
        else
            return false;
    }

    public static void setVanish(Player  player) {
        if (!isVanish(player)) {
            for (Player p : players)
                p.hidePlayer(player);
            vanish.add(player);
        }
    }

    public static void removeVanish(Player player) {
        if (isVanish(player)) {
            for (Player p : players)
                p.showPlayer(player);
            vanish.remove(player);
        }
    }

    public static void addVanish(Player player) {
        if (!isVanish(player)) {
            for (Player p : players)
                p.hidePlayer(player);
            vanish.add(player);
        }
    }

    //this function is used for vanish moderation to new player in server
    public static void vanishPlayerForNewPlayer() {
        for (Player mod : getPlayersVanish()) {
            for (Player p : players) {
                p.hidePlayer(mod);
            }
        }
    }

    public static ArrayList<Player> getPlayersVanish() {
        return vanish;
    }
}
