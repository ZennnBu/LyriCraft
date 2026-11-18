
package com.lyricraft.audio;

import com.lyricraft.LyriCraftClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class SongManager {

    private final List<Path> songs = new ArrayList<>();

    public void refreshSongs() {

        songs.clear();

        Path folder = LyriCraftClient.SONGS_DIRECTORY;

        if (folder == null || !Files.isDirectory(folder)) {
            LyriCraftClient.LOGGER.warn("Songs folder not found!");
            return;
        }

        try (Stream<Path> files = Files.list(folder)) {

            files.filter(Files::isRegularFile)
                    .filter(this::isSupportedAudio)
                    .sorted(Comparator.comparing(
                            path -> path.getFileName()
                                    .toString()
                                    .toLowerCase(Locale.ROOT)
                    ))
                    .forEach(songs::add);

            LyriCraftClient.LOGGER.info(
                    "Found {} songs!",
                    songs.size()
            );

        } catch (IOException exception) {

            LyriCraftClient.LOGGER.error(
                    "Unable to scan songs folder!",
                    exception
            );
        }
    }

    private boolean isSupportedAudio(Path file) {

        String name = file.getFileName()
                .toString()
                .toLowerCase(Locale.ROOT);

        return name.endsWith(".mp3")
                || name.endsWith(".ogg");
    }

    public List<Path> getSongs() {
        return List.copyOf(songs);
    }

    public Path getSong(int index) {

        if (index < 0 || index >= songs.size()) {
            return null;
        }

        return songs.get(index);
    }

    public int getSongCount() {
        return songs.size();
    }

    public boolean hasSongs() {
        return !songs.isEmpty();
    }
}
