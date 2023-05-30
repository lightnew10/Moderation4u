package fr.lightnew.events;

import fr.lightnew.commands.advance.BanEntity;
import fr.lightnew.sql.RequestModeration;
import fr.lightnew.tools.ObjectLoad;
import fr.lightnew.tools.PlayerUUID;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class SanctionEvent implements Listener {


    @EventHandler
    public void preJoin(AsyncPlayerPreLoginEvent event) {
        if (RequestModeration.isBan(event.getUniqueId())) {
            BanEntity ban = RequestModeration.getBan(event.getUniqueId());
            if (ban.getEndBan().before(new Date())) {
                ban.unBan();
            } else
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, ObjectLoad.banMessage(ban.getReason(), PlayerUUID.nameWithUUID(ban.getBannedByPlayer()), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(ban.getEndBan())));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void chat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (isMuted(player.getUniqueId())) {
            BanEntity mute = ObjectLoad.mutedPlayers.get(player.getUniqueId());
            if (mute.getEndBan().before(new Date())) {
                mute.unmute();
                player.sendMessage(ChatColor.GRAY + "[" + ChatColor.YELLOW + "Information" + ChatColor.GRAY + "] " + ChatColor.GREEN + "Vous êtes dé-mute !");
            } else {
                player.sendMessage(ChatColor.RED + "Vous êtes mute. " + ChatColor.GRAY + "(Fin du mute le " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(mute.getEndBan()) + ")");
                event.setCancelled(true);
            }
        }
    }

    private boolean isMuted(UUID uniqueId) {
        return ObjectLoad.mutedPlayers.containsKey(uniqueId);
    }

}
