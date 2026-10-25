package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class PlayerWeatherCommand implements CommandExecutor {

    private final BetterMinecraft plugin;

    public PlayerWeatherCommand(BetterMinecraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if(!command.getName().equalsIgnoreCase("playerweather")) {
            return true;
        }
        if(!sender.hasPermission("betterminecraft.playerweather")) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.no-permission"));
            return true;
        }
        if(!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.only-player"));
            return true;
        }
        if(args.length != 1) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("playerweather.usage"));
            return true;
        }
        if(args[0].equalsIgnoreCase("clear")) {
            player.setPlayerWeather(org.bukkit.WeatherType.CLEAR);
            player.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("playerweather.clear"));
            return true;
        } else if(args[0].equalsIgnoreCase("rain")) {
            player.setPlayerWeather(org.bukkit.WeatherType.DOWNFALL);
            player.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("playerweather.rain"));
            return true;
        } else if(args[0].equalsIgnoreCase("reset")) {
            player.resetPlayerWeather();
            player.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("playerweather.reset"));
            return true;
        } else {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("playerweather.usage"));
            return true;
        }
    }
}
