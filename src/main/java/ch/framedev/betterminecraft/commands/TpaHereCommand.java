package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TpaHereCommand implements CommandExecutor {

    private final BetterMinecraft plugin;
    private final Map<Player, Player> pendingTeleports = new HashMap<>();

    public TpaHereCommand(BetterMinecraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!command.getName().equalsIgnoreCase("tpahere")) {
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
        if(args.length != 1) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("tpahere.usage"));
            return true;
        }
        Player target = sender.getServer().getPlayer(args[0]);
        if(target == null) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.player-not-found"));
            return true;
        }
        pendingTeleports.put(target, player);
        sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("tpahere.sent", "%player%", String.valueOf(target.getName())));
        target.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("tpahere.received", "%player%", String.valueOf(player.getName())));
        return true;
    }

    public boolean hasPendingRequest(Player player) {
        return pendingTeleports.containsKey(player);
    }

    public Player getPendingRequest(Player player) {
        return pendingTeleports.get(player);
    }

    public void removePendingRequest(Player player) {
        pendingTeleports.remove(player);
    }
}
