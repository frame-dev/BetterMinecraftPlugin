package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;

public class TpaHereCommand implements CommandExecutor {

    private final BetterMinecraft plugin;
    private final Map<Player, Player> pendingTeleports = new HashMap<>();

    public TpaHereCommand(BetterMinecraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if(!command.getName().equalsIgnoreCase("tpahere")) {
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
        if(args.length != 1) {
            sender.sendMessage(plugin.getPrefix() + "§cUsage: /tpahere <player>");
            return true;
        }
        Player target = sender.getServer().getPlayer(args[0]);
        if(target == null) {
            sender.sendMessage(plugin.getPrefix() + "§cPlayer not found.");
            return true;
        }
        pendingTeleports.put(target, player);
        sender.sendMessage(plugin.getPrefix() + "§aTeleport request sent to " + target.getName() + ".");
        target.sendMessage(plugin.getPrefix() + "§a" + player.getName() + " wants you to teleport to them. Type /tpahereaccept to accept.");
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
