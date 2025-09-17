package io.github.derec4.deadRinger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class VeilCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /veil <on|off|toggle|list> [player]");
            return true;
        }

        String action = args[0].toLowerCase();

        if (action.equals("list")) {
            if (!sender.hasPermission("veil.admin")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }
            return handleList(sender);
        }

        // Determine target player
        Player target;
        boolean isTargetingSelf = args.length == 1;

        if (isTargetingSelf) {
            // Targeting self
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Console must specify a player!");
                return true;
            }

            if (!sender.hasPermission("veil.use")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to toggle your own veil!");
                return true;
            }

            target = (Player) sender;
        } else {
            // Targeting another player - requires admin permission
            if (!sender.hasPermission("veil.admin")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to target other players!");
                return true;
            }

            target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player '" + args[1] + "' not found or offline!");
                return true;
            }
        }

        // Handle the action
        switch (action) {
            case "on":
                return handleOn(sender, target, isTargetingSelf);
            case "off":
                return handleOff(sender, target, isTargetingSelf);
            case "toggle":
                return handleToggle(sender, target, isTargetingSelf);
            default:
                sender.sendMessage(ChatColor.RED + "Usage: /veil <on|off|toggle|list> [player]");
                return true;
        }
    }

    private boolean handleOn(CommandSender sender, Player target, boolean isTargetingSelf) {
        if (InvincibilityManager.isInvincible(target)) {
            if (isTargetingSelf) {
                sender.sendMessage(ChatColor.YELLOW + "You are already protected!");
            } else {
                sender.sendMessage(ChatColor.YELLOW + target.getName() + " is already protected!");
            }
            return true;
        }

        InvincibilityManager.grantInvincibility(target);

        if (isTargetingSelf) {
            sender.sendMessage(ChatColor.GREEN + "Veil protection enabled!");
        } else {
            sender.sendMessage(ChatColor.GREEN + "Veil protection enabled for " + target.getName());
            target.sendMessage(ChatColor.GREEN + "Veil protection has been enabled for you!");
        }
        return true;
    }

    private boolean handleOff(CommandSender sender, Player target, boolean isTargetingSelf) {
        if (!InvincibilityManager.isInvincible(target)) {
            if (isTargetingSelf) {
                sender.sendMessage(ChatColor.YELLOW + "You are not currently protected!");
            } else {
                sender.sendMessage(ChatColor.YELLOW + target.getName() + " is not currently protected!");
            }
            return true;
        }

        InvincibilityManager.revokeInvincibility(target);

        if (isTargetingSelf) {
            sender.sendMessage(ChatColor.RED + "Veil protection disabled!");
        } else {
            sender.sendMessage(ChatColor.RED + "Veil protection disabled for " + target.getName());
            target.sendMessage(ChatColor.RED + "Veil protection has been disabled for you!");
        }
        return true;
    }

    private boolean handleToggle(CommandSender sender, Player target, boolean isTargetingSelf) {
        if (InvincibilityManager.isInvincible(target)) {
            return handleOff(sender, target, isTargetingSelf);
        } else {
            return handleOn(sender, target, isTargetingSelf);
        }
    }

    private boolean handleList(CommandSender sender) {
        List<Player> veiledPlayers = Bukkit.getOnlinePlayers().stream()
            .filter(InvincibilityManager::isInvincible)
            .collect(Collectors.toList());

        if (veiledPlayers.isEmpty()) {
            sender.sendMessage(ChatColor.YELLOW + "No players are currently veiled.");
            return true;
        }

        sender.sendMessage(ChatColor.GREEN + "Veiled players:");
        veiledPlayers.forEach(player -> {
            sender.sendMessage(ChatColor.WHITE + "- " + player.getName() +
                ChatColor.GRAY + " (Health: " + String.format("%.1f", player.getHealth()) + "/20.0)");
        });
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> actions = Arrays.asList("on", "off", "toggle", "list");
            return actions.stream()
                    .filter(action -> action.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        } else if (args.length == 2) {
            String action = args[0].toLowerCase();

            if (!action.equals("list") && sender.hasPermission("veil.admin")) {
                return Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
            }
        }

        return completions;
    }
}
