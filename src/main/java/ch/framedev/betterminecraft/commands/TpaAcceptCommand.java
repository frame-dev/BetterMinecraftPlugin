package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpaAcceptCommand implements CommandExecutor {

    private final TpaCommand tpaCommand;
    private final BetterMinecraft plugin;

    public TpaAcceptCommand(BetterMinecraft plugin, TpaCommand tpaCommand) {
        this.plugin = plugin;
        this.tpaCommand = tpaCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!command.getName().equalsIgnoreCase("tpaaccept")) {
            return false;
        }
        if(!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getPrefix() + "§cYou must be a player to use this command.");
            return true;
        }
        if(!player.hasPermission("betterminecraft.tpa")) {
            sender.sendMessage(plugin.getPrefix() + "§cYou do not have permission to use this command.");
            return true;
        }
        if(args.length != 0) {
            sender.sendMessage(plugin.getPrefix() + "§cUsage: /tpaaccept");
            return true;
        }
        if(!tpaCommand.hasPendingRequest(player)) {
            sender.sendMessage(plugin.getPrefix() + "§cYou do not have a pending teleport request.");
            return true;
        }
        Player target = tpaCommand.getPendingRequest(player);
        player.teleport(target);
        sender.sendMessage(plugin.getPrefix() + "§aTeleporting to " + target.getName() + ".");
        target.sendMessage(plugin.getPrefix() + "§a" + player.getName() + " has accepted your teleport request.");
        tpaCommand.removePendingRequest(player);
        return false;
    }
}
