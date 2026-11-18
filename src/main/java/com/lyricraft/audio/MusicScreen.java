
package com.lyricraft.audio;

import com.lyricraft.LyriCraftClient;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.nio.file.Path;
import java.util.List;

public class MusicScreen extends Screen {

    public MusicScreen() {
        super(Component.literal("LyriCraft Music Player"));
    }

    @Override
    protected void init() {

        MusicController controller =
                LyriCraftClient.MUSIC_CONTROLLER;

        if (controller == null) {
            return;
        }

        List<Path> songs = controller.getSongs();

        int centerX = this.width / 2;
        int startY = 55;

        this.addRenderableWidget(
                Button.builder(
                        Component.literal("Refresh Songs"),
                        button -> controller.refreshSongs()
                ).bounds(centerX - 100, 25, 200, 20).build()
        );

        if (songs.isEmpty()) {
            this.addRenderableWidget(
                    Button.builder(
                            Component.literal("No Songs Found"),
                            button -> {}
                    ).bounds(centerX - 100, startY, 200, 20).build()
            );

            return;
        }

        int visibleSongs = Math.min(songs.size(), 8);

        for (int i = 0; i < visibleSongs; i++) {

            final int songIndex = i;

            String songName = songs.get(i)
                    .getFileName()
                    .toString();

            this.addRenderableWidget(
                    Button.builder(
                            Component.literal(songName),
                            button -> controller.playSong(songIndex)
                    ).bounds(
                            centerX - 110,
                            startY + (i * 24),
                            220,
                            20
                    ).build()
            );
        }

        int controlsY = startY + (visibleSongs * 24) + 8;

        this.addRenderableWidget(
                Button.builder(
                        Component.literal("Previous"),
                        button -> controller.playPrevious()
                ).bounds(
                        centerX - 105,
                        controlsY,
                        65,
                        20
                ).build()
        );

        this.addRenderableWidget(
                Button.builder(
                        Component.literal("Play / Pause"),
                        button -> controller.togglePlayback()
                ).bounds(
                        centerX - 35,
                        controlsY,
                        100,
                        20
                ).build()
        );

        this.addRenderableWidget(
                Button.builder(
                        Component.literal("Next"),
                        button -> controller.playNext()
                ).bounds(
                        centerX + 70,
                        controlsY,
                        65,
                        20
                ).build()
        );
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
