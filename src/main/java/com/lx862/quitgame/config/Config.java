package com.lx862.quitgame.config;

import com.google.gson.*;
import com.lx862.quitgame.QuitGame;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;

public class Config {
    public static File configFile = FabricLoader.getInstance().getConfigDir().resolve("quitgame").resolve("config.json").toFile();

    public static void load() {
        configFile.getParentFile().mkdirs();

        if(!configFile.exists()) {
            QuitGame.LOGGER.info("[QuitGame] Config not found, generating one...");
            write();
            load();
        } else {
            try {
                JsonElement jsonElement = JsonParser.parseReader(new FileReader(configFile));
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                if(jsonObject.get("alwaysUnlock").getAsBoolean()) {
                    QuitGame.unlocked = true;
                }

                JsonArray keywords = jsonObject.get("keywords").getAsJsonArray();
                for(int i = 0; i < keywords.size(); i++) {
                    QuitGame.keywords.add(keywords.get(i).getAsString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void write() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("alwaysUnlock", false);

        JsonArray keywords = new JsonArray();
        keywords.add("init");
        keywords.add("start");
        keywords.add("menu");
        keywords.add("play");
        keywords.add("poison");
        jsonObject.add("keywords", keywords);

        try (Writer writer = new FileWriter(configFile)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(jsonObject, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
