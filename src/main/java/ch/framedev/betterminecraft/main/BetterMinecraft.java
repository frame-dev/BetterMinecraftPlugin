package ch.framedev.betterminecraft.main;

import ch.framedev.betterminecraft.commands.HomeCommand;
import ch.framedev.betterminecraft.commands.TpaAcceptCommand;
import ch.framedev.betterminecraft.commands.TpaCommand;
import ch.framedev.betterminecraft.commands.TpaDenyCommand;
import ch.framedev.betterminecraft.listeners.PlayerListeners;
import ch.framedev.betterminecraft.managers.RecipesManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BetterMinecraft extends JavaPlugin {

    private static BetterMinecraft instance;

    private final String prefix = "§a[§6BetterMinecraft§a]§r ";

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

        getLogger().info("Plugin enabled.");
    }

    @Override
    public void onDisable() {

    }

    public String getPrefix() {
        return prefix;
    }

    public static BetterMinecraft getInstance() {
        return instance;
    }

    public RecipesManager getRecipesManager() {
        return recipesManager;
    }
}
