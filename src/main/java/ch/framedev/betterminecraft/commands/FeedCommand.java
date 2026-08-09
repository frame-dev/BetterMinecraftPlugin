package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class FeedCommand implements CommandExecutor {

    private final BetterMinecraft plugin;

    public FeedCommand(BetterMinecraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if(!command.getName().equalsIgnoreCase("feed"))
            return true;
        if(args.length == 0) {
            if(!(sender instanceof Player player)) {
                sender.sendMessage(plugin.getPrefix() + "§cYou must be a player to use this command.");
                return true;
            }
            if(!player.hasPermission("betterminecraft.feed")) {
                player.sendMessage(plugin.getPrefix() + "§cYou do not have permission to use this command.");
                return true;
            }
            player.setFoodLevel(20);
            player.setSaturation(20);
            player.sendMessage(plugin.getPrefix() + "§aYou have been fed.");
            return true;
        }
        if(args.length == 1) {
            String playerName = args[0];
            if(!sender.hasPermission("betterminecraft.feed.others")) {
                sender.sendMessage(plugin.getPrefix() + "§cYou do not have permission to feed other players.");
                return true;
            }
            Player target = plugin.getServer().getPlayer(playerName);
            if(target == null) {
                sender.sendMessage(plugin.getPrefix() + "§cPlayer not found.");
                return true;
            }
            target.setFoodLevel(20);
            target.setSaturation(20);
            sender.sendMessage(plugin.getPrefix() + "§aYou have fed " + target.getName() + ".");
            target.sendMessage(plugin.getPrefix() + "§aYou have been fed by " + sender.getName() + ".");
            return true;
        }
        return false;
    }
}
