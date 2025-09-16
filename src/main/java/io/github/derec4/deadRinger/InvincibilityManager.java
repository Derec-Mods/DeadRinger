package io.github.derec4.deadRinger;

import org.bukkit.entity.Player;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class InvincibilityManager {
    private static final Set<UUID> protectedPlayers = new HashSet<>();

    public static void addProtectedPlayer(Player player) {
        protectedPlayers.add(player.getUniqueId());
    }

    public static void removeProtectedPlayer(Player player) {
        protectedPlayers.remove(player.getUniqueId());
    }

    public static boolean isProtected(Player player) {
        return protectedPlayers.contains(player.getUniqueId());
    }

    public static Set<UUID> getProtectedPlayers() {
        return new HashSet<>(protectedPlayers);
    }

    public static void clearAll() {
        protectedPlayers.clear();
    }
}
