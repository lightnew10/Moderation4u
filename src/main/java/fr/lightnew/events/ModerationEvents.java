package fr.lightnew.events;

import fr.lightnew.commands.Freeze;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class ModerationEvents implements Listener {

    @EventHandler
    public void freeze(PlayerMoveEvent event) {
        if (Freeze.freeze_players.contains(event.getPlayer())) {
            Location to = event.getFrom();
            to.setPitch(event.getTo().getPitch());
            to.setYaw(event.getTo().getYaw());
            event.setTo(to);
        }
    }
}
