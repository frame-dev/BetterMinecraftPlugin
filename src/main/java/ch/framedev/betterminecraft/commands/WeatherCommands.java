package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;

public class WeatherCommands implements CommandExecutor {

    private final BetterMinecraft plugin;

    public WeatherCommands(BetterMinecraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        return switch (command.getName().toLowerCase()) {
            case "sun" -> changeWeather(sender, Weather.CLEAR, "betterminecraft.sun");

            case "rain" -> changeWeather(sender, Weather.RAIN, "betterminecraft.rain");

            case "thunder" -> changeWeather(sender, Weather.THUNDER, "betterminecraft.thunder");

            default -> false;
        };
    }

    private boolean changeWeather(CommandSender sender, Weather weather, String permission) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(plugin.getPrefix() + "§cYou don't have permission to change the weather.");
            return true;
        }

        int changedWorlds = 0;

        for (World world : plugin.getServer().getWorlds()) {
            if (world.getEnvironment() != World.Environment.NORMAL) {
                continue;
            }

            switch (weather) {
                case CLEAR -> {
                    world.setStorm(false);
                    world.setThundering(false);
                    world.setWeatherDuration(0);
                    world.setThunderDuration(0);
                }

                case RAIN -> {
                    world.setStorm(true);
                    world.setThundering(false);
                    world.setWeatherDuration(20 * 60 * 10);
                }

                case THUNDER -> {
                    world.setStorm(true);
                    world.setThundering(true);
                    world.setWeatherDuration(20 * 60 * 10);
                    world.setThunderDuration(20 * 60 * 10);
                }
            }

            changedWorlds++;
        }

        if (changedWorlds == 0) {
            sender.sendMessage(plugin.getPrefix() + "§cNo supported world was found.");
            return true;
        }

        sender.sendMessage(plugin.getPrefix() + "§aWeather has been changed to §6" + weather.getDisplayName() + "§a.");

        return true;
    }

    private enum Weather {
        CLEAR("Clear"), RAIN("Rain"), THUNDER("Thunder");

        private final String displayName;

        Weather(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}