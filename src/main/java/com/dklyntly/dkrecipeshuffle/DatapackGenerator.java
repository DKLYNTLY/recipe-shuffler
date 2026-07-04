package com.dklyntly.dkrecipeshuffle;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

public class DatapackGenerator {
    public static final String PACK_DIR = "recipe_shuffler_generated";
    public static final String PACK_ID = "file/" + PACK_DIR;
    public static final String SEED_FILE = "recipe_shuffler_seed.txt";
    public static final String CONFIG_FILE = "recipe_shuffler_config.txt";
    private static final String LEGACY_PACK_DIR = "dkrecipeshuffle";

    public static int generate(MinecraftServer server, Map<ResourceLocation, ItemStack> replacements) {
        Path datapackDir = server.getWorldPath(net.minecraft.world.level.storage.LevelResource.DATAPACK_DIR);
        File dkDir = datapackDir.resolve(PACK_DIR).toFile();
        File legacyDir = datapackDir.resolve(LEGACY_PACK_DIR).toFile();
        int written = 0;

        // Clear old datapack
        if (dkDir.exists()) {
            deleteRecursive(dkDir);
        }
        if (legacyDir.exists()) {
            deleteRecursive(legacyDir);
        }
        dkDir.mkdirs();

        // pack.mcmeta
        try (FileWriter fw = new FileWriter(new File(dkDir, "pack.mcmeta"))) {
            fw.write("{\"pack\":{\"pack_format\":15,\"description\":\"Recipe Shuffler generated recipes\"}}");
        } catch (Exception e) { e.printStackTrace(); }

        // Dump each recipe JSON with swapped result
        for (Recipe<?> r : server.getRecipeManager().getRecipes()) {
            ItemStack repl = replacements.get(r.getId());
            if (repl == null) continue;
            try {
                ResourceLocation path = new ResourceLocation(r.getId().getNamespace(), "recipes/" + r.getId().getPath() + ".json");
                Optional<Resource> opt = server.getResourceManager().getResource(path);
                if (opt.isEmpty()) continue;

                Resource res = opt.get();
                JsonObject root;
                try (Reader reader = new InputStreamReader(res.open(), StandardCharsets.UTF_8)) {
                    root = JsonParser.parseReader(reader).getAsJsonObject();
                }

                if (root.has("result")) {
                    if (root.get("result").isJsonObject()) {
                        JsonObject result = root.getAsJsonObject("result");
                        result.addProperty("item", net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(repl.getItem()).toString());
                        result.addProperty("count", repl.getCount());
                    } else {
                        // some recipes have result as string
                        root.addProperty("result", net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(repl.getItem()).toString());
                    }
                }

                File recipesDir = new File(dkDir, "data/" + r.getId().getNamespace() + "/recipes");
                File outFile = new File(recipesDir, r.getId().getPath() + ".json");
                outFile.getParentFile().mkdirs();
                try (FileWriter fw = new FileWriter(outFile, StandardCharsets.UTF_8)) {
                    fw.write(root.toString());
                }
                written++;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return written;
    }

    private static void deleteRecursive(File f) {
        if (f.isDirectory()) {
            File[] kids = f.listFiles();
            if (kids != null) {
                for (File child : kids) deleteRecursive(child);
            }
        }
        f.delete();
    }
}
