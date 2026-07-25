package ch.framedev.betterminecraft;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipesManager {

    private final BetterMinecraft plugin;
    private final List<ShapedRecipe> recipes = new ArrayList<>();

    public RecipesManager(BetterMinecraft plugin) {
        this.plugin = plugin;
    }

    public void init() {
        List<Boolean> recipesResultList = setupCrafting();
        long successfulCount = recipesResultList.stream()
                .filter(Boolean::booleanValue)
                .count();
        long failedCount = recipesResultList.size() - successfulCount;
        String message = "Recipes setup completed with " + successfulCount + " successful recipes and " + failedCount + " failed recipes.";
        plugin.getLogger().info(message);
    }

    private List<Boolean> setupCrafting() {
        List<Boolean> success = new ArrayList<>();
        // Granite Tools
        ShapedRecipe recipeGraniteSword = new ShapedRecipe(new NamespacedKey(plugin, "better_vanilla_recipe_granite_sword"), new ItemStack(Material.STONE_SWORD));
        recipeGraniteSword.shape(" G ", " G ", " S ");
        recipeGraniteSword.setIngredient('G', Material.GRANITE);
        recipeGraniteSword.setIngredient('S', Material.STICK);
        success.add(plugin.getServer().addRecipe(recipeGraniteSword));
        recipes.add(recipeGraniteSword);
        ShapedRecipe recipeGranitePickaxe = new ShapedRecipe(new NamespacedKey(plugin, "better_vanilla_recipe_granite_pickaxe"), new ItemStack(Material.STONE_PICKAXE));
        recipeGranitePickaxe.shape("GGG", " S ", " S ");
        recipeGranitePickaxe.setIngredient('G', Material.GRANITE);
        recipeGranitePickaxe.setIngredient('S', Material.STICK);
        success.add(plugin.getServer().addRecipe(recipeGranitePickaxe));
        recipes.add(recipeGranitePickaxe);
        ShapedRecipe recipeGraniteAxe = new ShapedRecipe(new NamespacedKey(plugin, "better_vanilla_recipe_granite_axe"), new ItemStack(Material.STONE_AXE));
        recipeGraniteAxe.shape("GG ", "GS ", " S ");
        recipeGraniteAxe.setIngredient('G', Material.GRANITE);
        recipeGraniteAxe.setIngredient('S', Material.STICK);
        success.add(plugin.getServer().addRecipe(recipeGraniteAxe));
        recipes.add(recipeGraniteAxe);
        ShapedRecipe recipeGraniteShovel = new ShapedRecipe(new NamespacedKey(plugin, "better_vanilla_recipe_granite_shovel"), new ItemStack(Material.STONE_SHOVEL));
        recipeGraniteShovel.shape(" G ", " S ", " S ");
        recipeGraniteShovel.setIngredient('G', Material.GRANITE);
        recipeGraniteShovel.setIngredient('S', Material.STICK);
        success.add(plugin.getServer().addRecipe(recipeGraniteShovel));
        recipes.add(recipeGraniteShovel);
        ShapedRecipe recipeGraniteHoe = new ShapedRecipe(new NamespacedKey(plugin, "better_vanilla_recipe_granite_hoe"), new ItemStack(Material.STONE_HOE));
        recipeGraniteHoe.shape("GG ", " S ", " S ");
        recipeGraniteHoe.setIngredient('G', Material.GRANITE);
        recipeGraniteHoe.setIngredient('S', Material.STICK);
        success.add(plugin.getServer().addRecipe(recipeGraniteHoe));
        recipes.add(recipeGraniteHoe);

        // Andesite Tools
        ShapedRecipe recipeAndesiteSword = new ShapedRecipe(new NamespacedKey(plugin, "better_vanilla_recipe_andesite_sword"), new ItemStack(Material.STONE_SWORD));
        recipeAndesiteSword.shape(" G ", " G ", " S ");
        recipeAndesiteSword.setIngredient('G', Material.ANDESITE);
        recipeAndesiteSword.setIngredient('S', Material.STICK);
        success.add(plugin.getServer().addRecipe(recipeAndesiteSword));
        recipes.add(recipeAndesiteSword);
        ShapedRecipe recipeAndesiteAxe = new ShapedRecipe(new NamespacedKey(plugin, "better_vanilla_recipe_andesite_axe"), new ItemStack(Material.STONE_AXE));
        recipeAndesiteAxe.shape("GG ", "GS ", " S ");
        recipeAndesiteAxe.setIngredient('G', Material.ANDESITE);
        recipeAndesiteAxe.setIngredient('S', Material.STICK);
        success.add(plugin.getServer().addRecipe(recipeAndesiteAxe));
        recipes.add(recipeAndesiteAxe);
        ShapedRecipe recipeAndesitePickaxe = new ShapedRecipe(new NamespacedKey(plugin, "better_vanilla_recipe_andesite_pickaxe"), new ItemStack(Material.STONE_PICKAXE));
        recipeAndesitePickaxe.shape("GGG", " S ", " S ");
        recipeAndesitePickaxe.setIngredient('G', Material.ANDESITE);
        recipeAndesitePickaxe.setIngredient('S', Material.STICK);
        success.add(plugin.getServer().addRecipe(recipeAndesitePickaxe));
        recipes.add(recipeAndesitePickaxe);
        ShapedRecipe recipeAndesiteHoe = new ShapedRecipe(new NamespacedKey(plugin, "better_vanilla_recipe_andesite_hoe"), new ItemStack(Material.STONE_HOE));
        recipeAndesiteHoe.shape("GG ", " S ", " S ");
        recipeAndesiteHoe.setIngredient('G', Material.ANDESITE);
        recipeAndesiteHoe.setIngredient('S', Material.STICK);
        success.add(plugin.getServer().addRecipe(recipeAndesiteHoe));
        recipes.add(recipeAndesiteHoe);
        ShapedRecipe recipeAndesiteShovel = new ShapedRecipe(new NamespacedKey(plugin, "better_vanilla_recipe_andesite_shovel"), new ItemStack(Material.STONE_SHOVEL));
        recipeAndesiteShovel.shape(" G ", " S ", " S ");
        recipeAndesiteShovel.setIngredient('G', Material.ANDESITE);
        recipeAndesiteShovel.setIngredient('S', Material.STICK);
        success.add(plugin.getServer().addRecipe(recipeAndesiteShovel));
        recipes.add(recipeAndesiteShovel);

        // Diorite Tools.
        ShapedRecipe recipeDioriteSword = new ShapedRecipe(new NamespacedKey(plugin, "better_vanilla_recipe_diorite_sword"), new ItemStack(Material.STONE_SWORD));
        recipeDioriteSword.shape("G", "G", "S");
        recipeDioriteSword.setIngredient('G', Material.DIORITE);
        recipeDioriteSword.setIngredient('S', Material.STICK);
        success.add(plugin.getServer().addRecipe(recipeDioriteSword));
        recipes.add(recipeDioriteSword);
        ShapedRecipe recipeDioriteAxe = new ShapedRecipe(new NamespacedKey(plugin, "better_vanilla_recipe_diorite_axe"), new ItemStack(Material.STONE_AXE));
        recipeDioriteAxe.shape("GG ", "GS ", " S ");
        recipeDioriteAxe.setIngredient('G', Material.DIORITE);
        recipeDioriteAxe.setIngredient('S', Material.STICK);
        success.add(plugin.getServer().addRecipe(recipeDioriteAxe));
        recipes.add(recipeDioriteAxe);
        ShapedRecipe recipeDioritePickaxe = new ShapedRecipe(new NamespacedKey(plugin, "better_vanilla_recipe_diorite_pickaxe"), new ItemStack(Material.STONE_PICKAXE));
        recipeDioritePickaxe.shape("GGG", " S ", " S ");
        recipeDioritePickaxe.setIngredient('G', Material.DIORITE);
        recipeDioritePickaxe.setIngredient('S', Material.STICK);
        success.add(plugin.getServer().addRecipe(recipeDioritePickaxe));
        recipes.add(recipeDioritePickaxe);
        ShapedRecipe recipeDioriteShovel = new ShapedRecipe(new NamespacedKey(plugin, "better_vanilla_recipe_diorite_shovel"), new ItemStack(Material.STONE_SHOVEL));
        recipeDioriteShovel.shape("G", "S", "S");
        recipeDioriteShovel.setIngredient('G', Material.DIORITE);
        recipeDioriteShovel.setIngredient('S', Material.STICK);
        success.add(plugin.getServer().addRecipe(recipeDioriteShovel));
        recipes.add(recipeDioriteShovel);
        ShapedRecipe recipeDioriteHoe = new ShapedRecipe(new NamespacedKey(plugin, "better_vanilla_recipe_diorite_hoe"), new ItemStack(Material.STONE_HOE));
        recipeDioriteHoe.shape("GG ", " S ", " S ");
        recipeDioriteHoe.setIngredient('G', Material.DIORITE);
        recipeDioriteHoe.setIngredient('S', Material.STICK);
        success.add(plugin.getServer().addRecipe(recipeDioriteHoe));
        recipes.add(recipeDioriteHoe);

        // Tuff Tools
        ShapedRecipe recipeTuffSword = new ShapedRecipe(new NamespacedKey(plugin, "better_vanilla_recipe_tuff_sword"), new ItemStack(Material.STONE_SWORD));
        recipeTuffSword.shape("G", "G", "S");
        recipeTuffSword.setIngredient('G', Material.TUFF);
        recipeTuffSword.setIngredient('S', Material.STICK);
        success.add(plugin.getServer().addRecipe(recipeTuffSword));
        recipes.add(recipeTuffSword);
        ShapedRecipe recipeTuffAxe = new ShapedRecipe(new NamespacedKey(plugin, "better_vanilla_recipe_tuff_axe"), new ItemStack(Material.STONE_AXE));
        recipeTuffAxe.shape("GG ", "GS ", " S ");
        recipeTuffAxe.setIngredient('G', Material.TUFF);
        recipeTuffAxe.setIngredient('S', Material.STICK);
        success.add(plugin.getServer().addRecipe(recipeTuffAxe));
        recipes.add(recipeTuffAxe);
        ShapedRecipe recipeTuffPickaxe = new ShapedRecipe(new NamespacedKey(plugin, "better_vanilla_recipe_tuff_pickaxe"), new ItemStack(Material.STONE_PICKAXE));
        recipeTuffPickaxe.shape("GGG", " S ", " S ");
        recipeTuffPickaxe.setIngredient('G', Material.TUFF);
        recipeTuffPickaxe.setIngredient('S', Material.STICK);
        success.add(plugin.getServer().addRecipe(recipeTuffPickaxe));
        recipes.add(recipeTuffPickaxe);
        ShapedRecipe recipeTuffShovel = new ShapedRecipe(new NamespacedKey(plugin, "better_vanilla_recipe_tuff_shovel"), new ItemStack(Material.STONE_SHOVEL));
        recipeTuffShovel.shape("G", "S", "S");
        recipeTuffShovel.setIngredient('G', Material.TUFF);
        recipeTuffShovel.setIngredient('S', Material.STICK);
        success.add(plugin.getServer().addRecipe(recipeTuffShovel));
        recipes.add(recipeTuffShovel);
        ShapedRecipe recipeTuffHoe = new ShapedRecipe(new NamespacedKey(plugin, "better_vanilla_recipe_tuff_hoe"), new ItemStack(Material.STONE_HOE));
        recipeTuffHoe.shape("GG", " S", " S");
        recipeTuffHoe.setIngredient('G', Material.TUFF);
        recipeTuffHoe.setIngredient('S', Material.STICK);
        success.add(plugin.getServer().addRecipe(recipeTuffHoe));
        recipes.add(recipeTuffHoe);
        return success;
    }

    public List<ShapedRecipe> getRecipes() {
        return recipes;
    }

    public boolean canCraft(Player player, ShapedRecipe recipe) {
        Map<Material, Integer> inventory = new HashMap<>();

        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType().isAir()) {
                continue;
            }

            inventory.merge(item.getType(), item.getAmount(), Integer::sum);
        }

        Map<Character, RecipeChoice> choiceMap = recipe.getChoiceMap();

        for (String row : recipe.getShape()) {
            for (char ingredientKey : row.toCharArray()) {
                if (ingredientKey == ' ') {
                    continue;
                }

                RecipeChoice choice = choiceMap.get(ingredientKey);

                if (choice == null) {
                    continue;
                }

                if (!consumeChoice(inventory, choice)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean consumeChoice(Map<Material, Integer> inventory, RecipeChoice choice) {
        if (choice instanceof RecipeChoice.MaterialChoice materialChoice) {
            for (Material material : materialChoice.getChoices()) {
                int availableAmount = inventory.getOrDefault(material, 0);

                if (availableAmount > 0) {
                    inventory.put(material, availableAmount - 1);
                    return true;
                }
            }
            return false;
        }

        if (choice instanceof RecipeChoice.ExactChoice exactChoice) {
            for (ItemStack requiredItem : exactChoice.getChoices()) {
                Material material = requiredItem.getType();
                int availableAmount = inventory.getOrDefault(material, 0);

                if (availableAmount > 0) {
                    inventory.put(material, availableAmount - 1);
                    return true;
                }
            }
        }
        return false;
    }
}
