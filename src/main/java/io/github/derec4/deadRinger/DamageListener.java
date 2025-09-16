package io.github.derec4.deadRinger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class DamageListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!InvincibilityManager.isInvincible(player)) {
            return;
        }

        if (player.getHealth() <= 1.0) {
            // Allow fall damage as per requirements
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                return;
            }

            // Void damage: Allow actively protected players to die
            if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                return;
            }

            // Totems: If holding totem, allow damage to be taken for natural video flow
            ItemStack mainHand = player.getInventory().getItemInMainHand();
            ItemStack offHand = player.getInventory().getItemInOffHand();
            if ((mainHand != null && mainHand.getType() == Material.TOTEM_OF_UNDYING) ||
                (offHand != null && offHand.getType() == Material.TOTEM_OF_UNDYING)) {
                return;
            }

            // Set minimal damage to show effects but schedule health restoration
            event.setDamage(0.01);

            // Schedule health restoration for next tick to ensure it happens after damage is applied
            Bukkit.getScheduler().runTaskLater(DeadRinger.getInstance(), () -> {
                if (player.isOnline()) {
                    player.setHealth(1.0);
                }
            }, 1L);
            /**
             * Void damage: Allow actively protected players to die.
             * Totems: If holding totem, allow damage to be taken in order to allow natural video flow
             */
        }
    }
}
