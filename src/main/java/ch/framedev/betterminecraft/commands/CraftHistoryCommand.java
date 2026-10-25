package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import ch.framedev.betterminecraft.managers.CraftHistory;
import ch.framedev.betterminecraft.managers.CraftHistoryFilter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CraftHistoryCommand implements CommandExecutor {

    private final BetterMinecraft plugin;
    private final CraftHistory craftHistory;

    public CraftHistoryCommand(BetterMinecraft plugin) {
        this.plugin = plugin;
        this.craftHistory = plugin.getCraftHistory();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!command.getName().equalsIgnoreCase("crafthistory")) return true;
        if(!plugin.getConfig().getBoolean("others.collectCraftingHistory")) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("crafthistory.disabled"));
            return true;
        }
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.only-player"));
                return true;
            }
            if (!player.hasPermission("betterminecraft.crafthistory")) {
                player.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.no-permission"));
                return true;
            }
            craftHistory.showCraftingHistory(player, player, 0);
            return true;
        }
        if (args.length == 1) {
            if (!sender.hasPermission("betterminecraft.crafthistory.others")) {
                sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("crafthistory.no-permission-others"));
                return true;
            }
            String targetName = args[0];
            Player target = plugin.getServer().getPlayer(targetName);
            if (target == null) {
                sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.player-not-found"));
                return true;
            }
            if (sender instanceof Player player) {
                craftHistory.showCraftingHistory(player, target, 0);
                return true;
            }
            List<CraftHistory.CraftHistoryEntry> history = craftHistory.getPlayerHistory(target);
            if (history.isEmpty()) {
                sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("crafthistory.empty", "%player%", String.valueOf(target.getName())));
                return true;
            }
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("crafthistory.header", "%player%", String.valueOf(target.getName())));
            for (CraftHistory.CraftHistoryEntry entry : history) {
                sender.sendMessage(plugin.getMessagesManager().getMessage("crafthistory.entry", "%recipe%", String.valueOf(entry.recipe()), "%amount%", String.valueOf(entry.amount()), "%date%", String.valueOf(entry.lastCraftedFormatted())));
            }
            return true;
        }
        if (args.length == 2) {
            if (!sender.hasPermission("betterminecraft.crafthistory.others")) {
                sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("crafthistory.no-permission-others"));
                return true;
            }
            String targetName = args[0];
            Player target = plugin.getServer().getPlayer(targetName);
            if (target == null) {
                sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.player-not-found"));
                return true;
            }
            switch (args[1]) {
                case "amount": {
                    if(sender instanceof Player player) {
                        craftHistory.showCraftingHistory(player, target.getUniqueId(), targetName, 0, CraftHistoryFilter.AMOUNT_HIGHEST);
                        return true;
                    }
                    List<CraftHistory.CraftHistoryEntry> history = craftHistory.getPlayerHistory(target);
                    craftHistory.sortHistory(
                            history,
                            CraftHistoryFilter.AMOUNT_HIGHEST
                    );
                    if (history.isEmpty()) {
                        sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("crafthistory.empty", "%player%", String.valueOf(target.getName())));
                        return true;
                    }
                    sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("crafthistory.header", "%player%", String.valueOf(target.getName())));
                    for (CraftHistory.CraftHistoryEntry entry : history) {
                        sender.sendMessage(plugin.getMessagesManager().getMessage("crafthistory.entry", "%recipe%", String.valueOf(entry.recipe()), "%amount%", String.valueOf(entry.amount()), "%date%", String.valueOf(entry.lastCraftedFormatted())));
                    }
                    return true;
                }
                case "last": {
                    if(sender instanceof Player player) {
                        craftHistory.showCraftingHistory(player, target.getUniqueId(), targetName, 0, CraftHistoryFilter.LAST_CRAFTED_NEWEST);
                        return true;
                    }
                    List<CraftHistory.CraftHistoryEntry> history = craftHistory.getPlayerHistory(target);
                    craftHistory.sortHistory(
                            history,
                            CraftHistoryFilter.LAST_CRAFTED_NEWEST
                    );
                    if (history.isEmpty()) {
                        sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("crafthistory.empty", "%player%", String.valueOf(target.getName())));
                        return true;
                    }
                    sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("crafthistory.last-header", "%player%", String.valueOf(target.getName())));
                    for (CraftHistory.CraftHistoryEntry entry : history) {
                        sender.sendMessage(plugin.getMessagesManager().getMessage("crafthistory.entry", "%recipe%", String.valueOf(entry.recipe()), "%amount%", String.valueOf(entry.amount()), "%date%", String.valueOf(entry.lastCraftedFormatted())));
                    }
                    return true;
                }
                default:
                    sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("crafthistory.usage"));
                    return true;
            }
        }
        return false;
    }
}
