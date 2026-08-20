package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import ch.framedev.betterminecraft.managers.LocationManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class SetWarpCommand implements CommandExecutor {

    private final BetterMinecraft plugin;

    public SetWarpCommand(BetterMinecraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        LocationManager locationManager = new LocationManager();
        if(!command.getName().equalsIgnoreCase("setwarp"))
            return true;
        if(!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getPrefix() + "§cYou have to be a player to execute this command!");
            return true;
        }
        if(!player.hasPermission("betbetterminecraft.warp.set")) {
            sender.sendMessage(plugin.getPrefix() + "§cYou don't have permission to execute this command!");
            return true;
        }
        if(args.length != 1) {
            player.sendMessage(plugin.getPrefix() + "§cYou have to specify a name for the warp!");
            player.sendMessage(plugin.getPrefix() + "§c/setwarp <name>!");
            return true;
        }
        locationManager.setLocation("warps." + args[0], player.getLocation());
        player.sendMessage(plugin.getPrefix() + "§aYou have successfully set the warp with the name '" + args[0] + "'!");
        return true;
    }
}
