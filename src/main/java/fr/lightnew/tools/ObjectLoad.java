package fr.lightnew.tools;

import org.bukkit.ChatColor;

public class ObjectLoad {

    public static String permission_command_staff, advanced_permission_command_staff, no_perm;

    public static void init() {
        permission_command_staff = "base.staff";
        advanced_permission_command_staff = "advanced.staff";
        no_perm = ChatColor.RED + "Vous n'avez la permission de faire ceci !";
    }
}
