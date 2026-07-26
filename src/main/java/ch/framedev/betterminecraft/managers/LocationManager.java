package ch.framedev.betterminecraft.managers;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.io.File;

public class LocationManager {

    private final File locationsFile;
    private final FileConfiguration locationsConfig;
    private final BetterMinecraft plugin = BetterMinecraft.getInstance();

    public LocationManager() {
        this.locationsFile = new File(plugin.getDataFolder(), "locations.yml");

        if(!locationsFile.exists()) {
            try {
                locationsFile.createNewFile();
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, e.getMessage(), e);
            }
        }

        this.locationsConfig = YamlConfiguration.loadConfiguration(locationsFile);
        load();
    }

    private boolean save() {
        try {
            locationsConfig.save(locationsFile);
            return true;
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
        return false;
    }

    private void load() {
        try {
            locationsConfig.load(locationsFile);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public boolean containsLocation(String key) {
        if (!locationsConfig.contains(key)) return false;
        String worldString = locationsConfig.getString(key + ".world");
        World world = Bukkit.getWorld(worldString);
        if (world == null) return false;
        return locationsConfig.contains(key + ".x") && locationsConfig.contains(key + ".y") && locationsConfig.contains(key + ".z");
    }

    public boolean setLocation(String key, Location location) {
        validateKey(key);
        Objects.requireNonNull(location, "Location cannot be null");
        World world = location.getWorld();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float yaw = location.getYaw();
        float pitch = location.getPitch();

        if (world == null) {
            throw new IllegalArgumentException("Location must have a world");
        }

        locationsConfig.set(key + ".world", world.getName());
        locationsConfig.set(key + ".x", x);
        locationsConfig.set(key + ".y", y);
        locationsConfig.set(key + ".z", z);
        locationsConfig.set(key + ".yaw", yaw);
        locationsConfig.set(key + ".pitch", pitch);
        return save();
    }

    public Location getLocation(String key) {
        validateKey(key);

        if(!locationsConfig.contains(key + ".world")) return null;

        String worldString = locationsConfig.getString(key + ".world");
        World world = Bukkit.getWorld(worldString);
        if (world == null) return null;

        if (!locationsConfig.contains(key + ".x") && !locationsConfig.contains(key + ".y") && !locationsConfig.contains(key + ".z"))
            return null;

        double x = locationsConfig.getDouble(key + ".x");
        double y = locationsConfig.getDouble(key + ".y");
        double z = locationsConfig.getDouble(key + ".z");
        float yaw = (float) locationsConfig.getDouble(key + ".yaw");
        float pitch = (float) locationsConfig.getDouble(key + ".pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }

    public boolean removeLocation(String key) {
        if (!locationsConfig.contains(key)) return false;
        locationsConfig.set(key, null);
        return save();
    }

    public List<String> getLocationNames(String key) {
        if (!locationsConfig.contains(key)) return null;
        ConfigurationSection section = locationsConfig.getConfigurationSection(key);
        if(section == null) return new ArrayList<>();
        return section.getKeys(false).stream().toList();
    }

    private void validateKey(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Location key must not be null or blank");
        }
    }
}
