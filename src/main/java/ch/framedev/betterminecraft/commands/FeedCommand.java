package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FeedCommand implements CommandExecutor {

    private final BetterMinecraft plugin;

    public FeedCommand(BetterMinecraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!command.getName().equalsIgnoreCase("feed"))
            return true;
        if(args.length == 0) {
            if(!(sender instanceof Player player)) {
                sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.only-player"));
                return true;
            }
            if(!player.hasPermission("betterminecraft.feed")) {
                player.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.no-permission"));
                return true;
            }
            player.setFoodLevel(20);
            player.setSaturation(20);
            player.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("feed.self"));
            return true;
        }
        if(args.length == 1) {
            String playerName = args[0];
            if(!sender.hasPermission("betterminecraft.feed.others")) {
                sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("feed.no-permission-others"));
                return true;
            }
            Player target = plugin.getServer().getPlayer(playerName);
            if(target == null) {
                sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.player-not-found"));
                return true;
            }
            target.setFoodLevel(20);
            target.setSaturation(20);
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("feed.other", "%player%", String.valueOf(target.getName())));
            target.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("feed.by-player", "%player%", String.valueOf(sender.getName())));
            return true;
        }
        return false;
    }
}
