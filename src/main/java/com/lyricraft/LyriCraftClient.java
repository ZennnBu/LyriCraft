
package com.lyricraft;

import com.lyricraft.audio.MusicController;
import com.lyricraft.audio.MusicKeybinds;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LyriCraftClient implements ClientModInitializer {

    public static final String MOD_ID = "lyricraft";

    public static final Logger LOGGER =
            LoggerFactory.getLogger(MOD_ID);

    public static Path MOD_DIRECTORY;
    public static Path SONGS_DIRECTORY;
    public static Path LYRICS_DIRECTORY;

    public static MusicController MUSIC_CONTROLLER;

    @Override
    public void onInitializeClient() {

        Path gameDirectory =
                FabricLoader.getInstance().getGameDir();

        MOD_DIRECTORY =
                gameDirectory.resolve("LyriCraft");

        SONGS_DIRECTORY =
                MOD_DIRECTORY.resolve("songs");

        LYRICS_DIRECTORY =
                MOD_DIRECTORY.resolve("lyrics");

        try {
            Files.createDirectories(SONGS_DIRECTORY);
            Files.createDirectories(LYRICS_DIRECTORY);

            MUSIC_CONTROLLER = new MusicController();
            MUSIC_CONTROLLER.refreshSongs();

            MusicKeybinds.register();

            LOGGER.info("LyriCraft initialized successfully!");
            LOGGER.info("Music folder: {}", SONGS_DIRECTORY);
            LOGGER.info("Lyrics folder: {}", LYRICS_DIRECTORY);

        } catch (IOException exception) {

            LOGGER.error(
                    "Failed to initialize LyriCraft folders!",
                    exception
            );
        }
    }
}
