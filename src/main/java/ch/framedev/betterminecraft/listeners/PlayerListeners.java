package ch.framedev.betterminecraft.listeners;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import ch.framedev.betterminecraft.managers.CraftHistory;
import ch.framedev.betterminecraft.utils.DoorPhysics;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Gate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRecipeBookClickEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerListeners implements Listener {

    private static final int NEARBY_CRAFTING_RADIUS = 5;
    private static final int TOOL_WARNING_DURABILITY = 5;
    private final NamespacedKey toolBreakWarningKey;

    private final BetterMinecraft plugin;
    private final DoorPhysics doorPhysics;
    private final CraftHistory craftHistory;

    public PlayerListeners(BetterMinecraft plugin) {
        this.plugin = plugin;
        this.doorPhysics = new DoorPhysics();
        this.craftHistory = plugin.getCraftHistory();
        this.toolBreakWarningKey = new NamespacedKey(
                plugin,
                "tool_break_warning"
        );
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK
                && plugin.getConfig().getBoolean("warn.beforeToolBreak", true)) {

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (!player.isOnline()) {
                    return;
                }

                warnToolBreak(
                        player,
                        player.getInventory().getItemInMainHand()
                );
            }, 1L);
        }

        Block clickedBlock = event.getClickedBlock();

        if (clickedBlock == null) {
            return;
        }

        if (clickedBlock.getType() == Material.CRAFTING_TABLE) {
            handleCraftingTable(event, player, clickedBlock);
        }

        handleDoorsAndGates(event, clickedBlock);
        handleCropHarvest(event, player, clickedBlock);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onRecipeBookClick(PlayerRecipeBookClickEvent event) {
        if (!plugin.getConfig().getBoolean("listeners.craftingTableCraftFromNearbyInventories", true)) {
            return;
        }

        Player player = event.getPlayer();

        Inventory topInventory = player.getOpenInventory().getTopInventory();

        if (!(topInventory instanceof CraftingInventory)) {
            return;
        }

        if (topInventory.getType() != InventoryType.WORKBENCH) {
            return;
        }

        Recipe recipe = event.getRecipe();

        if (!(recipe instanceof ShapedRecipe) && !(recipe instanceof ShapelessRecipe)) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> fillRecipeFromNearbyInventories(player, recipe), 1L);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerCraftItem(CraftItemEvent event) {
        if (!plugin.getConfig().getBoolean("others.collectCraftingHistory", true)) {
            return;
        }

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        Recipe recipe = event.getRecipe();

        if (recipe == null) {
            return;
        }

        craftHistory.addHistory(player, recipe);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!plugin.getConfig().getBoolean("warn.beforeToolBreak", true)) {
            return;
        }

        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!player.isOnline()) {
                return;
            }

            warnToolBreak(player, player.getInventory().getItemInMainHand());
        }, 1L);
    }

    private void handleCraftingTable(PlayerInteractEvent event, Player player, Block craftingTable) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (!plugin.getConfig().getBoolean("listeners.craftingTableDiscoverRecipes", true)) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!player.isOnline()) {
                return;
            }

            Inventory topInventory = player.getOpenInventory().getTopInventory();

            if (topInventory.getType() != InventoryType.WORKBENCH) {
                return;
            }

            Location location = topInventory.getLocation();

            if (location == null) {
                return;
            }

            Block currentCraftingTable = location.getBlock();

            if (currentCraftingTable.getType() != Material.CRAFTING_TABLE) {
                return;
            }

            plugin.getRecipesManager().updateAvailableRecipes(player, currentCraftingTable, NEARBY_CRAFTING_RADIUS);
        }, 1L);
    }

    private void fillRecipeFromNearbyInventories(Player player, Recipe recipe) {
        if (!player.isOnline()) {
            return;
        }

        Inventory topInventory = player.getOpenInventory().getTopInventory();

        if (!(topInventory instanceof CraftingInventory craftingInventory)) {
            return;
        }

        if (craftingInventory.getType() != InventoryType.WORKBENCH) {
            return;
        }

        Location location = craftingInventory.getLocation();

        if (location == null) {
            return;
        }

        Block craftingTable = location.getBlock();

        if (craftingTable.getType() != Material.CRAFTING_TABLE) {
            return;
        }

        List<Inventory> nearbyInventories = plugin.getRecipesManager().getNearbyInventories(craftingTable, NEARBY_CRAFTING_RADIUS);

        if (recipe instanceof ShapedRecipe shapedRecipe) {
            plugin.getRecipesManager().fillShapedRecipe(craftingInventory, shapedRecipe, nearbyInventories);
        } else if (recipe instanceof ShapelessRecipe shapelessRecipe) {
            plugin.getRecipesManager().fillShapelessRecipe(craftingInventory, shapelessRecipe, nearbyInventories);
        }

        player.updateInventory();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!player.isOnline()) {
                return;
            }

            if (craftingTable.getType() != Material.CRAFTING_TABLE) {
                return;
            }

            plugin.getRecipesManager().updateAvailableRecipes(player, craftingTable, NEARBY_CRAFTING_RADIUS);
        }, 1L);
    }

    private void handleDoorsAndGates(PlayerInteractEvent event, Block clickedBlock) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (clickedBlock.getBlockData() instanceof Door) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (clickedBlock.getBlockData() instanceof Door) {
                    doorPhysics.synchroniseDoor(clickedBlock);
                }
            }, 1L);

            return;
        }

        if (clickedBlock.getBlockData() instanceof Gate) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (clickedBlock.getBlockData() instanceof Gate) {
                    doorPhysics.synchroniseGate(clickedBlock);
                }
            }, 1L);
        }
    }

    private void handleCropHarvest(PlayerInteractEvent event, Player player, Block clickedBlock) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (!(clickedBlock.getBlockData() instanceof Ageable crop)) {
            return;
        }

        if (crop.getAge() != crop.getMaximumAge()) {
            return;
        }

        Material cropType = clickedBlock.getType();
        Material replantMaterial = getReplantMaterial(cropType);

        if (replantMaterial == null) {
            return;
        }

        event.setCancelled(true);

        dropCrop(clickedBlock, cropType);

        boolean replant = plugin.getConfig().getBoolean("listeners.replantCrops", true);

        if (!replant) {
            clickedBlock.setType(Material.AIR);
            return;
        }

        if (!removeItemFromInventory(player, replantMaterial)) {
            clickedBlock.setType(Material.AIR);
            return;
        }

        crop.setAge(0);
        clickedBlock.setBlockData(crop);
    }

    private Material getReplantMaterial(Material cropType) {
        return switch (cropType) {
            case WHEAT -> Material.WHEAT_SEEDS;
            case CARROTS -> Material.CARROT;
            case POTATOES -> Material.POTATO;
            case BEETROOTS -> Material.BEETROOT_SEEDS;
            case NETHER_WART -> Material.NETHER_WART;
            case COCOA -> Material.COCOA_BEANS;
            default -> null;
        };
    }

    private void dropCrop(Block block, Material cropType) {
        switch (cropType) {
            case WHEAT -> {
                dropItem(block, Material.WHEAT, 1);
                dropItem(block, Material.WHEAT_SEEDS, randomAmount(0, 3));
            }

            case CARROTS -> dropItem(block, Material.CARROT, randomAmount(2, 5));

            case POTATOES -> dropItem(block, Material.POTATO, randomAmount(2, 5));

            case BEETROOTS -> {
                dropItem(block, Material.BEETROOT, 1);
                dropItem(block, Material.BEETROOT_SEEDS, randomAmount(0, 3));
            }

            case NETHER_WART -> dropItem(block, Material.NETHER_WART, randomAmount(2, 4));

            case COCOA -> dropItem(block, Material.COCOA_BEANS, 3);

            default -> {
            }
        }
    }

    private void dropItem(Block block, Material material, int amount) {
        if (amount <= 0) {
            return;
        }

        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(material, amount));
    }

    private int randomAmount(int minimum, int maximum) {
        return ThreadLocalRandom.current().nextInt(minimum, maximum + 1);
    }

    public void warnToolBreak(Player player, ItemStack item) {
        if (!plugin.getConfig().getBoolean("warn.beforeToolBreak", true)) {
            return;
        }

        if (item == null || item.getType().isAir()) {
            return;
        }

        ItemMeta itemMeta = item.getItemMeta();

        if (!(itemMeta instanceof Damageable damageable)) {
            return;
        }

        int maxDurability = item.getType().getMaxDurability();

        if (maxDurability <= 0) {
            return;
        }

        int remainingDurability =
                maxDurability - damageable.getDamage();

        PersistentDataContainer data =
                itemMeta.getPersistentDataContainer();

        if (remainingDurability > TOOL_WARNING_DURABILITY) {
            if (data.has(
                    toolBreakWarningKey,
                    PersistentDataType.BYTE
            )) {
                data.remove(toolBreakWarningKey);
                item.setItemMeta(itemMeta);
            }

            return;
        }

        if (remainingDurability <= 0) {
            return;
        }

        if (data.has(
                toolBreakWarningKey,
                PersistentDataType.BYTE
        )) {
            return;
        }

        data.set(
                toolBreakWarningKey,
                PersistentDataType.BYTE,
                (byte) 1
        );

        item.setItemMeta(itemMeta);

        String message = plugin.getMessageFromConfig(
                "warn.beforeToolBreakMessage",
                "&cYour tool is about to break!"
        );

        player.sendMessage(
                plugin.getPrefix() + message
        );
    }

    private boolean removeItemFromInventory(Player player, Material material) {
        ItemStack required = new ItemStack(material, 1);

        if (!player.getInventory().containsAtLeast(required, 1)) {
            return false;
        }

        player.getInventory().removeItem(required);

        return true;
    }
}