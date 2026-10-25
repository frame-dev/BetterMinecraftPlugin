package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import ch.framedev.betterminecraft.managers.LocationManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class DelWarpCommand implements CommandExecutor, TabCompleter {

    private final BetterMinecraft plugin;
    LocationManager locationManager = new LocationManager();

    public DelWarpCommand(BetterMinecraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!command.getName().equalsIgnoreCase("delwarp"))
            return true;
        if(!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.only-player"));
            return true;
        }
        if(!player.hasPermission("betterminecraft.warp.del")) {
            player.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.no-permission"));
            return true;
        }
        if(args.length != 1) {
            player.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("delwarp.usage"));
            return true;
        }
        locationManager.removeLocation("warps." + args[0]);
        player.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("delwarp.success", "%warp%", String.valueOf(args[0])));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!command.getName().equalsIgnoreCase("delwarp"))
            return List.of();
        if(args.length != 1) return List.of();
        List<String> warpNames = locationManager.getLocationNames("warps");
        String input = args[0].toLowerCase(Locale.ROOT);
        return warpNames.stream().filter(warpName -> warpName.toLowerCase(Locale.ROOT).startsWith(input))
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }
}
