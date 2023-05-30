package fr.lightnew.commands.advance;

import fr.lightnew.sql.RequestModeration;
import fr.lightnew.tools.ObjectLoad;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class BanEntity {

    private final UUID banID;
    private final UUID playerBanned;
    private final UUID bannedByPlayer;
    private final String reason;
    private final Integer time;
    private final TimeType timeType;
    private final Date startBan;
    private final Date endBan;

    public BanEntity(UUID playerBanned, UUID bannedByPlayer, String reason, Integer time, TimeType timeType) {
        this.banID = UUID.randomUUID();
        this.playerBanned = playerBanned;
        this.bannedByPlayer = bannedByPlayer;
        this.reason = reason;
        this.time = time;
        this.timeType = timeType;
        this.startBan = new Date();
        this.endBan = addTime(time, timeType);
    }
    public BanEntity(UUID banID, UUID playerBanned, UUID bannedByPlayer, String reason, Integer time, TimeType timeType, Date startBan, Date endBan) {
        this.banID = banID;
        this.playerBanned = playerBanned;
        this.bannedByPlayer = bannedByPlayer;
        this.reason = reason;
        this.time = time;
        this.timeType = timeType;
        this.startBan = startBan;
        this.endBan = endBan;
    }

    private Date addTime(int amount, TimeType timeType) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (timeType == TimeType.MIN)
            calendar.add(Calendar.MINUTE, amount);
        if (timeType == TimeType.HOUR)
            calendar.add(Calendar.HOUR_OF_DAY, amount);
        if (timeType == TimeType.DAY)
            calendar.add(Calendar.DAY_OF_YEAR, amount);
        if (timeType == TimeType.WEEK)
            calendar.add(Calendar.WEEK_OF_YEAR, amount);
        if (timeType == TimeType.MONTH)
            calendar.add(Calendar.MONTH, amount);
        if (timeType == TimeType.YEAR)
            calendar.add(Calendar.YEAR, amount);
        return calendar.getTime();
    }

    public String getReason() {
        return reason;
    }

    public Date getEndBan() {
        return endBan;
    }

    public Date getStartBan() {
        return startBan;
    }

    public Integer getTime() {
        return time;
    }

    public TimeType getTimeType() {
        return timeType;
    }

    public UUID getBanID() {
        return banID;
    }

    public UUID getBannedByPlayer() {
        return bannedByPlayer;
    }

    public UUID getPlayerBanned() {
        return playerBanned;
    }


    public void ban() {
        if (Bukkit.getPlayer(playerBanned) != null) {
            Player player = Bukkit.getPlayer(playerBanned);
            player.kickPlayer(ObjectLoad.banMessage(reason, Bukkit.getPlayer(bannedByPlayer).getName(), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(endBan)));
        }
        RequestModeration.addBan(this);
    }

    public void unBan() {
        RequestModeration.addUnBanLog(this);
        RequestModeration.removeBan(this);
        //TODO ADD STATS BAN
    }

    public void mute() {
        if (Bukkit.getPlayer(playerBanned) != null) {
            Player player = Bukkit.getPlayer(playerBanned);
            player.sendMessage(ChatColor.RED + "Vous venez d'être banni pour " + ChatColor.GRAY + reason);
        }
        RequestModeration.addMute(this);
        ObjectLoad.mutedPlayers.put(playerBanned, this);
    }

    public void unmute() {
        RequestModeration.addUnMuteLog(this);
        RequestModeration.removeMute(this);
        ObjectLoad.mutedPlayers.remove(playerBanned);
        //TODO ADD STATS BAN
    }

}
