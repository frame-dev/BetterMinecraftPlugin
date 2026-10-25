package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class BetterMinecraftCommand implements CommandExecutor, TabCompleter {

    private final BetterMinecraft plugin;

    public BetterMinecraftCommand(BetterMinecraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!command.getName().equalsIgnoreCase("betterminecraft")) {
            return true;
        }
        if(!sender.hasPermission("betterminecraft.admin")) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.no-permission"));
            return true;
        }
        if(args.length == 0) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("betterminecraft.version", "%version%", String.valueOf(plugin.getDescription().getVersion())));
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("betterminecraft.author", "%authors%", String.valueOf(plugin.getDescription().getAuthors())));
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("betterminecraft.reload-hint"));
            return true;
        }
        if(args.length == 1) {
            switch (args[0]) {
                case "reload": {
                    plugin.reloadConfig();
                    plugin.getMessagesManager().reload();
                    sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("betterminecraft.reloaded"));
                    return true;
                }
                case "version": {
                    sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("betterminecraft.version", "%version%", String.valueOf(plugin.getDescription().getVersion())));
                    return true;
                }
                case "info": {
                    sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("betterminecraft.version", "%version%", String.valueOf(plugin.getDescription().getVersion())));
                    sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("betterminecraft.author", "%authors%", String.valueOf(plugin.getDescription().getAuthors())));
                    sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("betterminecraft.website", "%website%", String.valueOf(plugin.getDescription().getWebsite())));
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

    private void sendCommands(@NotNull CommandSender sender) {
        Map<String, Map<String, Object>> commands = plugin.getDescription().getCommands();

        if (commands.isEmpty()) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("betterminecraft.no-commands"));
            return;
        }

        sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("betterminecraft.commands-header"));

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

            String commandPath = description != null && !description.isBlank()
                    ? "betterminecraft.command-entry-description"
                    : "betterminecraft.command-entry";
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage(
                    commandPath, "%command%", commandName, "%description%", description == null ? "" : description));

            if (permission != null && !permission.isBlank()) {
                sender.sendMessage(
                        plugin.getPrefix()
                                + plugin.getMessagesManager().getMessage("betterminecraft.command-permission", "%permission%", permission)
                );
            }

            if (!aliases.isEmpty()) {
                sender.sendMessage(
                        plugin.getPrefix()
                                + plugin.getMessagesManager().getMessage("betterminecraft.command-aliases", "%aliases%", String.join(", /", aliases))
                );
            }

            if (usage != null && !usage.isBlank()) {
                sender.sendMessage(
                        plugin.getPrefix()
                                + plugin.getMessagesManager().getMessage("betterminecraft.command-usage", "%usage%", usage.replace("<command>", commandName))
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
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!command.getName().equalsIgnoreCase("betterminecraft")) {
            return List.of();
        }
        if(args.length == 1) {
            List<String> completions = List.of("reload", "version", "info", "help", "commands");
            return completions.stream().filter(s -> s.startsWith(args[0])).toList();
        }
        return List.of();
    }

    private void sendHelp(@NotNull CommandSender sender) {
        sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("betterminecraft.help.reload"));
        sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("betterminecraft.help.help"));
        sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("betterminecraft.help.version"));
        sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("betterminecraft.help.info"));
        sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("betterminecraft.help.commands"));
    }
}
