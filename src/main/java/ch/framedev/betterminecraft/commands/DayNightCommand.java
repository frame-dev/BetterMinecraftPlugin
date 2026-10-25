package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DayNightCommand implements CommandExecutor {

    private static final long DAY_TIME = 1000L;
    private static final long NIGHT_TIME = 18000L;

    private final BetterMinecraft plugin;

    public DayNightCommand(BetterMinecraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return switch (command.getName().toLowerCase()) {
            case "day" -> changeTime(sender, DAY_TIME, "Day", "betterminecraft.day");

            case "night" -> changeTime(sender, NIGHT_TIME, "Night", "betterminecraft.night");

            default -> false;
        };
    }

    private boolean changeTime(CommandSender sender, long time, String timeName, String permission) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.no-permission"));
            return true;
        }

        int changedWorlds = 0;

        for (World world : plugin.getServer().getWorlds()) {
            if (world.getEnvironment() != World.Environment.NORMAL) {
                continue;
            }

            world.setTime(time);
            changedWorlds++;
        }

        if (changedWorlds == 0) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("time.no-worlds"));
            return true;
        }
        String weatherCommand = plugin.getMessagesManager().getMessage("time.time-message", "%time%", timeName);
        sender.sendMessage(plugin.getPrefix() + weatherCommand);
        return true;
    }
}