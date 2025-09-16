package io.github.derec4.deadRinger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        // Check if the damaged entity is a player
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        if (!InvincibilityManager.isInvincible(player)) {
            return;
        }

        if (player.getHealth() <= 1.0) {
            // Allow fall damage as per requirements
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                return;
            }

            // Cancel all other damage when at half heart
            event.setCancelled(true);

            // Ensure player stays at exactly half heart
            player.setHealth(1.0);
        }
    }
}
