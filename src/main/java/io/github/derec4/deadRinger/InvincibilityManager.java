package io.github.derec4.deadRinger;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import java.util.HashMap;
import java.util.Map;

public class InvincibilityManager {
    private static final Map<Player, PermissionAttachment> attachments = new HashMap<>();
    private static final String INVINCIBLE_PERMISSION = "kiingtong.invincible";

    public static void grantInvincibility(Player player) {
        if (!player.hasPermission(INVINCIBLE_PERMISSION)) {
            PermissionAttachment attachment = player.addAttachment(io.github.derec4.deadRinger.DeadRinger.getInstance());
            attachment.setPermission(INVINCIBLE_PERMISSION, true);
            attachments.put(player, attachment);
        }
    }

    public static void revokeInvincibility(Player player) {
        if (attachments.containsKey(player)) {
            PermissionAttachment attachment = attachments.get(player);
            attachment.unsetPermission(INVINCIBLE_PERMISSION);
            player.removeAttachment(attachment);
            attachments.remove(player);
        }
    }

    public static boolean isInvincible(Player player) {
        return player.hasPermission(INVINCIBLE_PERMISSION);
    }
}
