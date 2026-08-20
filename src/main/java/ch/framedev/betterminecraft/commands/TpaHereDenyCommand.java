package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class TpaHereDenyCommand implements CommandExecutor {

    private final BetterMinecraft plugin;
    private final TpaHereCommand tpaHereCommand;

    public TpaHereDenyCommand(BetterMinecraft plugin, TpaHereCommand tpaHereCommand) {
        this.plugin = plugin;
        this.tpaHereCommand = tpaHereCommand;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if(!command.getName().equalsIgnoreCase("tpaheredeny")) {
            return true;
        }
        if(!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getPrefix() + "§cThis command can only be used by players.");
            return true;
        }
        if(!player.hasPermission("betterminecraft.tpahere")) {
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
        requester.sendMessage(plugin.getPrefix() + "§cYour teleport request to " + player.getName() + " has been denied.");
        player.sendMessage(plugin.getPrefix() + "§cYou have denied the teleport request from " + requester.getName() + ".");
        tpaHereCommand.removePendingRequest(player);
        return true;
    }
}
