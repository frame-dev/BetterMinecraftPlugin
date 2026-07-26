package ch.framedev.betterminecraft.main;

import ch.framedev.betterminecraft.commands.*;
import ch.framedev.betterminecraft.listeners.PlayerListeners;
import ch.framedev.betterminecraft.managers.RecipesManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BetterMinecraft extends JavaPlugin {

    private static BetterMinecraft instance;

    private RecipesManager recipesManager;

    @Override
    public void onEnable() {
        instance = this;
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        this.recipesManager = new RecipesManager(this);
        recipesManager.init();
        this.getServer().getPluginManager().registerEvents(new PlayerListeners(this), this);

        HomeCommand homeCommand = new HomeCommand(this);
        this.getCommand("home").setExecutor(homeCommand);
        this.getCommand("home").setTabCompleter(homeCommand);
        this.getCommand("sethome").setExecutor(homeCommand);
        this.getCommand("delhome").setExecutor(homeCommand);
        this.getCommand("delhome").setTabCompleter(homeCommand);
        this.getCommand("homes").setExecutor(homeCommand);

        TpaCommand tpaCommand = new TpaCommand(this);
        this.getCommand("tpa").setExecutor(tpaCommand);
        this.getCommand("tpadeny").setExecutor(new TpaDenyCommand(this, tpaCommand));
        this.getCommand("tpaaccept").setExecutor(new TpaAcceptCommand(this, tpaCommand));

        WarpCommand warpCommand = new WarpCommand(this);
        DelWarpCommand delWarpCommand = new DelWarpCommand(this);
        this.getCommand("setwarp").setExecutor(new SetWarpCommand(this));
        this.getCommand("warp").setExecutor(warpCommand);
        this.getCommand("delwarp").setExecutor(delWarpCommand);
        this.getCommand("warp").setTabCompleter(warpCommand);
        this.getCommand("delwarp").setTabCompleter(warpCommand);

        getLogger().info("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled.");
    }

    public String getPrefix() {
        return "§a[§6BetterMinecraft§a]§r ";
    }

    public static BetterMinecraft getInstance() {
        return instance;
    }

    public RecipesManager getRecipesManager() {
        return recipesManager;
    }

    public String getMessageFromConfig(String keyForMessage, String defaultMessage) {
        String message = getConfig().getString(keyForMessage, defaultMessage);
        if (message.contains("&"))
            message = message.replace("&", "§");
        return message;
    }
}
