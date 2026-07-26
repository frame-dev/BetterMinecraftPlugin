package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TpaCommand implements CommandExecutor {

    private final Map<Player, Player> pendingTeleports = new HashMap<>();
    private final BetterMinecraft plugin;

    public TpaCommand(BetterMinecraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!command.getName().equalsIgnoreCase("tpa")) {
            return false;
        }
        if(!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getPrefix() + "§cThis command can only be used by players.");
            return false;
        }
        if(!player.hasPermission("betterminecraft.tpa")) {
            sender.sendMessage(plugin.getPrefix() + "§cYou do not have permission to use this command.");
            return true;
        }
        if(args.length != 1) {
            sender.sendMessage(plugin.getPrefix() + "§cUsage: /tpa <player>");
            return false;
        }
        Player target = sender.getServer().getPlayer(args[0]);
        if(target == null) {
            sender.sendMessage(plugin.getPrefix() + "§cPlayer not found.");
            return false;
        }
        pendingTeleports.put(target, player);
        sender.sendMessage(plugin.getPrefix() + "§aTeleport request sent to " + target.getName() + ".");
        target.sendMessage(plugin.getPrefix() + "§a" + player.getName() + " wants to teleport to you. Type /tpaccept to accept.");
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
