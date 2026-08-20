package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class TpaDenyCommand implements CommandExecutor, TabCompleter {

    private final TpaCommand tpaCommand;
    private final BetterMinecraft plugin;

    public TpaDenyCommand(BetterMinecraft plugin, TpaCommand tpaCommand) {
        this.plugin = plugin;
        this.tpaCommand = tpaCommand;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, Command command, @NonNull String label, String[] args) {
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

    @Override
    public @Nullable List<String> onTabComplete(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if(!command.getName().equalsIgnoreCase("tpadeny")) {
            return List.of();
        }
        if(args.length == 1) {
            List<Player> pendingPlayers = tpaCommand.getPendingTeleports().keySet().stream().filter(player -> player.getName().toLowerCase().startsWith(args[0].toLowerCase())).toList();
            return pendingPlayers.stream().map(Player::getName).toList();
        }
        return List.of();
    }
}
