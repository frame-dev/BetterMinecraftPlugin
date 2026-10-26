package ch.framedev.betterminecraft.commands;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import ch.framedev.betterminecraft.utils.InventoryStringDeSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BackpackCommand implements CommandExecutor, Listener {

    private static final int BACKPACK_SIZE = 45;

    private final BetterMinecraft plugin;
    private final File backpackFile;
    private final YamlConfiguration backpackConfig = new YamlConfiguration();
    private final Map<UUID, BackpackHolder> backpacks = new HashMap<>();
    private final Set<UUID> unavailableBackpacks = new HashSet<>();

    public BackpackCommand(BetterMinecraft plugin) {
        this.plugin = plugin;
        this.backpackFile = new File(plugin.getDataFolder(), "backpacks.yml");

        try {
            if (!backpackFile.exists()) {
                backpackConfig.save(backpackFile);
            }
            backpackConfig.load(backpackFile);
        } catch (Exception e) {
            throw new IllegalStateException("Could not load backpacks.yml", e);
        }

        loadBackpacks();
    }

    private void loadBackpacks() {
        for (String key : backpackConfig.getKeys(false)) {
            UUID playerUUID;
            try {
                playerUUID = UUID.fromString(key);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid backpack UUID: " + key);
                continue;
            }

            try {
                String serializedInventory = backpackConfig.getString(key);
                if (serializedInventory == null) {
                    throw new IOException("Missing serialized inventory");
                }
                ItemStack[] contents = InventoryStringDeSerializer.itemStackArrayFromBase64(serializedInventory);
                if (contents.length != BACKPACK_SIZE) {
                    throw new IOException("Expected " + BACKPACK_SIZE + " slots, found " + contents.length);
                }
                BackpackHolder holder = new BackpackHolder(playerUUID);
                holder.getInventory().setContents(contents);
                backpacks.put(playerUUID, holder);
            } catch (IOException | RuntimeException e) {
                // Preserve broken backpack data instead of replacing it with an empty inventory.
                unavailableBackpacks.add(playerUUID);
                plugin.getLogger().severe("Could not load backpack for " + playerUUID + ": " + e.getMessage());
            }
        }
    }

    public void saveBackpacks() {
        for (BackpackHolder holder : backpacks.values()) {
            saveBackpackContents(holder);
        }
        saveFile();
    }

    private void saveBackpackContents(BackpackHolder holder) {
        String serializedInventory = InventoryStringDeSerializer.itemStackArrayToBase64(holder.getInventory().getContents());
        backpackConfig.set(holder.owner.toString(), serializedInventory);
    }

    private void saveFile() {
        try {
            backpackConfig.save(backpackFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save backpacks.yml: " + e.getMessage());
        }
    }

    public void shutdown() {
        for (BackpackHolder holder : backpacks.values()) {
            for (HumanEntity viewer : new ArrayList<>(holder.getInventory().getViewers())) {
                if (viewer != null) {
                    viewer.closeInventory();
                }
            }
        }
        saveBackpacks();
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder() instanceof BackpackHolder holder)) {
            return;
        }
        if (backpacks.get(holder.owner) != holder) {
            return;
        }
        saveBackpackContents(holder);
        saveFile();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!command.getName().equalsIgnoreCase("backpack")) {
            return false;
        }
        if(!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.only-player"));
            return true;
        }
        if(!player.hasPermission("betterminecraft.backpack")) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("general.no-permission"));
            return true;
        }
        if(args.length != 0) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("backpack.usage"));
            return true;
        }
        UUID playerUUID = player.getUniqueId();
        if (unavailableBackpacks.contains(playerUUID)) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessagesManager().getMessage("backpack.unavailable"));
            return true;
        }

        BackpackHolder holder = backpacks.get(playerUUID);
        if (holder == null) {
            holder = new BackpackHolder(playerUUID);
            backpacks.put(playerUUID, holder);
        }
        player.openInventory(holder.getInventory());
        return true;
    }

    private class BackpackHolder implements InventoryHolder {

        private final UUID owner;
        private final Inventory inventory;

        private BackpackHolder(UUID owner) {
            this.owner = owner;
            this.inventory = plugin.getServer().createInventory(this, BACKPACK_SIZE,
                    plugin.getMessagesManager().getMessage("backpack.title"));
        }

        @Override
        public @NotNull Inventory getInventory() {
            return inventory;
        }
    }
}
