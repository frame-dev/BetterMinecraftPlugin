package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TpaDenyCommand implements CommandExecutor, TabCompleter {

    private final TpaCommand tpaCommand;
    private final BetterMinecraft plugin;

    public TpaDenyCommand(BetterMinecraft plugin, TpaCommand tpaCommand) {
        this.plugin = plugin;
        this.tpaCommand = tpaCommand;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if(!command.getName().equalsIgnoreCase("tpadeny"))
            return true;
        if(!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.only-player"));
            return true;
        }
        if(!player.hasPermission("betterminecraft.tpa")) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.no-permission"));
            return true;
        }
        if(!tpaCommand.hasPendingRequest(player)) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("tpadeny.no-request"));
            return true;
        }
        tpaCommand.removePendingRequest(player);
        sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("tpadeny.denied"));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!command.getName().equalsIgnoreCase("tpadeny")) {
            return List.of();
        }
        if(args.length == 1) {
            List<String> playerNames = new ArrayList<>();
            String input = args[0].toLowerCase(Locale.ROOT);
            for(Player player : tpaCommand.getPendingTeleports().keySet()) {
                if(player == null)
                    continue;
                String playerName = player.getName();
                if(playerName.toLowerCase(Locale.ROOT).startsWith(input)) {
                    playerNames.add(playerName);
                }
            }
            return playerNames;
        }
        return List.of();
    }
}
