package ch.framedev.betterminecraft.managers;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CraftHistory implements Listener {

    private static final int INVENTORY_SIZE = 54;
    private static final int PAGE_SIZE = 45;

    private static final int PREVIOUS_PAGE_SLOT = 45;
    private static final int FILTER_SLOT = 47;
    private static final int CLOSE_SLOT = 49;
    private static final int NEXT_PAGE_SLOT = 53;

    private final BetterMinecraft plugin;

    private final File craftHistoryFile;
    private final FileConfiguration craftHistoryConfig;

    public CraftHistory() {
        this.plugin = BetterMinecraft.getInstance();

        craftHistoryFile = new File(plugin.getDataFolder(), "craft_history.yml");

        if (!craftHistoryFile.exists()) {
            try {
                File parent = craftHistoryFile.getParentFile();

                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }

                craftHistoryFile.createNewFile();

            } catch (IOException e) {
                throw new RuntimeException("Could not create craft_history.yml", e);
            }
        }

        craftHistoryConfig = YamlConfiguration.loadConfiguration(craftHistoryFile);
    }

    public void addHistory(Player player, Recipe recipe) {
        if (!(recipe instanceof Keyed keyed)) {
            return;
        }

        String playerId = player.getUniqueId().toString();

        String playerName = player.getName();

        String recipeKey = keyed.getKey().toString();

        String path = "players." + playerId + ".recipes." + recipeKey;

        long lastCrafted = System.currentTimeMillis();

        craftHistoryConfig.set(path + ".playerName", playerName);

        craftHistoryConfig.set(path + ".lastCrafted", lastCrafted);

        int craftedAmount = craftHistoryConfig.getInt(path + ".amount", 0);

        craftHistoryConfig.set(path + ".amount", craftedAmount + recipe.getResult().getAmount());

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        craftHistoryConfig.set(path + ".lastCraftedFormatted", sdf.format(new Date(lastCrafted)));

        save();
    }

    public List<CraftHistoryEntry> getPlayerHistory(Player player) {
        return getPlayerHistory(player.getUniqueId());
    }

    public List<CraftHistoryEntry> getPlayerHistory(UUID playerId) {
        ConfigurationSection section = craftHistoryConfig.getConfigurationSection("players." + playerId + ".recipes");

        if (section == null) {
            return List.of();
        }

        List<CraftHistoryEntry> history = new ArrayList<>();

        for (String recipeName : section.getKeys(false)) {
            ConfigurationSection recipeSection = section.getConfigurationSection(recipeName);

            if (recipeSection == null) {
                continue;
            }

            NamespacedKey recipeKey = NamespacedKey.fromString(recipeName);

            if (recipeKey == null) {
                continue;
            }

            String playerName = recipeSection.getString("playerName", "Unknown");

            String lastCraftedFormatted = recipeSection.getString("lastCraftedFormatted", "Unknown");

            long lastCrafted = recipeSection.getLong("lastCrafted");

            int amount = recipeSection.getInt("amount");

            history.add(new CraftHistoryEntry(playerName, recipeKey, lastCraftedFormatted, lastCrafted, amount));
        }

        return history;
    }

    public void showCraftingHistory(Player viewer, Player target) {
        showCraftingHistory(viewer, target.getUniqueId(), target.getName(), 0, CraftHistoryFilter.LAST_CRAFTED_NEWEST);
    }

    public void showCraftingHistory(Player viewer, Player target, int page) {
        showCraftingHistory(viewer, target.getUniqueId(), target.getName(), page, CraftHistoryFilter.LAST_CRAFTED_NEWEST);
    }

    public void showCraftingHistory(Player viewer, UUID targetId, String targetName, int page, CraftHistoryFilter filter) {
        List<CraftHistoryEntry> history = new ArrayList<>(getPlayerHistory(targetId));

        if (history.isEmpty()) {
            viewer.sendMessage(plugin.getPrefix() + "§c" + targetName + " has no crafting history.");

            return;
        }

        sortHistory(history, filter);

        int maxPage = Math.max(0, (history.size() - 1) / PAGE_SIZE);

        page = Math.max(0, Math.min(page, maxPage));

        CraftHistoryHolder holder = new CraftHistoryHolder(targetId, targetName, page, filter);

        Inventory inventory = Bukkit.createInventory(holder, INVENTORY_SIZE, "§aCrafting History §f" + targetName + " §7(" + (page + 1) + "/" + (maxPage + 1) + ")");

        int startIndex = page * PAGE_SIZE;

        int endIndex = Math.min(startIndex + PAGE_SIZE, history.size());

        int inventorySlot = 0;

        for (int index = startIndex; index < endIndex; index++) {
            CraftHistoryEntry entry = history.get(index);

            Recipe recipe = Bukkit.getRecipe(entry.recipe());

            if (recipe == null) {
                continue;
            }

            ItemStack displayItem = recipe.getResult().clone();

            displayItem.setAmount(1);

            ItemMeta meta = displayItem.getItemMeta();

            if (meta != null) {
                meta.setLore(List.of("§7Recipe: §f" + entry.recipe(), "", "§7Total crafted: §a" + entry.amount() + "x", "§7Last crafted: §e" + entry.lastCraftedFormatted()));

                displayItem.setItemMeta(meta);
            }

            inventory.setItem(inventorySlot++, displayItem);
        }

        ItemStack filler = createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " ");

        for (int slot = PAGE_SIZE; slot < INVENTORY_SIZE; slot++) {
            inventory.setItem(slot, filler);
        }

        if (page > 0) {
            inventory.setItem(PREVIOUS_PAGE_SLOT, createGuiItem(Material.ARROW, "§ePrevious Page", "", "§7Page §f" + page));
        }

        inventory.setItem(FILTER_SLOT, createFilterItem(filter));

        inventory.setItem(CLOSE_SLOT, createGuiItem(Material.BARRIER, "§cClose", "", "§7Click to close."));

        if (page < maxPage) {
            inventory.setItem(NEXT_PAGE_SLOT, createGuiItem(Material.ARROW, "§eNext Page", "", "§7Page §f" + (page + 2)));
        }

        viewer.openInventory(inventory);
    }

    public void sortHistory(List<CraftHistoryEntry> history, CraftHistoryFilter filter) {
        switch (filter) {

            case LAST_CRAFTED_NEWEST ->
                    history.sort(Comparator.comparingLong(CraftHistoryEntry::lastCrafted).reversed());

            case LAST_CRAFTED_OLDEST -> history.sort(Comparator.comparingLong(CraftHistoryEntry::lastCrafted));

            case AMOUNT_HIGHEST -> history.sort(Comparator.comparingInt(CraftHistoryEntry::amount).reversed());

            case AMOUNT_LOWEST -> history.sort(Comparator.comparingInt(CraftHistoryEntry::amount));
        }
    }

    private ItemStack createFilterItem(CraftHistoryFilter filter) {
        String selected = switch (filter) {

            case LAST_CRAFTED_NEWEST -> "§aLast Crafted: Newest";

            case LAST_CRAFTED_OLDEST -> "§aLast Crafted: Oldest";

            case AMOUNT_HIGHEST -> "§aAmount: Highest";

            case AMOUNT_LOWEST -> "§aAmount: Lowest";
        };

        return createGuiItem(Material.HOPPER, "§eSort / Filter", "", "§7Current:", selected, "", "§eClick to change", "", getFilterLine(filter, CraftHistoryFilter.LAST_CRAFTED_NEWEST, "Last Crafted: Newest"), getFilterLine(filter, CraftHistoryFilter.LAST_CRAFTED_OLDEST, "Last Crafted: Oldest"), getFilterLine(filter, CraftHistoryFilter.AMOUNT_HIGHEST, "Amount: Highest"), getFilterLine(filter, CraftHistoryFilter.AMOUNT_LOWEST, "Amount: Lowest"));
    }

    private String getFilterLine(CraftHistoryFilter current, CraftHistoryFilter filter, String name) {
        if (current == filter) {
            return "§a➤ " + name;
        }

        return "§7  " + name;
    }

    private ItemStack createGuiItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);

        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(name);

            if (lore.length > 0) {
                meta.setLore(Arrays.asList(lore));
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    @EventHandler
    public void onCraftHistoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if (!(event.getView().getTopInventory().getHolder() instanceof CraftHistoryHolder holder)) {
            return;
        }

        event.setCancelled(true);

        int rawSlot = event.getRawSlot();

        if (rawSlot < 0) {
            return;
        }

        if (rawSlot >= event.getView().getTopInventory().getSize()) {
            return;
        }

        UUID targetId = holder.getTargetId();

        String targetName = holder.getTargetName();

        int currentPage = holder.getPage();

        CraftHistoryFilter filter = holder.getFilter();

        if (rawSlot == CLOSE_SLOT) {
            player.closeInventory();
            return;
        }

        if (rawSlot == FILTER_SLOT) {
            CraftHistoryFilter nextFilter = filter.next();

            showCraftingHistory(player, targetId, targetName, 0, nextFilter);

            return;
        }

        if (rawSlot == PREVIOUS_PAGE_SLOT) {
            if (currentPage <= 0) {
                return;
            }

            showCraftingHistory(player, targetId, targetName, currentPage - 1, filter);

            return;
        }

        if (rawSlot == NEXT_PAGE_SLOT) {
            List<CraftHistoryEntry> history = getPlayerHistory(targetId);

            int maxPage = Math.max(0, (history.size() - 1) / PAGE_SIZE);

            if (currentPage >= maxPage) {
                return;
            }

            showCraftingHistory(player, targetId, targetName, currentPage + 1, filter);
        }
    }

    private void save() {
        try {
            craftHistoryConfig.save(craftHistoryFile);

        } catch (IOException e) {
            plugin.getLogger().severe("Could not save craft_history.yml: " + e.getMessage());

            e.printStackTrace();
        }
    }

    public record CraftHistoryEntry(String playerName, NamespacedKey recipe, String lastCraftedFormatted,
                                    long lastCrafted, int amount) {
    }
}