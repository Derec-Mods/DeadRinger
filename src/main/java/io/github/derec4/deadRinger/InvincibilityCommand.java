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

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /invincible <add|remove|list> <player>");
            return true;
        }

        String action = args[0].toLowerCase();

        if (args.length < 2 && !action.equals("list")) {
            sender.sendMessage(ChatColor.RED + "Specify a player!");
            return true;
        }

        Player target = (args.length >= 2) ? Bukkit.getPlayer(args[1]) : null;

        if (target == null && !action.equals("list")) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return true;
        }

        switch (action) {
            case "add":
                InvincibilityManager.grantInvincibility(target);
                sender.sendMessage(ChatColor.GREEN + "Granted invincibility to " + target.getName());
                target.sendMessage(ChatColor.GREEN + "You are now invincible!");
                break;
            case "remove":
                InvincibilityManager.revokeInvincibility(target);
                sender.sendMessage(ChatColor.RED + "Removed invincibility from " + target.getName());
                target.sendMessage(ChatColor.RED + "You are no longer invincible!");
                break;
            case "list":
                sender.sendMessage(ChatColor.GREEN + "Invincible players:");
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (InvincibilityManager.isInvincible(player)) {
                        sender.sendMessage(ChatColor.WHITE + "- " + player.getName());
                    }
                }
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Usage: /invincible <add|remove|list> <player>");
                break;
        }
        return true;
    }
}
