package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerTimeCommand implements CommandExecutor {

    private final BetterMinecraft plugin;

    public PlayerTimeCommand(BetterMinecraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!command.getName().equalsIgnoreCase("playertime")) {
            return true;
        }
        if (!sender.hasPermission("betterminecraft.playertime")) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.no-permission"));
            return true;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.only-player"));
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("playertime.usage"));
            return true;
        }
        if (args[0].equalsIgnoreCase("day")) {
            player.setPlayerTime(4000, false);
            player.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("playertime.day"));
            return true;
        } else if (args[0].equalsIgnoreCase("night")) {
            player.setPlayerTime(13000, false);
            player.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("playertime.night"));
            return true;
        } else if(args[0].equalsIgnoreCase("reset")) {
            player.resetPlayerTime();
            player.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("playertime.reset"));
            return true;
        } else {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("playertime.usage"));
            return true;
        }
    }
}
