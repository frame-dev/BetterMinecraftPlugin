package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpaHereDenyCommand implements CommandExecutor {

    private final BetterMinecraft plugin;
    private final TpaHereCommand tpaHereCommand;

    public TpaHereDenyCommand(BetterMinecraft plugin, TpaHereCommand tpaHereCommand) {
        this.plugin = plugin;
        this.tpaHereCommand = tpaHereCommand;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!command.getName().equalsIgnoreCase("tpaheredeny")) {
            return true;
        }
        if(!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.only-player"));
            return true;
        }
        if(!player.hasPermission("betterminecraft.tpahere")) {
            player.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.no-permission"));
            return true;
        }
        if(!tpaHereCommand.hasPendingRequest(player)) {
            player.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("tpaheredeny.no-request"));
            return true;
        }
        Player requester = tpaHereCommand.getPendingRequest(player);
        if(requester == null) {
            player.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("tpaheredeny.offline"));
            tpaHereCommand.removePendingRequest(player);
            return true;
        }
        requester.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("tpaheredeny.request-denied", "%player%", String.valueOf(player.getName())));
        player.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("tpaheredeny.denied", "%player%", String.valueOf(requester.getName())));
        tpaHereCommand.removePendingRequest(player);
        return true;
    }
}
