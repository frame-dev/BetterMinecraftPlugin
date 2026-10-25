package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HealCommand implements CommandExecutor {

    private final BetterMinecraft plugin;

    public HealCommand(BetterMinecraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.only-player"));
                return true;
            }
            if (!player.hasPermission("betterminecraft.heal")) {
                sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.no-permission"));
                return true;
            }
            AttributeInstance attributeInstance = player.getAttribute(Attribute.MAX_HEALTH);
            if (attributeInstance == null)
                return true;
            player.setHealth(attributeInstance.getValue());
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("heal.self"));
            return true;
        }
        if (args.length == 1) {
            String player = args[0];
            if (!sender.hasPermission("betterminecraft.heal.others")) {
                sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.no-permission"));
                return true;
            }
            Player target = Bukkit.getPlayer(player);
            if (target == null) {
                sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.player-not-found"));
                return true;
            }
            AttributeInstance attributeInstance = target.getAttribute(Attribute.MAX_HEALTH);
            if (attributeInstance == null)
                return true;
            target.setHealth(attributeInstance.getValue());
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("heal.other", "%player%", String.valueOf(target.getName())));
            target.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("heal.self"));
            return true;
        }
        return false;
    }
}
