package ch.framedev.betterminecraft.managers;

import ch.framedev.betterminecraft.main.BetterMinecraft;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RecipesManager {

    private final BetterMinecraft plugin;

    private final List<ShapedRecipe> recipes = new ArrayList<>();

    public RecipesManager(BetterMinecraft plugin) {
        this.plugin = plugin;
    }

    /**
     * Initializes all custom recipes.
     */
    public void init() {
        List<Boolean> recipesResultList = setupCrafting();

        long successfulCount = recipesResultList.stream().filter(Boolean::booleanValue).count();

        long failedCount = recipesResultList.size() - successfulCount;

        plugin.getLogger().info("Recipes setup completed with " + successfulCount + " successful recipes and " + failedCount + " failed recipes.");
    }

    /**
     * Registers all BetterMinecraft recipes.
     *
     * @return registration results
     */
    private List<Boolean> setupCrafting() {
        List<Boolean> success = new ArrayList<>();

        /*
         * =========================================================
         * Granite Tools
         * =========================================================
         */

        ShapedRecipe recipeGraniteSword = new ShapedRecipe(new NamespacedKey(plugin, "better_minecraft_recipe_granite_sword"), new ItemStack(Material.STONE_SWORD));

        recipeGraniteSword.shape(" G ", " G ", " S ");

        recipeGraniteSword.setIngredient('G', Material.GRANITE);

        recipeGraniteSword.setIngredient('S', Material.STICK);

        success.add(plugin.getServer().addRecipe(recipeGraniteSword));

        recipes.add(recipeGraniteSword);


        ShapedRecipe recipeGranitePickaxe = new ShapedRecipe(new NamespacedKey(plugin, "better_minecraft_recipe_granite_pickaxe"), new ItemStack(Material.STONE_PICKAXE));

        recipeGranitePickaxe.shape("GGG", " S ", " S ");

        recipeGranitePickaxe.setIngredient('G', Material.GRANITE);

        recipeGranitePickaxe.setIngredient('S', Material.STICK);

        success.add(plugin.getServer().addRecipe(recipeGranitePickaxe));

        recipes.add(recipeGranitePickaxe);


        ShapedRecipe recipeGraniteAxe = new ShapedRecipe(new NamespacedKey(plugin, "better_minecraft_recipe_granite_axe"), new ItemStack(Material.STONE_AXE));

        recipeGraniteAxe.shape("GG ", "GS ", " S ");

        recipeGraniteAxe.setIngredient('G', Material.GRANITE);

        recipeGraniteAxe.setIngredient('S', Material.STICK);

        success.add(plugin.getServer().addRecipe(recipeGraniteAxe));

        recipes.add(recipeGraniteAxe);


        ShapedRecipe recipeGraniteShovel = new ShapedRecipe(new NamespacedKey(plugin, "better_minecraft_recipe_granite_shovel"), new ItemStack(Material.STONE_SHOVEL));

        recipeGraniteShovel.shape(" G ", " S ", " S ");

        recipeGraniteShovel.setIngredient('G', Material.GRANITE);

        recipeGraniteShovel.setIngredient('S', Material.STICK);

        success.add(plugin.getServer().addRecipe(recipeGraniteShovel));

        recipes.add(recipeGraniteShovel);


        ShapedRecipe recipeGraniteHoe = new ShapedRecipe(new NamespacedKey(plugin, "better_minecraft_recipe_granite_hoe"), new ItemStack(Material.STONE_HOE));

        recipeGraniteHoe.shape("GG ", " S ", " S ");

        recipeGraniteHoe.setIngredient('G', Material.GRANITE);

        recipeGraniteHoe.setIngredient('S', Material.STICK);

        success.add(plugin.getServer().addRecipe(recipeGraniteHoe));

        recipes.add(recipeGraniteHoe);

        /*
         * =========================================================
         * Andesite Tools
         * =========================================================
         */

        ShapedRecipe recipeAndesiteSword = new ShapedRecipe(new NamespacedKey(plugin, "better_minecraft_recipe_andesite_sword"), new ItemStack(Material.STONE_SWORD));

        recipeAndesiteSword.shape(" G ", " G ", " S ");

        recipeAndesiteSword.setIngredient('G', Material.ANDESITE);

        recipeAndesiteSword.setIngredient('S', Material.STICK);

        success.add(plugin.getServer().addRecipe(recipeAndesiteSword));

        recipes.add(recipeAndesiteSword);


        ShapedRecipe recipeAndesiteAxe = new ShapedRecipe(new NamespacedKey(plugin, "better_minecraft_recipe_andesite_axe"), new ItemStack(Material.STONE_AXE));

        recipeAndesiteAxe.shape("GG ", "GS ", " S ");

        recipeAndesiteAxe.setIngredient('G', Material.ANDESITE);

        recipeAndesiteAxe.setIngredient('S', Material.STICK);

        success.add(plugin.getServer().addRecipe(recipeAndesiteAxe));

        recipes.add(recipeAndesiteAxe);


        ShapedRecipe recipeAndesitePickaxe = new ShapedRecipe(new NamespacedKey(plugin, "better_minecraft_recipe_andesite_pickaxe"), new ItemStack(Material.STONE_PICKAXE));

        recipeAndesitePickaxe.shape("GGG", " S ", " S ");

        recipeAndesitePickaxe.setIngredient('G', Material.ANDESITE);

        recipeAndesitePickaxe.setIngredient('S', Material.STICK);

        success.add(plugin.getServer().addRecipe(recipeAndesitePickaxe));

        recipes.add(recipeAndesitePickaxe);


        ShapedRecipe recipeAndesiteHoe = new ShapedRecipe(new NamespacedKey(plugin, "better_minecraft_recipe_andesite_hoe"), new ItemStack(Material.STONE_HOE));

        recipeAndesiteHoe.shape("GG ", " S ", " S ");

        recipeAndesiteHoe.setIngredient('G', Material.ANDESITE);

        recipeAndesiteHoe.setIngredient('S', Material.STICK);

        success.add(plugin.getServer().addRecipe(recipeAndesiteHoe));

        recipes.add(recipeAndesiteHoe);


        ShapedRecipe recipeAndesiteShovel = new ShapedRecipe(new NamespacedKey(plugin, "better_minecraft_recipe_andesite_shovel"), new ItemStack(Material.STONE_SHOVEL));

        recipeAndesiteShovel.shape(" G ", " S ", " S ");

        recipeAndesiteShovel.setIngredient('G', Material.ANDESITE);

        recipeAndesiteShovel.setIngredient('S', Material.STICK);

        success.add(plugin.getServer().addRecipe(recipeAndesiteShovel));

        recipes.add(recipeAndesiteShovel);

        /*
         * =========================================================
         * Diorite Tools
         * =========================================================
         */

        ShapedRecipe recipeDioriteSword = new ShapedRecipe(new NamespacedKey(plugin, "better_minecraft_recipe_diorite_sword"), new ItemStack(Material.STONE_SWORD));

        recipeDioriteSword.shape(" G ", " G ", " S ");

        recipeDioriteSword.setIngredient('G', Material.DIORITE);

        recipeDioriteSword.setIngredient('S', Material.STICK);

        success.add(plugin.getServer().addRecipe(recipeDioriteSword));

        recipes.add(recipeDioriteSword);


        ShapedRecipe recipeDioriteAxe = new ShapedRecipe(new NamespacedKey(plugin, "better_minecraft_recipe_diorite_axe"), new ItemStack(Material.STONE_AXE));

        recipeDioriteAxe.shape("GG ", "GS ", " S ");

        recipeDioriteAxe.setIngredient('G', Material.DIORITE);

        recipeDioriteAxe.setIngredient('S', Material.STICK);

        success.add(plugin.getServer().addRecipe(recipeDioriteAxe));

        recipes.add(recipeDioriteAxe);


        ShapedRecipe recipeDioritePickaxe = new ShapedRecipe(new NamespacedKey(plugin, "better_minecraft_recipe_diorite_pickaxe"), new ItemStack(Material.STONE_PICKAXE));

        recipeDioritePickaxe.shape("GGG", " S ", " S ");

        recipeDioritePickaxe.setIngredient('G', Material.DIORITE);

        recipeDioritePickaxe.setIngredient('S', Material.STICK);

        success.add(plugin.getServer().addRecipe(recipeDioritePickaxe));

        recipes.add(recipeDioritePickaxe);


        ShapedRecipe recipeDioriteShovel = new ShapedRecipe(new NamespacedKey(plugin, "better_minecraft_recipe_diorite_shovel"), new ItemStack(Material.STONE_SHOVEL));

        recipeDioriteShovel.shape(" G ", " S ", " S ");

        recipeDioriteShovel.setIngredient('G', Material.DIORITE);

        recipeDioriteShovel.setIngredient('S', Material.STICK);

        success.add(plugin.getServer().addRecipe(recipeDioriteShovel));

        recipes.add(recipeDioriteShovel);


        ShapedRecipe recipeDioriteHoe = new ShapedRecipe(new NamespacedKey(plugin, "better_minecraft_recipe_diorite_hoe"), new ItemStack(Material.STONE_HOE));

        recipeDioriteHoe.shape("GG ", " S ", " S ");

        recipeDioriteHoe.setIngredient('G', Material.DIORITE);

        recipeDioriteHoe.setIngredient('S', Material.STICK);

        success.add(plugin.getServer().addRecipe(recipeDioriteHoe));

        recipes.add(recipeDioriteHoe);

        /*
         * =========================================================
         * Tuff Tools
         * =========================================================
         */

        ShapedRecipe recipeTuffSword = new ShapedRecipe(new NamespacedKey(plugin, "better_minecraft_recipe_tuff_sword"), new ItemStack(Material.STONE_SWORD));

        recipeTuffSword.shape(" G ", " G ", " S ");

        recipeTuffSword.setIngredient('G', Material.TUFF);

        recipeTuffSword.setIngredient('S', Material.STICK);

        success.add(plugin.getServer().addRecipe(recipeTuffSword));

        recipes.add(recipeTuffSword);


        ShapedRecipe recipeTuffAxe = new ShapedRecipe(new NamespacedKey(plugin, "better_minecraft_recipe_tuff_axe"), new ItemStack(Material.STONE_AXE));

        recipeTuffAxe.shape("GG ", "GS ", " S ");

        recipeTuffAxe.setIngredient('G', Material.TUFF);

        recipeTuffAxe.setIngredient('S', Material.STICK);

        success.add(plugin.getServer().addRecipe(recipeTuffAxe));

        recipes.add(recipeTuffAxe);


        ShapedRecipe recipeTuffPickaxe = new ShapedRecipe(new NamespacedKey(plugin, "better_minecraft_recipe_tuff_pickaxe"), new ItemStack(Material.STONE_PICKAXE));

        recipeTuffPickaxe.shape("GGG", " S ", " S ");

        recipeTuffPickaxe.setIngredient('G', Material.TUFF);

        recipeTuffPickaxe.setIngredient('S', Material.STICK);

        success.add(plugin.getServer().addRecipe(recipeTuffPickaxe));

        recipes.add(recipeTuffPickaxe);


        ShapedRecipe recipeTuffShovel = new ShapedRecipe(new NamespacedKey(plugin, "better_minecraft_recipe_tuff_shovel"), new ItemStack(Material.STONE_SHOVEL));

        recipeTuffShovel.shape(" G ", " S ", " S ");

        recipeTuffShovel.setIngredient('G', Material.TUFF);

        recipeTuffShovel.setIngredient('S', Material.STICK);

        success.add(plugin.getServer().addRecipe(recipeTuffShovel));

        recipes.add(recipeTuffShovel);


        ShapedRecipe recipeTuffHoe = new ShapedRecipe(new NamespacedKey(plugin, "better_minecraft_recipe_tuff_hoe"), new ItemStack(Material.STONE_HOE));

        recipeTuffHoe.shape("GG ", " S ", " S ");

        recipeTuffHoe.setIngredient('G', Material.TUFF);

        recipeTuffHoe.setIngredient('S', Material.STICK);

        success.add(plugin.getServer().addRecipe(recipeTuffHoe));

        recipes.add(recipeTuffHoe);

        return success;
    }

    /**
     * Returns BetterMinecraft's custom shaped recipes.
     */
    public List<ShapedRecipe> getRecipes() {
        return List.copyOf(recipes);
    }

    /*
     * =========================================================
     * PLAYER-INVENTORY CRAFTABILITY
     * =========================================================
     */

    public boolean canCraft(Player player, ShapelessRecipe recipe) {
        List<ItemStack> availableItems = getPlayerItems(player);

        return canCraft(availableItems, recipe);
    }

    public boolean canCraft(Player player, ShapedRecipe recipe) {
        List<ItemStack> availableItems = getPlayerItems(player);

        return canCraft(availableItems, recipe);
    }

    /*
     * =========================================================
     * COMBINED INVENTORY CRAFTABILITY
     * =========================================================
     */

    public boolean canCraft(List<ItemStack> availableItems, ShapedRecipe recipe) {
        List<RecipeChoice> requiredIngredients = new ArrayList<>();

        Map<Character, RecipeChoice> choiceMap = recipe.getChoiceMap();

        for (String row : recipe.getShape()) {

            for (char ingredientKey : row.toCharArray()) {

                if (ingredientKey == ' ') {
                    continue;
                }

                RecipeChoice choice = choiceMap.get(ingredientKey);

                if (choice != null) {
                    requiredIngredients.add(choice);
                }
            }
        }

        return hasIngredients(availableItems, requiredIngredients);
    }

    public boolean canCraft(List<ItemStack> availableItems, ShapelessRecipe recipe) {
        return hasIngredients(availableItems, recipe.getChoiceList());
    }

    /**
     * Simulates consuming one item for every required RecipeChoice.
     * <p>
     * Nothing is actually removed from the real inventories.
     */
    public boolean hasIngredients(List<ItemStack> availableItems, List<RecipeChoice> requiredIngredients) {
        List<ItemStack> simulatedInventory = cloneItems(availableItems);

        for (RecipeChoice choice : requiredIngredients) {

            boolean found = false;

            for (int i = 0; i < simulatedInventory.size(); i++) {
                ItemStack item = simulatedInventory.get(i);

                if (item == null || item.getType().isAir()) {
                    continue;
                }

                if (!choice.test(item)) {
                    continue;
                }

                /*
                 * Simulate consuming exactly one item.
                 */
                if (item.getAmount() <= 1) {
                    simulatedInventory.remove(i);
                } else {
                    item.setAmount(item.getAmount() - 1);
                }

                found = true;
                break;
            }

            if (!found) {
                return false;
            }
        }

        return true;
    }

    /*
     * =========================================================
     * RECIPE BOOK
     * =========================================================
     */

    /**
     * Discovers every shaped/shapeless recipe that can be made using
     * the player inventory + nearby inventories.
     * <p>
     * Note:
     * discoverRecipe() only makes the recipe known to Minecraft.
     * Vanilla still decides the red/green recipe-book state using its
     * own available-item logic.
     */
    public void updateAvailableRecipes(Player player, Block craftingTable, int radius) {
        List<ItemStack> availableItems = getAvailableCraftingItems(player, craftingTable, radius);

        Iterator<Recipe> iterator = Bukkit.recipeIterator();

        while (iterator.hasNext()) {
            Recipe recipe = iterator.next();

            boolean craftable;

            if (recipe instanceof ShapedRecipe shapedRecipe) {

                craftable = canCraft(availableItems, shapedRecipe);

            } else if (recipe instanceof ShapelessRecipe shapelessRecipe) {

                craftable = canCraft(availableItems, shapelessRecipe);

            } else {
                continue;
            }

            if (!craftable) {
                continue;
            }

            if (recipe instanceof Keyed keyed) {
                player.discoverRecipe(keyed.getKey());
            }
        }
    }

    /**
     * Compatibility method if you're already calling this elsewhere.
     */
    public void discoverNearbyCraftableRecipes(Player player, Block craftingTable, int radius) {
        updateAvailableRecipes(player, craftingTable, radius);
    }

    /*
     * =========================================================
     * SHAPED RECIPE FILLING
     * =========================================================
     */

    /**
     * Fills missing shaped-recipe slots using nearby inventories.
     * <p>
     * Items already placed into the crafting matrix by vanilla are
     * preserved.
     */
    public void fillShapedRecipe(CraftingInventory craftingInventory, ShapedRecipe recipe, List<Inventory> nearbyInventories) {
        ItemStack[] matrix = craftingInventory.getMatrix();

        String[] shape = recipe.getShape();

        Map<Character, RecipeChoice> choices = recipe.getChoiceMap();

        /*
         * Recipe-book placement normally uses the top-left recipe
         * position, so map the shape directly into the 3x3 matrix.
         */
        for (int row = 0; row < shape.length; row++) {

            String recipeRow = shape[row];

            for (int column = 0; column < recipeRow.length(); column++) {
                char character = recipeRow.charAt(column);

                if (character == ' ') {
                    continue;
                }

                RecipeChoice choice = choices.get(character);

                if (choice == null) {
                    continue;
                }

                int matrixSlot = row * 3 + column;

                if (matrixSlot < 0 || matrixSlot >= matrix.length) {
                    continue;
                }

                ItemStack current = matrix[matrixSlot];

                /*
                 * Vanilla already placed a valid ingredient.
                 */
                if (current != null && !current.getType().isAir() && choice.test(current)) {
                    continue;
                }

                /*
                 * Don't overwrite an unrelated item.
                 */
                if (current != null && !current.getType().isAir()) {
                    continue;
                }

                ItemStack collected = takeOneFromInventories(nearbyInventories, choice);

                if (collected == null) {
                    continue;
                }

                matrix[matrixSlot] = collected;
            }
        }

        craftingInventory.setMatrix(matrix);
    }

    /*
     * =========================================================
     * SHAPELESS RECIPE FILLING
     * =========================================================
     */

    public void fillShapelessRecipe(CraftingInventory craftingInventory, ShapelessRecipe recipe, List<Inventory> nearbyInventories) {
        ItemStack[] matrix = craftingInventory.getMatrix();

        List<RecipeChoice> remainingChoices = new ArrayList<>(recipe.getChoiceList());

        /*
         * Simulate existing matrix quantities.
         *
         * This is better than treating each occupied slot as only one
         * ingredient.
         */
        for (ItemStack matrixItem : matrix) {

            if (matrixItem == null || matrixItem.getType().isAir()) {
                continue;
            }

            int availableAmount = matrixItem.getAmount();

            for (int amount = 0; amount < availableAmount; amount++) {
                int matchingChoice = findMatchingChoice(remainingChoices, matrixItem);

                if (matchingChoice == -1) {
                    break;
                }

                remainingChoices.remove(matchingChoice);
            }

            if (remainingChoices.isEmpty()) {
                break;
            }
        }

        /*
         * Add any missing ingredients from nearby inventories.
         */
        for (RecipeChoice choice : remainingChoices) {

            int slot = findEmptyCraftingSlot(matrix);

            if (slot == -1) {
                break;
            }

            ItemStack collected = takeOneFromInventories(nearbyInventories, choice);

            if (collected == null) {
                continue;
            }

            matrix[slot] = collected;
        }

        craftingInventory.setMatrix(matrix);
    }

    /**
     * Compatibility with your previous method name.
     */
    public void fillShapeLessRecipe(CraftingInventory craftingInventory, ShapelessRecipe recipe, List<Inventory> nearbyInventories) {
        fillShapelessRecipe(craftingInventory, recipe, nearbyInventories);
    }

    private int findMatchingChoice(List<RecipeChoice> choices, ItemStack item) {
        for (int i = 0; i < choices.size(); i++) {
            if (choices.get(i).test(item)) {
                return i;
            }
        }

        return -1;
    }

    public int findEmptyCraftingSlot(ItemStack[] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            ItemStack item = matrix[i];

            if (item == null || item.getType().isAir()) {
                return i;
            }
        }

        return -1;
    }

    /*
     * =========================================================
     * ITEM COLLECTION
     * =========================================================
     */

    /**
     * Removes exactly one matching item from the supplied inventories.
     *
     * @return cloned single-item stack, or null
     */
    public ItemStack takeOneFromInventories(List<Inventory> inventories, RecipeChoice choice) {
        for (Inventory inventory : inventories) {

            for (int slot = 0; slot < inventory.getSize(); slot++) {
                ItemStack item = inventory.getItem(slot);

                if (item == null || item.getType().isAir()) {
                    continue;
                }

                if (!choice.test(item)) {
                    continue;
                }

                /*
                 * Preserve all metadata.
                 */
                ItemStack collected = item.clone();

                collected.setAmount(1);

                if (item.getAmount() <= 1) {

                    inventory.setItem(slot, null);

                } else {

                    ItemStack remaining = item.clone();

                    remaining.setAmount(item.getAmount() - 1);

                    inventory.setItem(slot, remaining);
                }

                return collected;
            }
        }

        return null;
    }

    /*
     * =========================================================
     * NEARBY INVENTORIES
     * =========================================================
     */

    /**
     * Finds storage containers around a crafting table.
     * <p>
     * Using Container instead of InventoryHolder prevents things such
     * as arbitrary inventory holders from being treated as storage.
     */
    public List<Inventory> getNearbyInventories(Block craftingTable, int radius) {
        List<Inventory> inventories = new ArrayList<>();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    /*
                     * Crafting table itself.
                     */
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }

                    Block block = craftingTable.getRelative(x, y, z);

                    if (!(block.getState() instanceof Container container)) {
                        continue;
                    }

                    Inventory inventory = container.getInventory();

                    /*
                     * Helps prevent the same combined inventory
                     * appearing more than once.
                     */
                    if (!inventories.contains(inventory)) {
                        inventories.add(inventory);
                    }
                }
            }
        }

        return inventories;
    }

    /*
     * =========================================================
     * AVAILABLE ITEMS
     * =========================================================
     */

    /**
     * Returns all available crafting ingredients from the player's
     * inventory and nearby containers.
     */
    public List<ItemStack> getAvailableCraftingItems(Player player, Block craftingTable, int radius) {
        List<ItemStack> availableItems = getPlayerItems(player);

        for (Inventory inventory : getNearbyInventories(craftingTable, radius)) {
            for (ItemStack item : inventory.getStorageContents()) {
                if (item == null || item.getType().isAir()) {
                    continue;
                }

                availableItems.add(item.clone());
            }
        }

        return availableItems;
    }

    private List<ItemStack> getPlayerItems(Player player) {
        List<ItemStack> items = new ArrayList<>();

        for (ItemStack item : player.getInventory().getStorageContents()) {
            if (item == null || item.getType().isAir()) {
                continue;
            }

            items.add(item.clone());
        }

        return items;
    }

    private List<ItemStack> cloneItems(List<ItemStack> items) {
        List<ItemStack> result = new ArrayList<>();

        for (ItemStack item : items) {

            if (item == null || item.getType().isAir()) {
                continue;
            }

            result.add(item.clone());
        }

        return result;
    }
}