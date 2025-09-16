package io.github.derec4.deadRinger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class DeadRinger extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        // Register event listener
        getServer().getPluginManager().registerEvents(new DamageListener(), this);

        // Register commands
        getCommand("invincible").setExecutor(new InvincibilityCommand());

        VeilCommand veilCommand = new VeilCommand();
        getCommand("veil").setExecutor(veilCommand);
        getCommand("veil").setTabCompleter(veilCommand);

        Bukkit.getLogger().info("");
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "  |_______|                             " +
                "  ");
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "  | Derex |     Dead Ringer v" + getDescription().getVersion());
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "  |_______|     Running on " + Bukkit.getName() + " - " + Bukkit.getVersion());
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Developed for YouTuber Kiingtong");
        Bukkit.getLogger().info("");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // Clear all protected players when plugin disables
        InvincibilityManager.clearAll();
    }
}
