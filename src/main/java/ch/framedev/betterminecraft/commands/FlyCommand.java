package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FlyCommand implements CommandExecutor {

    private final BetterMinecraft plugin;

    public FlyCommand(BetterMinecraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0) {
            if(!(sender.hasPermission("betterminecraft.fly"))) {
                sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.no-permission"));
                return true;
            }
            if(!(sender instanceof Player player)) {
                sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.only-player"));
                return true;
            }
            boolean canFly = player.getAllowFlight();
            player.setAllowFlight(!canFly);
            player.setFlying(!canFly);
            player.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("fly.self", "%state%", String.valueOf(plugin.getMessagesManager().getMessage(!canFly ? "fly.enabled" : "fly.disabled"))));
            return true;
        }
        if(args.length == 1) {
            if(!(sender.hasPermission("betterminecraft.fly.others"))) {
                sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.no-permission"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null) {
                sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("fly.player-not-found", "%player%", String.valueOf(args[0])));
                return true;
            }
            boolean canFly = target.getAllowFlight();
            target.setAllowFlight(!canFly);
            target.setFlying(!canFly);
            target.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("fly.self", "%state%", String.valueOf(plugin.getMessagesManager().getMessage(!canFly ? "fly.enabled" : "fly.disabled"))));
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("fly.other", "%state%", String.valueOf(plugin.getMessagesManager().getMessage(!canFly ? "fly.enabled" : "fly.disabled")), "%player%", String.valueOf(target.getName())));
            return true;
        }
        return false;
    }
}
