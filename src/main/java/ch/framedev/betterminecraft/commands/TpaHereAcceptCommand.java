package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class TpaHereAcceptCommand implements CommandExecutor {

    private final TpaHereCommand tpaHereCommand;
    private final BetterMinecraft plugin;

    public TpaHereAcceptCommand(BetterMinecraft plugin, TpaHereCommand tpaHereCommand) {
        this.plugin = plugin;
        this.tpaHereCommand = tpaHereCommand;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if(!command.getName().equalsIgnoreCase("tpahereaccept")) {
            return true;
        }
        if(!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getPrefix() + "§cThis command can only be used by players.");
            return true;
        }
        if(!player.hasPermission("betterminecraft.tpahereaccept")) {
            player.sendMessage(plugin.getPrefix() + "§cYou do not have permission to use this command.");
            return true;
        }
        if(!tpaHereCommand.hasPendingRequest(player)) {
            player.sendMessage(plugin.getPrefix() + "§cYou have no pending teleport requests.");
            return true;
        }
        Player requester = tpaHereCommand.getPendingRequest(player);
        if(requester == null) {
            player.sendMessage(plugin.getPrefix() + "§cThe player who sent the request is no longer online.");
            tpaHereCommand.removePendingRequest(player);
            return true;
        }
        requester.teleport(player.getLocation());
        requester.sendMessage(plugin.getPrefix() + "§aYou have been teleported to "+ player.getName() + ".");
        player.sendMessage(plugin.getPrefix() + "§a" + requester.getName() + " has been teleported to you.");
        tpaHereCommand.removePendingRequest(player);
        return true;
    }
}
