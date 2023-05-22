package fr.lightnew.commands.advance;

import fr.lightnew.tools.PlayerUUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BanUnit {

    private final UUID uuid;
    private final String name;
    private final Integer amount;
    private final TimeType timeType;
    private final String reason;

    public BanUnit(Player player, Integer amount, TimeType timeType, String reason) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.amount = amount;
        this.timeType = timeType;
        this.reason = reason;
    }

    public BanUnit(String name, Integer amount, TimeType timeType, String reason) {
        this.name = name;
        this.uuid = PlayerUUID.uuidWithName(name);
        this.amount = amount;
        this.timeType = timeType;
        this.reason = reason;
    }

    public void ban() {
        String message = "";
        if (Bukkit.getPlayer(uuid) != null) {
            Player player = Bukkit.getPlayer(uuid);
            player.kickPlayer(message);
        } else {
            //request for player in bdd
        }
    }

    public static void unBan() {

    }

}
