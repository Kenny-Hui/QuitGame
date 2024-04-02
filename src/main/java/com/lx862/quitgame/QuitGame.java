package com.lx862.quitgame;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuitGame implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("QuitGame");
    public static double scale = 1.8;
    public static float rotAngle = 20.0F;
    public static String[] keywords = {"start", "menu", "play", "poison"};

    public static boolean unlocked = false;

    @Override
    public void onInitialize() {
        LOGGER.info("[QuitGame] QuitGame loaded");
    }
}
