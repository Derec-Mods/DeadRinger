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
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        if (!InvincibilityManager.isInvincible(player)) {
            return;
        }

        // Edge cases
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            return;
        }

        // Totems: If holding totem, allow damage to be taken for natural video flow
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();
        if (mainHand.getType() == Material.TOTEM_OF_UNDYING ||
            offHand.getType() == Material.TOTEM_OF_UNDYING) {
            return;
        }

        double currentHealth = player.getHealth();
        double finalDamage = event.getFinalDamage();
        double healthAfterDamage = currentHealth - finalDamage;

        // If damage would bring player below 1.0 HP, protect them
        if (healthAfterDamage < 1.0) {
            event.setDamage(0.0);

            Bukkit.getScheduler().runTaskLater(DeadRinger.getInstance(), () -> {
                if (player.isOnline()) {
                    player.setHealth(1.0);
                }
            }, 1L);
        }
        // If damage wouldn't bring below 1.0 HP, allow normal damage
    }
}
