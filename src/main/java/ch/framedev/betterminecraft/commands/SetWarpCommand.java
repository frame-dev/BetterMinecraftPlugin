package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import ch.framedev.betterminecraft.managers.LocationManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetWarpCommand implements CommandExecutor {

    private final BetterMinecraft plugin;

    public SetWarpCommand(BetterMinecraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        LocationManager locationManager = new LocationManager();
        if(!command.getName().equalsIgnoreCase("setwarp"))
            return true;
        if(!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.only-player"));
            return true;
        }
        if(!player.hasPermission("betterminecraft.warp.set")) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.no-permission"));
            return true;
        }
        if(args.length != 1) {
            player.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("setwarp.missing-name"));
            player.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("setwarp.usage"));
            return true;
        }
        locationManager.setLocation("warps." + args[0], player.getLocation());
        player.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("setwarp.success", "%warp%", String.valueOf(args[0])));
        return true;
    }
}
