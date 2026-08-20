package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.command.*;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class BetterMinecraftCommand implements CommandExecutor, TabCompleter {

    private final BetterMinecraft plugin;

    public BetterMinecraftCommand(BetterMinecraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if(!command.getName().equalsIgnoreCase("betterminecraft")) {
            return true;
        }
        if(!sender.hasPermission("betterminecraft.admin")) {
            sender.sendMessage(plugin.getPrefix() + "§cYou do not have permission to use this command.");
            return true;
        }
        if(args.length == 0) {
            sender.sendMessage(plugin.getPrefix() + "§aBetterMinecraft plugin version: " + plugin.getDescription().getVersion());
            sender.sendMessage(plugin.getPrefix() + "§aAuthor: " + plugin.getDescription().getAuthors());
            sender.sendMessage(plugin.getPrefix() + "§aUse /betterminecraft reload to reload the plugin.");
            return true;
        }
        if(args.length == 1) {
            switch (args[0]) {
                case "reload": {
                    plugin.reloadConfig();
                    sender.sendMessage(plugin.getPrefix() + "§aBetterMinecraft plugin reloaded.");
                    return true;
                }
                case "version": {
                    sender.sendMessage(plugin.getPrefix() + "§aBetterMinecraft plugin version: " + plugin.getDescription().getVersion());
                    return true;
                }
                case "info": {
                    sender.sendMessage(plugin.getPrefix() + "§aBetterMinecraft plugin version: " + plugin.getDescription().getVersion());
                    sender.sendMessage(plugin.getPrefix() + "§aAuthor: " + plugin.getDescription().getAuthors());
                    return true;
                }
                case "help": {
                    sendHelp(sender);
                    return true;
                }
                case "commands": {
                    sendCommands(sender);
                    return true;
                }
            }
        }
        return false;
    }

    private void sendCommands(@NonNull CommandSender sender) {
        Map<String, Map<String, Object>> commands = plugin.getDescription().getCommands();

        if (commands.isEmpty()) {
            sender.sendMessage(plugin.getPrefix() + "§cNo commands available.");
            return;
        }

        sender.sendMessage(plugin.getPrefix() + "§aAvailable commands:");

        for (Map.Entry<String, Map<String, Object>> entry : commands.entrySet()) {
            String commandName = entry.getKey();
            Map<String, Object> commandInfo = entry.getValue();

            String description = getString(commandInfo, "description");
            String permission = getString(commandInfo, "permission");
            String usage = getString(commandInfo, "usage");
            List<String> aliases = getAliases(commandInfo);

            // Don't show commands the sender cannot use
            if (permission != null && !permission.isBlank() && !sender.hasPermission(permission)) {
                continue;
            }

            StringBuilder commandLine = new StringBuilder()
                    .append(plugin.getPrefix())
                    .append("§6/")
                    .append(commandName);

            if (description != null && !description.isBlank()) {
                commandLine.append(" §8- §f").append(description);
            }

            sender.sendMessage(commandLine.toString());

            if (permission != null && !permission.isBlank()) {
                sender.sendMessage(
                        plugin.getPrefix()
                                + "§8  ├ §7Permission: §f"
                                + permission
                );
            }

            if (!aliases.isEmpty()) {
                sender.sendMessage(
                        plugin.getPrefix()
                                + "§8  ├ §7Aliases: §f/"
                                + String.join(", /", aliases)
                );
            }

            if (usage != null && !usage.isBlank()) {
                sender.sendMessage(
                        plugin.getPrefix()
                                + "§8  └ §7Usage: §f"
                                + usage.replace("<command>", commandName)
                );
            }
        }
    }

    private String getString(Map<String, Object> commandInfo, String key) {
        Object value = commandInfo.get(key);
        return value instanceof String string ? string : null;
    }

    private List<String> getAliases(Map<String, Object> commandInfo) {
        Object value = commandInfo.get("aliases");

        if (value instanceof String alias) {
            return List.of(alias);
        }

        if (value instanceof List<?> list) {
            return list.stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .toList();
        }

        return List.of();
    }

    @Override
    public @Nullable List<String> onTabComplete(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if(!command.getName().equalsIgnoreCase("betterminecraft")) {
            return List.of();
        }
        if(args.length == 1) {
            List<String> completions = List.of("reload", "version", "info", "help", "commands");
            return completions.stream().filter(s -> s.startsWith(args[0])).toList();
        }
        return List.of();
    }

    private void sendHelp(@NonNull CommandSender sender) {
        sender.sendMessage(plugin.getPrefix() + "§a/betterminecraft reload - Reloads the plugin.");
        sender.sendMessage(plugin.getPrefix() + "§a/betterminecraft help - Shows this help message.");
        sender.sendMessage(plugin.getPrefix() + "§a/betterminecraft version - Shows the plugin version.");
        sender.sendMessage(plugin.getPrefix() + "§a/betterminecraft info - Shows plugin information.");
        sender.sendMessage(plugin.getPrefix() + "§a/betterminecraft commands - Shows all available commands.");
    }
}
