package fr.lightnew.events;

import fr.lightnew.tools.PlayerUUID;
import fr.lightnew.tools.Vanish;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;

public class PlayerEvent implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Vanish.vanishPlayerForNewPlayer();
        File file = new File("plugins/Moderation4u/", player.getUniqueId() + ".yml");
        if (file.exists()) {
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            List<?> list = configuration.getList("inventory.items");
            player.getInventory().clear();
            for (int i = 0; i < list.size(); i++)
                player.getInventory().setItem(i, (ItemStack) list.get(i));
            file.delete();
        }
    }
}
