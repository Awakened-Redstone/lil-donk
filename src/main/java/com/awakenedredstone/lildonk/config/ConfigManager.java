package com.awakenedredstone.lildonk.config;

import com.awakenedredstone.lildonk.LilDonk;
import com.awakenedredstone.lildonk.client.compat.yacl.YACLImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path configPath = FabricLoader.getInstance().getConfigDir().resolve(LilDonk.MOD_ID + ".json");

    public static void loadConfig() {
        Config config = load();

        if (config.donkEnabled == null) config.donkEnabled = true;

        Config.update(config);
        save();
        LilDonk.LOGGER.info("Loaded config");
    }

    private static Config load() {
        Config config = Config.getInstance();
        try {
            if (!Files.exists(configPath)) {
                Files.createDirectories(configPath.getParent());
                Files.createFile(configPath);
                return config;
            }
            try {
                config = GSON.fromJson(Files.newBufferedReader(configPath), Config.class);
            } catch (JsonSyntaxException e) {
                LilDonk.LOGGER.error("Failed to parse config file, using default config");
                config = Config.getInstance();
            }
        } catch (IOException e) {
            LilDonk.LOGGER.error("Failed to load config", e);
        }
        return config == null ? Config.getInstance() : config;
    }

    public static Screen getConfigScreen() {
        try {
            if (!FabricLoader.getInstance().isModLoaded("yet-another-config-lib"))
                return new ConfirmScreen((result) -> {
                    if (result) {
                        Util.getOperatingSystem().open(URI.create("https://modrinth.com/mod/yacl/versions?l=fabric&c=release&g=" + MinecraftClient.getInstance().getGameVersion()));
                    }
                    MinecraftClient.getInstance().setScreen(null);
                }, Text.of("Yet Another Config Lib not installed!"), Text.of("YACL is required to edit the config in game, would you like to install YACL?"), ScreenTexts.YES, ScreenTexts.NO);
            return YACLImpl.getScreen();
        } catch (RuntimeException e) {
            return null;
        }
    }

    private static void save() {
        try {
            Files.write(configPath, GSON.toJson(Config.getInstance()).getBytes());
        } catch (IOException e) {
            LilDonk.LOGGER.error("Failed to save config", e);
        }
    }

    public static void saveConfig() {
        save();
        LilDonk.LOGGER.info("Saved config");
    }
}
