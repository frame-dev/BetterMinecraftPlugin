package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import ch.framedev.betterminecraft.managers.LocationManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class WarpCommand implements CommandExecutor, TabCompleter {

    private final BetterMinecraft plugin;
    LocationManager locationManager = new LocationManager();

    public WarpCommand(BetterMinecraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getPrefix() + "§cOnly Player can use this command!");
            return true;
        }
        if (!command.getName().equalsIgnoreCase("warp")) {
            return true;
        }
        if(!player.hasPermission("betterminecraft.warp")) {
            sender.sendMessage(plugin.getPrefix() + "§cYou don't have permission to use this command!");
            return true;
        }
        if(args.length != 1) {
            player.sendMessage(plugin.getPrefix() + "§c/warp <name>!");
            return true;
        }
        Location location = locationManager.getLocation("warps." + args[0]);
        if(location == null) {
            player.sendMessage(plugin.getPrefix() + "§cThe warp with the name '" + args[0] + "' doesn't exist!");
            return true;
        }
        player.teleport(location);
        player.sendMessage(plugin.getPrefix() + "§aYou have been teleported to the warp with the name '" + args[0] + "'!");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if(!command.getName().equalsIgnoreCase("warp"))
            return List.of();
        if(args.length != 1) return List.of();
        List<String> warpNames = locationManager.getLocationNames("warps");
        String input = args[0].toLowerCase(Locale.ROOT);
        return warpNames.stream().filter(warpName -> warpName.toLowerCase(Locale.ROOT).startsWith(input))
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }
}
