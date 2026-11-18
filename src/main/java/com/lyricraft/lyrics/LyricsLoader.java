
package com.lyricraft.lyrics;

import com.lyricraft.LyriCraftClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public class LyricsLoader {

    public List<LrcParser.LyricLine> loadLyrics(Path song) {

        if (song == null) {
            return List.of();
        }

        String fileName = song.getFileName().toString();

        int extensionIndex = fileName.lastIndexOf('.');

        if (extensionIndex <= 0) {
            return List.of();
        }

        String songName = fileName.substring(0, extensionIndex);

        Path lyricsFile = LyriCraftClient.LYRICS_DIRECTORY
                .resolve(songName + ".lrc");

        if (!Files.isRegularFile(lyricsFile)) {
            LyriCraftClient.LOGGER.warn(
                    "Lyrics not found for: {}",
                    songName
            );

            return List.of();
        }

        try {
            List<LrcParser.LyricLine> lyrics =
                    LrcParser.parse(lyricsFile);

            LyriCraftClient.LOGGER.info(
                    "Loaded {} lyric lines for {}",
                    lyrics.size(),
                    songName
            );

            return lyrics;

        } catch (IOException exception) {

            LyriCraftClient.LOGGER.error(
                    "Failed to load lyrics for {}",
                    songName,
                    exception
            );

            return List.of();
        }
    }
}
