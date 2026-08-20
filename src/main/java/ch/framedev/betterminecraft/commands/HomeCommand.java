package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import ch.framedev.betterminecraft.managers.LocationManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;

public class HomeCommand implements CommandExecutor, TabCompleter {

    private final BetterMinecraft plugin;

    private final LocationManager locationManager = new LocationManager();

    public HomeCommand(BetterMinecraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getPrefix() + "§cYou must be a player to use this command.");
            return true;
        }
        if (command.getName().equalsIgnoreCase("sethome")) {
            if (!player.hasPermission("betterminecraft.home.set")) {
                player.sendMessage(plugin.getPrefix() + "§cYou do not have permission to set a home.");
                return true;
            }
            if (args.length == 0) {
                locationManager.setLocation(player.getName() + ".home.home", player.getLocation());
                player.sendMessage(plugin.getPrefix() + "§aHome set!");
                return true;
            }
            if (args.length == 1) {
                String homeName = args[0];
                locationManager.setLocation(player.getName() + ".home." + homeName, player.getLocation());
                player.sendMessage(plugin.getPrefix() + "§aHome " + homeName + " set!");
                return true;
            }
        }

        if (command.getName().equalsIgnoreCase("home")) {
            if (!player.hasPermission("betterminecraft.home.use")) {
                player.sendMessage(plugin.getPrefix() + "§cYou do not have permission to use homes.");
                return true;
            }
            if (args.length == 0) {
                Location home = locationManager.getLocation(player.getName() + ".home.home");
                if (home == null) {
                    player.sendMessage(plugin.getPrefix() + "§cYou do not have a home set.");
                    return true;
                }
                player.teleport(home);
                player.sendMessage(plugin.getPrefix() + "§aTeleporting to home...");
                return true;
            }
            if (args.length == 1) {
                String homeName = args[0];
                Location home = locationManager.getLocation(player.getName() + ".home." + homeName);
                if (home == null) {
                    player.sendMessage(plugin.getPrefix() + "§cYou do not have a home set.");
                    return true;
                }
                player.teleport(home);
                player.sendMessage(plugin.getPrefix() + "§aTeleporting to home...");
                return true;
            }
        }
        if(command.getName().equalsIgnoreCase("delhome")) {
            if(!player.hasPermission("betterminecraft.home.del")) {
                player.sendMessage(plugin.getPrefix() + "§cYou do not have permission to delete homes.");
                return true;
            }
            if(args.length == 0) {
                locationManager.removeLocation(player.getName() + ".home.home");
                player.sendMessage(plugin.getPrefix() + "§aHome deleted.");
                return true;
            }
            if(args.length == 1) {
                String homeName = args[0];
                if (!locationManager.removeLocation(player.getName() + ".home." + homeName)) {
                    player.sendMessage(plugin.getPrefix() + "§cYou do not have a home set with that name.");
                    return true;
                }
                player.sendMessage(plugin.getPrefix() + "§aHome deleted.");
                return true;
            }
        }
        if(command.getName().equalsIgnoreCase("homes")) {
            if(!player.hasPermission("betterminecraft.home.list")) {
                player.sendMessage(plugin.getPrefix() + "§cYou do not have permission to use this command.");
                return true;
            }
            player.sendMessage("§a=====Homes=====");
            List<String> homeNames = locationManager.getLocationNames(player.getName() + ".home");
            if (homeNames == null) return true;
            for(String homeName : homeNames) {
                TextComponent textComponent = new TextComponent("§6" + homeName);
                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClick to teleport to home §6" + homeName).create()));
                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home " + homeName));
                player.spigot().sendMessage(textComponent);
            }
            player.sendMessage("§a=====Homes=====");
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return List.of();
        if (args.length != 1) return List.of();
        if (command.getName().equalsIgnoreCase("home")) {
            String input = args[0].toLowerCase(Locale.ROOT);

            List<String> homeNames = locationManager.getLocationNames(player.getName() + ".home");
            if (homeNames == null) return List.of();
            return homeNames.stream()
                    .filter(homeName -> homeName.toLowerCase(Locale.ROOT).startsWith(input))
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .toList();
        }
        if(command.getName().equalsIgnoreCase("delhome")) {
            String input = args[0].toLowerCase(Locale.ROOT);

            List<String> homeNames = locationManager.getLocationNames(player.getName() + ".home");
            if (homeNames == null) return List.of();
            return homeNames.stream()
                    .filter(homeName -> homeName.toLowerCase(Locale.ROOT).startsWith(input))
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .toList();
        }
        return List.of();
    }
}
