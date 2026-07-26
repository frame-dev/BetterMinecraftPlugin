package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpaDenyCommand implements CommandExecutor {

    private final TpaCommand tpaCommand;
    private final BetterMinecraft plugin;

    public TpaDenyCommand(BetterMinecraft plugin, TpaCommand tpaCommand) {
        this.plugin = plugin;
        this.tpaCommand = tpaCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!command.getName().equalsIgnoreCase("tpadeny"))
            return true;
        if(!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getPrefix() + "§cYou must be a player to use this command.");
            return true;
        }
        if(!player.hasPermission("betterminecraft.tpa")) {
            sender.sendMessage(plugin.getPrefix() + "§cYou do not have permission to use this command.");
            return true;
        }
        if(!tpaCommand.hasPendingRequest(player)) {
            sender.sendMessage(plugin.getPrefix() + "§cYou do not have a pending request.");
            return true;
        }
        tpaCommand.removePendingRequest(player);
        sender.sendMessage(plugin.getPrefix() + "§aYou have denied the teleport request.");
        return true;
    }
}
