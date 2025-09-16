package io.github.derec4.deadRinger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class VeilJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (InvincibilityManager.isInvincible(player)) {
            player.sendMessage(ChatColor.GOLD + "Your veil is enabled");
        }
    }
}

