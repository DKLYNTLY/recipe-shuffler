package com.dklyntly.dkrecipeshuffle;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

import java.util.*;

public class RecipeShuffle {
    public static Map<ResourceLocation, ItemStack> scramble(MinecraftServer server, long seed) {
        List<Recipe<?>> all = new ArrayList<>(server.getRecipeManager().getRecipes());
        RegistryAccess access = server.registryAccess();

        List<ItemStack> outputs = new ArrayList<>();
        for (Recipe<?> r : all) {
            try {
                if (!isIncluded(r, access)) continue;
                ItemStack res = r.getResultItem(access);
                if (!res.isEmpty() && isAllowedOutput(res)) outputs.add(res.copy());
            } catch (Exception ignored) {}
        }

        if (outputs.size() < 2) return Collections.emptyMap();

        Collections.shuffle(outputs, new Random(seed));

        Map<ResourceLocation, ItemStack> remap = new HashMap<>();
        int i = 0;
        for (Recipe<?> r : all) {
            try {
                if (!isIncluded(r, access)) continue;
                ItemStack orig = r.getResultItem(access);
                if (orig.isEmpty() || !isAllowedOutput(orig)) continue;
                ItemStack newOut = outputs.get(i % outputs.size()).copy();
                newOut.setCount(orig.getCount());
                remap.put(r.getId(), newOut);
                i++;
            } catch (Exception ignored) {}
        }
        return remap;
    }

    public static Map<ResourceLocation, ItemStack> scramble(MinecraftServer server) {
        return scramble(server, server.overworld().getSeed());
    }

    private static boolean isIncluded(Recipe<?> recipe, RegistryAccess access) {
        if (DKRecipeShuffleMod.Config.chaosMode()) return true;

        ResourceLocation recipeId = recipe.getId();
        if (DKRecipeShuffleMod.Config.recipeBlacklist().contains(recipeId)) return false;
        if (DKRecipeShuffleMod.Config.modBlacklist().contains(recipeId.getNamespace())) return false;

        ItemStack output = recipe.getResultItem(access);
        ResourceLocation outputId = BuiltInRegistries.ITEM.getKey(output.getItem());
        if (DKRecipeShuffleMod.Config.itemBlacklist().contains(outputId)) return false;
        return !DKRecipeShuffleMod.Config.modBlacklist().contains(outputId.getNamespace());
    }

    private static boolean isAllowedOutput(ItemStack stack) {
        if (DKRecipeShuffleMod.Config.chaosMode()) return true;

        ResourceLocation outputId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (DKRecipeShuffleMod.Config.itemBlacklist().contains(outputId)) return false;
        return !DKRecipeShuffleMod.Config.modBlacklist().contains(outputId.getNamespace());
    }
}
