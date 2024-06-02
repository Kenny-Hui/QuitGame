package com.lx862.quitgame;

import com.lx862.quitgame.config.Config;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class QuitGame implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("QuitGame");
    public static double scale = 1.8;
    public static float rotAngle = 20.0F;
    public static List<String> keywords = new ArrayList<>();
    public static boolean unlocked = false;
    public static float unlockedAlpha = 0;

    @Override
    public void onInitialize() {
        Config.load();
        LOGGER.info("[QuitGame] QuitGame loaded");
    }
}
