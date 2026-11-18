
package com.lyricraft.audio;

import com.lyricraft.LyriCraftClient;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

public class MusicHud {

    public static void register() {

        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath(
                        "lyricraft",
                        "music_hud"
                ),
                MusicHud::render
        );
    }

    private static void render(
            GuiGraphicsExtractor graphics,
            DeltaTracker deltaTracker
    ) {

        MusicController controller =
                LyriCraftClient.MUSIC_CONTROLLER;

        if (controller == null || !controller.isPlaying()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();

        int x = (graphics.guiWidth() - 220) / 2;
        int y = 12;

        graphics.fill(x, y, x + 220, y + 65, 0xDD101010);
        graphics.outline(x, y, 220, 65, 0xFF8B5CF6);

        graphics.text(
                minecraft.font,
                "LYRICRAFT",
                x + 10, y + 7,
                0xFFB794F6,
                false
        );

        graphics.text(
                minecraft.font,
                controller.getCurrentSongName(),
                x + 10, y + 21,
                0xFFFFFFFF,
                false
        );

        String lyric = controller.getCurrentLyric();

        if (lyric.isBlank()) {
            lyric = "♪ Music playing ♪";
        }

        graphics.text(
                minecraft.font,
                lyric,
                x + 10, y + 38,
                0xFFFFD6FF,
                false
        );

        graphics.text(
                minecraft.font,
                "P: Play/Pause   N: Next",
                x + 10, y + 52,
                0xFFAAAAAA,
                false
        );
    }
}
