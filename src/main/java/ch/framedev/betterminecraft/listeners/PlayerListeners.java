package ch.framedev.betterminecraft.listeners;

import ch.framedev.betterminecraft.utils.DoorPhysics;
import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Gate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class PlayerListeners implements Listener {

    private final BetterMinecraft plugin;

    public PlayerListeners(BetterMinecraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteractBlock(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() == Material.CRAFTING_TABLE) {

            for (ShapedRecipe shapedRecipe : plugin.getRecipesManager().getRecipes()) {
                if (plugin.getRecipesManager().canCraft(event.getPlayer(), shapedRecipe)) {
                    event.getPlayer().discoverRecipe(shapedRecipe.getKey());
                }
            }
        }
        Block clickedBlock = event.getClickedBlock();
        DoorPhysics doorPhysics = new DoorPhysics();
        if (clickedBlock == null) return;
        if (clickedBlock.getBlockData() instanceof Door) {
            Bukkit.getScheduler().runTask(plugin, () -> doorPhysics.synchroniseDoor(clickedBlock));
        }
        if (clickedBlock.getBlockData() instanceof Gate) {
            Bukkit.getScheduler().runTask(plugin, () -> doorPhysics.synchroniseGate(clickedBlock));
        }

        if (event.getClickedBlock().getBlockData() instanceof Ageable crop) {
            if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
            if (crop.getAge() == crop.getMaximumAge()) {
                World world = event.getClickedBlock().getWorld();
                Material type = clickedBlock.getType();
                Random random = new Random();
                int randomInt = random.nextInt(1, 3);

                switch (type) {
                    case WHEAT -> {
                        world.dropItemNaturally(clickedBlock.getLocation(), new ItemStack(Material.WHEAT));
                        if (!plugin.getConfig().getBoolean("listeners.replantCrops"))
                            clickedBlock.setType(Material.AIR);
                        else {
                            Player player = event.getPlayer();
                            if (removeItemFromInventory(player, Material.WHEAT_SEEDS)) {
                                crop.setAge(0);
                                clickedBlock.setBlockData(crop);
                            } else {
                                clickedBlock.setType(Material.AIR);
                            }
                        }
                    }
                    case CARROTS -> {
                        world.dropItemNaturally(clickedBlock.getLocation(), new ItemStack(Material.CARROT, randomInt));
                        if (!plugin.getConfig().getBoolean("listeners.replantCrops"))
                            clickedBlock.setType(Material.AIR);
                        else {
                            Player player = event.getPlayer();
                            if (removeItemFromInventory(player, Material.CARROT)) {
                                crop.setAge(0);
                                clickedBlock.setBlockData(crop);
                            } else {
                                clickedBlock.setType(Material.AIR);
                            }
                        }
                    }
                    case POTATOES -> {
                        world.dropItemNaturally(clickedBlock.getLocation(), new ItemStack(Material.POTATO, randomInt));
                        if (!plugin.getConfig().getBoolean("listeners.replantCrops"))
                            clickedBlock.setType(Material.AIR);
                        else {
                            crop.setAge(0);
                            clickedBlock.setBlockData(crop);
                            Player player = event.getPlayer();
                            removeItemFromInventory(player, Material.POTATO);
                        }
                    }
                    case BEETROOTS -> {
                        world.dropItemNaturally(clickedBlock.getLocation(), new ItemStack(Material.BEETROOT));
                        if (!plugin.getConfig().getBoolean("listeners.replantCrops"))
                            clickedBlock.setType(Material.AIR);
                        else {
                            Player player = event.getPlayer();
                            if (removeItemFromInventory(player, Material.BEETROOT_SEEDS)) {
                                crop.setAge(0);
                                clickedBlock.setBlockData(crop);
                            } else {
                                clickedBlock.setType(Material.AIR);
                            }
                        }
                    }
                    case NETHER_WART -> {
                        world.dropItemNaturally(clickedBlock.getLocation(), new ItemStack(Material.NETHER_WART, randomInt));
                        if (!plugin.getConfig().getBoolean("listeners.replantCrops"))
                            clickedBlock.setType(Material.AIR);
                        else {
                            Player player = event.getPlayer();
                            if (removeItemFromInventory(player, Material.NETHER_WART)) {
                                crop.setAge(0);
                                clickedBlock.setBlockData(crop);
                            } else {
                                clickedBlock.setType(Material.AIR);
                            }
                        }
                    }
                    case COCOA -> {
                        world.dropItemNaturally(clickedBlock.getLocation(), new ItemStack(Material.COCOA_BEANS));
                        if (!plugin.getConfig().getBoolean("listeners.replantCrops"))
                            clickedBlock.setType(Material.AIR);
                        else {
                            Player player = event.getPlayer();
                            if (removeItemFromInventory(player, Material.COCOA_BEANS)) {
                                crop.setAge(0);
                                clickedBlock.setBlockData(crop);
                            } else {
                                clickedBlock.setType(Material.AIR);
                            }
                        }
                    }
                }
            }
        }

        warnToolBreak(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand());
    }

    public void warnToolBreak(Player player, ItemStack item) {
        if (item.getType().isAir()) return;
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;
        if (!(itemMeta instanceof Damageable damageable) || !damageable.hasMaxDamage()) {
            return;
        }
        int remainingDurability = damageable.getMaxDamage() - damageable.getDamage();
        if(remainingDurability <= 5) {
            String message = plugin.getMessageFromConfig("warn.beforeToolBreakMessage", "&cYour tool is about to break!");
            player.sendMessage(plugin.getPrefix() + message);
        }
    }

    private boolean removeItemFromInventory(Player player, Material material) {
        if (player.getInventory().containsAtLeast(new ItemStack(material), 1)) {
            player.getInventory().removeItem(new ItemStack(material, 1));
            return true;
        }
        return false;
    }
}
