package fr.lightnew.constructor;

import fr.lightnew.commands.Report;
import fr.lightnew.sql.Requests;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;

public class ReportEntity {

    private final UUID id;
    private final String owner;
    private final String target;
    private final String reason;
    private final Date date_creation;
    private ReportStatut statut;
    private String takeBy;
    private Date dateTakeBy;
    private Date endReport;

    public ReportEntity(String owner, String target, String reason, Date dateCreation, ReportStatut statut, String takeBy, Date dateTakeBy, Date endReport) {
        this.id = UUID.randomUUID();
        this.owner = owner;
        this.target = target;
        this.reason = reason;
        this.date_creation = dateCreation;
        this.statut = statut;
        this.takeBy = takeBy;
        this.dateTakeBy = dateTakeBy;
        this.endReport = endReport;
    }

    public ReportEntity(UUID id, String owner, String target, String reason, Date dateCreation, ReportStatut statut, String takeBy, Date dateTakeBy, Date endReport) {
        this.id = id;
        this.owner = owner;
        this.target = target;
        this.reason = reason;
        this.date_creation = dateCreation;
        this.statut = statut;
        this.takeBy = takeBy;
        this.dateTakeBy = dateTakeBy;
        this.endReport = endReport;
    }

    public UUID getId() {
        return id;
    }

    public Date getDate_creation() {
        return date_creation;
    }

    public String getOwner() {
        return owner;
    }

    public String getTakeBy() {
        return takeBy;
    }

    public String getTarget() {
        return target;
    }

    public String getReason() {
        return reason;
    }

    public ReportStatut getStatut() {
        return statut;
    }

    public Date getDateTakeBy() {
        return dateTakeBy;
    }

    public Date getEndReport() {
        return endReport;
    }

    public void setStatut(ReportStatut statut) {
        this.statut = statut;
    }

    public void setTakeBy(String takeBy) {
        this.takeBy = takeBy;
        Report.staffReportTake.put(takeBy, this);
    }

    public void setDateTakeBy(Date dateTakeBy) {
        this.dateTakeBy = dateTakeBy;
    }

    public void setEndReport(Date endReport) {
        this.endReport = endReport;
    }

    public boolean finish() {
        statut = ReportStatut.FINISH;
        endReport = new Date();
        Report.reportsListID.replace(id, Report.reportsListID.get(id), this);

        int index = -1;
        for (int i = 0; i < Report.reportsList.size(); i++) {
            if (Report.reportsList.get(i).getId().toString().equalsIgnoreCase(id.toString()))
                index = i;
        }
        if (index != -1)
            Report.reportsList.set(index, this);
        else
            return false;
        Requests.moveReportToFinish(this);
        Report.staffReportTake.remove(takeBy);
        return true;
    }

    public boolean take(Player player) {
        if (statut == ReportStatut.TAKE || statut == ReportStatut.FINISH) {
            player.sendMessage(ChatColor.YELLOW + "Ce report est déjà prit");
            return false;
        }
        statut = ReportStatut.TAKE;
        dateTakeBy = new Date();
        takeBy = player.getName();
        Report.reportsListID.replace(id, Report.reportsListID.get(id), this);

        int index = -1;
        for (int i = 0; i < Report.reportsList.size(); i++) {
            if (Report.reportsList.get(i).getId().toString().equalsIgnoreCase(id.toString()))
                index = i;
        }
        if (index != -1)
            Report.reportsList.set(index, this);
        return true;
    }

    public void create() {
        Report.reportsList.add(this);
        Report.reportsListID.put(id, this);
        Requests.addReport(this);
    }

    @Override
    public String toString() {
        return "ReportEntity{" +
                "id=" + id +
                ", owner='" + owner + '\'' +
                ", target='" + target + '\'' +
                ", reason='" + reason + ChatColor.RESET + '\'' +
                ", date_creation=" + date_creation +
                ", statut=" + statut +
                ", takeBy='" + takeBy + '\'' +
                ", dateTakeBy=" + dateTakeBy +
                ", endReport=" + endReport +
                '}';
    }
}
