package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GameModeCommand implements CommandExecutor, TabCompleter {

    private final BetterMinecraft plugin;

    public GameModeCommand(BetterMinecraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if(args.length == 1) {
            if(!(sender instanceof Player)) {
                sender.sendMessage(plugin.getPrefix() + "§cOnly players can change their gamemode!");
                return true;
            }
            changeGamemode(sender, null, args[0]);
            return true;
        } else if(args.length == 2) {
            changeGamemode(sender, args[1], args[0]);
            return true;
        } else {
            sender.sendMessage(plugin.getPrefix() + "§cUsage: /gamemode <gamemode> [player]");
            return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        List<String> gameModes = List.of("survival", "s", "0", "creative", "c", "1",
                "adventure", "a", "2", "spectator", "sp", "3");
        if(args.length == 1) {
            return new ArrayList<>(gameModes.stream().filter(gameMode -> gameMode.toLowerCase().startsWith(args[0].toLowerCase())).toList());
        }
        if(args.length == 2) {
            return new ArrayList<>(plugin.getServer().getOnlinePlayers().stream().map(Player::getName).filter(player -> player.toLowerCase().startsWith(args[1].toLowerCase())).toList());
        }
        return List.of();
    }

    private void changeGamemode(CommandSender source, String targetString, String input) {
        if (!isNumber(input)) {
            switch (input) {
                case "survival", "s" -> setGameMode(source, targetString, GameMode.SURVIVAL);
                case "creative", "c" -> setGameMode(source, targetString, GameMode.CREATIVE);
                case "adventure", "a" -> setGameMode(source, targetString, GameMode.ADVENTURE);
                case "spectator", "sp" -> setGameMode(source, targetString, GameMode.SPECTATOR);
                default -> source.sendMessage(plugin.getPrefix() + "§cInvalid gamemode! '" + input + "'");
            };
        } else {
            int number = Integer.parseInt(input);
            GameMode gameMode = gameModeByInt(number);
            if(gameMode == null) {
                source.sendMessage(plugin.getPrefix() + "§cInvalid gamemode! '" + input + "'");
            }
            setGameMode(source, targetString, gameMode);
        }
    }

    private boolean isNumber(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private void setGameMode(CommandSender source, String targetString, GameMode gameMode) {
        if (source instanceof Player player && targetString == null) {
            if (!source.hasPermission("betterminecraft.gamemode")) {
                source.sendMessage(plugin.getPrefix() + "§cYou don't have permission to change your gamemode!");
                return;
            }
            player.setGameMode(gameMode);
            player.sendMessage(plugin.getPrefix() + "§aYou have changed your gamemode to " + gameMode.toString().toLowerCase() + "!");
        } else {
            if (!source.hasPermission("betterminecraft.gamemode.others")) {
                source.sendMessage(plugin.getPrefix() + "§cYou don't have permission to change another player's gamemode!");
                return;
            }
            Player target = Bukkit.getPlayer(targetString);
            if (target == null) {
                source.sendMessage(plugin.getPrefix() + "§cPlayer not found! '" + targetString + "'");
                return;
            }
            target.setGameMode(gameMode);
            target.sendMessage(plugin.getPrefix() + "§aYour gamemode has been changed to " + gameMode.toString().toLowerCase() + "!");
            source.sendMessage(plugin.getPrefix() + "§aYou have changed " + target.getName() + "'s gamemode to " + gameMode.toString().toLowerCase() + "!");
            return;
        }
    }

    private GameMode gameModeByInt(int number) {
        return switch (number) {
            case 0 -> GameMode.SURVIVAL;
            case 1 -> GameMode.CREATIVE;
            case 2 -> GameMode.ADVENTURE;
            case 3 -> GameMode.SPECTATOR;
            default -> null;
        };
    }
}
