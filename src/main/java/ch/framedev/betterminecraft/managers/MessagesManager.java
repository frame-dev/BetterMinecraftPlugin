package ch.framedev.betterminecraft.managers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MessagesManager {

    private final JavaPlugin plugin;
    private final File file;
    private YamlConfiguration config;

    public MessagesManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "messages.yml");
        reload();
    }

    public void reload() {
        boolean newFile = !file.exists();
        if (newFile) {
            plugin.saveResource("messages.yml", false);
        }

        config = new YamlConfiguration();
        config.options().parseComments(true);

        try {
            config.load(file);

            // Keep existing custom text when upgrading from config.yml messages.
            migrateLegacyMessage("general.prefix", "prefix", newFile);
            migrateLegacyMessage("warn.before-tool-break", "warn.beforeToolBreakMessage", newFile);

            try (InputStream stream = plugin.getResource("messages.yml")) {
                if (stream == null) {
                    plugin.getLogger().warning(
                            "Default messages.yml not found in resources!"
                    );
                    return;
                }

                YamlConfiguration defaults = new YamlConfiguration();
                defaults.options().parseComments(true);

                defaults.load(new InputStreamReader(
                        stream,
                        StandardCharsets.UTF_8
                ));

                config.setDefaults(defaults);
                config.options().copyDefaults(true);

                for (String path : defaults.getKeys(true)) {
                    List<String> comments = defaults.getComments(path);
                    List<String> inlineComments =
                            defaults.getInlineComments(path);

                    if (config.getComments(path).isEmpty()
                            && !comments.isEmpty()) {
                        config.setComments(path, comments);
                    }

                    if (config.getInlineComments(path).isEmpty()
                            && !inlineComments.isEmpty()) {
                        config.setInlineComments(path, inlineComments);
                    }
                }

                config.save(file);
            }

        } catch (Exception e) {
            plugin.getLogger().severe(
                    "Failed to load messages.yml: " + e.getMessage()
            );
        }
    }

    public String getMessage(String path) {
        String message = config.getString(path);

        if (message == null) {
            return ChatColor.RED + "Missing message: " + path;
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private void migrateLegacyMessage(String path, String legacyPath, boolean newFile) {
        if ((newFile || !config.isSet(path)) && plugin.getConfig().isString(legacyPath)) {
            config.set(path, plugin.getConfig().getString(legacyPath));
        }
    }

    public String getMessage(String path, String... replacements) {
        String message = getMessage(path);

        for (int i = 0; i + 1 < replacements.length; i += 2) {
            message = message.replace(replacements[i], replacements[i + 1]);
        }

        return message;
    }

    public String getPrefixedMessage(String path) {
        return getMessage("general.prefix") + getMessage(path);
    }

    public YamlConfiguration getConfig() {
        return config;
    }
}
