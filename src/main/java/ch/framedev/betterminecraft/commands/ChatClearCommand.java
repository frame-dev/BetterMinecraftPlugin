package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChatClearCommand implements CommandExecutor {

    private final BetterMinecraft plugin;

    public ChatClearCommand(BetterMinecraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!command.getName().equalsIgnoreCase("chatclear"))
            return true;
        if(!sender.hasPermission("betterminecraft.chatclear")) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.no-permission"));
            return true;
        }
        if(args.length != 0) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("chatclear.usage"));
            return true;
        }

        String message = plugin.getPrefix() + plugin.getMessagesManager().getMessage("chatclear.cleared", "%player%", sender.getName());
        for(Player player : plugin.getServer().getOnlinePlayers()) {
            if(player == null)
                continue;
            for(int i = 0; i < 150; i++) {
                player.sendMessage("");
            }
            player.sendMessage(message);
        }
        if(!(sender instanceof Player)) {
            sender.sendMessage(message);
        }
        return true;
    }
}
