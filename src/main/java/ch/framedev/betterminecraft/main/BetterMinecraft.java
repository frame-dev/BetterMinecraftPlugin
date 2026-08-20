package ch.framedev.betterminecraft.main;

import ch.framedev.betterminecraft.commands.*;
import ch.framedev.betterminecraft.listeners.PlayerListeners;
import ch.framedev.betterminecraft.managers.CraftHistory;
import ch.framedev.betterminecraft.managers.RecipesManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class BetterMinecraft extends JavaPlugin {

    private static BetterMinecraft instance;

    private RecipesManager recipesManager;
    private CraftHistory craftHistory;

    @Override
    public void onEnable() {
        instance = this;

        setupConfig();
        getLogger().info("Config loaded.");

        recipesManager = new RecipesManager(this);
        recipesManager.init();
        getLogger().info("Recipes loaded.");

        craftHistory = new CraftHistory();

        getServer().getPluginManager().registerEvents(craftHistory, this);
        getServer().getPluginManager().registerEvents(new PlayerListeners(this), this);
        getLogger().info("Events registered.");

        registerCommands();
        getLogger().info("Commands registered.");

        getLogger().info("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled.");
    }

    private void setupConfig() {
        saveDefaultConfig();

        getConfig().options().copyDefaults(true);

        saveConfig();
    }

    private void registerCommands() {
        HomeCommand homeCommand = new HomeCommand(this);

        setExecutor("home", homeCommand);
        setTabCompleter("home", homeCommand);

        setExecutor("sethome", homeCommand);

        setExecutor("delhome", homeCommand);
        setTabCompleter("delhome", homeCommand);

        setExecutor("homes", homeCommand);

        TpaCommand tpaCommand = new TpaCommand(this);

        setExecutor("tpa", tpaCommand);

        TpaDenyCommand tpaDenyCommand = new TpaDenyCommand(this, tpaCommand);

        setExecutor("tpadeny", tpaDenyCommand);
        setTabCompleter("tpadeny", tpaDenyCommand);

        setExecutor("tpaaccept", new TpaAcceptCommand(this, tpaCommand));

        TpaHereCommand tpaHereCommand = new TpaHereCommand(this);
        setExecutor("tpahere", tpaHereCommand);
        setExecutor("tpahereaccept", new TpaHereAcceptCommand(this, tpaHereCommand));
        setExecutor("tpaheredeny", new TpaHereDenyCommand(this, tpaHereCommand));

        WarpCommand warpCommand = new WarpCommand(this);

        DelWarpCommand delWarpCommand = new DelWarpCommand(this);

        setExecutor("setwarp", new SetWarpCommand(this));

        setExecutor("warp", warpCommand);

        setTabCompleter("warp", warpCommand);

        setExecutor("delwarp", delWarpCommand);

        setTabCompleter("delwarp", warpCommand);

        setExecutor("heal", new HealCommand(this));

        setExecutor("feed", new FeedCommand(this));

        setExecutor("crafthistory", new CraftHistoryCommand(this));

        GameModeCommand gameModeCommand = new GameModeCommand(this);
        setExecutor("gamemode", gameModeCommand);
        setTabCompleter("gamemode", gameModeCommand);

        setExecutor("fly", new FlyCommand(this));

        DayNightCommand dayNightCommand = new DayNightCommand(this);
        setExecutor("day", dayNightCommand);
        setExecutor("night", dayNightCommand);

        WeatherCommands weatherCommands = new WeatherCommands(this);
        setExecutor("sun", weatherCommands);
        setExecutor("rain", weatherCommands);
        setExecutor("thunder", weatherCommands);

        // Main Command
        BetterMinecraftCommand betterMinecraftCommand = new BetterMinecraftCommand(this);
        setExecutor("betterminecraft", betterMinecraftCommand);
        setTabCompleter("betterminecraft", betterMinecraftCommand);
    }

    private void setExecutor(String commandName, org.bukkit.command.CommandExecutor executor) {
        PluginCommand command = getCommand(commandName);

        if (command == null) {
            getLogger().severe("Command '" + commandName + "' is missing from plugin.yml!");

            return;
        }

        command.setExecutor(executor);
    }

    private void setTabCompleter(String commandName, org.bukkit.command.TabCompleter completer) {
        PluginCommand command = getCommand(commandName);

        if (command == null) {
            getLogger().severe("Command '" + commandName + "' is missing from plugin.yml!");

            return;
        }

        command.setTabCompleter(completer);
    }

    public CraftHistory getCraftHistory() {
        return craftHistory;
    }

    public RecipesManager getRecipesManager() {
        return recipesManager;
    }

    public String getPrefix() {
        String prefix = getMessageFromConfig("prefix", "§a[§6BetterMinecraft§a]§r ");
        return prefix.replace('&', '§');
    }

    public static BetterMinecraft getInstance() {
        return instance;
    }

    public String getMessageFromConfig(String key, String defaultMessage) {
        String message = getConfig().getString(key, defaultMessage);

        if (message == null) {
            return defaultMessage.replace('&', '§');
        }

        return message.replace('&', '§');
    }
}