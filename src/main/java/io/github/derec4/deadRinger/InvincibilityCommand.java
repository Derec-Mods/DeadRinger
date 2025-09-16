package io.github.derec4.deadRinger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InvincibilityCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if sender has permission
        if (!sender.hasPermission("kiingtong.invincible")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /invincible <add|remove|list> <player>");
            return true;
        }

        String action = args[0].toLowerCase();

        switch (action) {
            case "add":
                return handleAdd(sender, args[1]);
            case "remove":
                return handleRemove(sender, args[1]);
            case "list":
                return handleList(sender);
            default:
                sender.sendMessage(ChatColor.RED + "Usage: /invincible <add|remove|list> <player>");
                return true;
        }
    }

    private boolean handleAdd(CommandSender sender, String playerName) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player '" + playerName + "' not found or offline!");
            return true;
        }

        if (InvincibilityManager.isProtected(target)) {
            sender.sendMessage(ChatColor.YELLOW + target.getName() + " is already protected!");
            return true;
        }

        InvincibilityManager.addProtectedPlayer(target);
        sender.sendMessage(ChatColor.GREEN + "Added invincibility protection to " + target.getName());
        target.sendMessage(ChatColor.GREEN + "You have been granted invincibility protection!");
        return true;
    }

    private boolean handleRemove(CommandSender sender, String playerName) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player '" + playerName + "' not found or offline!");
            return true;
        }

        if (!InvincibilityManager.isProtected(target)) {
            sender.sendMessage(ChatColor.YELLOW + target.getName() + " is not currently protected!");
            return true;
        }

        InvincibilityManager.removeProtectedPlayer(target);
        sender.sendMessage(ChatColor.GREEN + "Removed invincibility protection from " + target.getName());
        target.sendMessage(ChatColor.RED + "Your invincibility protection has been removed!");
        return true;
    }

    private boolean handleList(CommandSender sender) {
        if (InvincibilityManager.getProtectedPlayers().isEmpty()) {
            sender.sendMessage(ChatColor.YELLOW + "No players are currently protected.");
            return true;
        }

        sender.sendMessage(ChatColor.GREEN + "Protected players:");
        InvincibilityManager.getProtectedPlayers().forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                sender.sendMessage(ChatColor.WHITE + "- " + player.getName() + " (Health: " +
                    String.format("%.1f", player.getHealth()) + "/20.0)");
            }
        });
        return true;
    }
}
