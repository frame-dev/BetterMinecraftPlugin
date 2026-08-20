package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class FlyCommand implements CommandExecutor {

    private final BetterMinecraft plugin;

    public FlyCommand(BetterMinecraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if(args.length == 0) {
            if(!(sender.hasPermission("betterminecraft.fly"))) {
                sender.sendMessage(plugin.getPrefix() + "§cYou do not have permission to use this command.");
                return true;
            }
            if(!(sender instanceof Player player)) {
                sender.sendMessage(plugin.getPrefix() + "§cYou must be a player to use this command.");
                return true;
            }
            boolean canFly = player.getAllowFlight();
            player.setAllowFlight(!canFly);
            player.setFlying(!canFly);
            player.sendMessage(plugin.getPrefix() + "§aYour flight mode has been" + (!canFly ? "enabled" : "disabled") + ".");
            return true;
        }
        if(args.length == 1) {
            if(!(sender.hasPermission("betterminecraft.fly.others"))) {
                sender.sendMessage(plugin.getPrefix() + "§cYou do not have permission to use this command.");
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null) {
                sender.sendMessage(plugin.getPrefix() + "§cPlayer not found. '" + args[0] + "'");
                return true;
            }
            boolean canFly = target.getAllowFlight();
            target.setAllowFlight(!canFly);
            target.setFlying(!canFly);
            target.sendMessage(plugin.getPrefix() + "§aYour flight mode has been " + (!canFly ? "enabled" : "disabled") + ".");
            sender.sendMessage(plugin.getPrefix() + "§aYou have " + (!canFly ? "enabled" : "disabled") + " flight for " + target.getName() + ".");
            return true;
        }
        return false;
    }
}
