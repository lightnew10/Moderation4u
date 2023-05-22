package fr.lightnew.tools;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.WeakHashMap;

public class Cooldown {

    public static boolean create(Player player, int time/*second*/, WeakHashMap<Player, Long> cooldownList) {
        if (cooldownList.containsKey(player)) {
            long timeSinceCreation = System.currentTimeMillis() - cooldownList.get(player);
            float timeLeft = time - (timeSinceCreation / 1000f);
            float secondsLeft = ((int) (timeLeft * 10)) / 10;
            if (secondsLeft <= 0) {
                cooldownList.remove(player);
                return true;
            }
            player.sendMessage(ChatColor.RED + "Il vous reste encore " + secondsLeft + " secondes avant de refaire la commande !");
            return false;
        }
        if (!cooldownList.containsKey(player))
            cooldownList.put(player, System.currentTimeMillis());
        return true;
    }
}
