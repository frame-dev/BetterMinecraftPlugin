package ch.framedev.betterminecraft;

import org.bukkit.plugin.java.JavaPlugin;

public final class BetterMinecraft extends JavaPlugin {

    private RecipesManager recipesManager;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        this.recipesManager = new RecipesManager(this);
        recipesManager.init();
        this.getServer().getPluginManager().registerEvents(new PlayerListeners(this), this);
        getLogger().info("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public RecipesManager getRecipesManager() {
        return recipesManager;
    }
}
