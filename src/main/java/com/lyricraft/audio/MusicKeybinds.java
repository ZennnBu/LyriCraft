
package com.lyricraft.audio;

import com.lyricraft.LyriCraftClient;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;

public class MusicKeybinds {

    private static final KeyMapping.Category CATEGORY =
            KeyMapping.Category.register(
                    Identifier.fromNamespaceAndPath(
                            "lyricraft",
                            "music"
                    )
            );

    private static KeyMapping playPauseKey;
    private static KeyMapping nextSongKey;
    private static KeyMapping previousSongKey;

    public static void register() {

        playPauseKey = KeyMappingHelper.registerKeyMapping(
                new KeyMapping(
                        "key.lyricraft.play_pause",
                        InputConstants.Type.KEYSYM,
                        InputConstants.KEY_P,
                        CATEGORY
                )
        );

        nextSongKey = KeyMappingHelper.registerKeyMapping(
                new KeyMapping(
                        "key.lyricraft.next_song",
                        InputConstants.Type.KEYSYM,
                        InputConstants.KEY_N,
                        CATEGORY
                )
        );

        previousSongKey = KeyMappingHelper.registerKeyMapping(
                new KeyMapping(
                        "key.lyricraft.previous_song",
                        InputConstants.Type.KEYSYM,
                        InputConstants.KEY_B,
                        CATEGORY
                )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (LyriCraftClient.MUSIC_CONTROLLER == null) {
                return;
            }

            while (playPauseKey.consumeClick()) {
                LyriCraftClient.MUSIC_CONTROLLER.togglePlayback();
            }

            while (nextSongKey.consumeClick()) {
                LyriCraftClient.MUSIC_CONTROLLER.playNext();
            }

            while (previousSongKey.consumeClick()) {
                LyriCraftClient.MUSIC_CONTROLLER.playPrevious();
            }
        });
    }
}
