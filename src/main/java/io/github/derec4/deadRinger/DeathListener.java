package io.github.derec4.deadRinger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (!InvincibilityManager.isInvincible(player)) {
            return;
        }

        if (player.getLastDamageCause() != null) {
            switch (player.getLastDamageCause().getCause()) {
                case FALL:
                case VOID:
                    return;
                default:
                    // Cancel death from all other causes for protected players
                    event.setCancelled(true);
                    player.setHealth(1.0);
                    break;
            }
        } else {
            event.setCancelled(true);
            player.setHealth(1.0);
        }
    }
}
