
package com.lyricraft.lyrics;

import java.util.List;

public class LyricManager {

    private List<LrcParser.LyricLine> lyrics = List.of();

    public void setLyrics(List<LrcParser.LyricLine> lyrics) {
        this.lyrics = lyrics == null ? List.of() : List.copyOf(lyrics);
    }

    public String getCurrentLyric(long playbackMillis) {

        if (lyrics.isEmpty()) {
            return "";
        }

        int left = 0;
        int right = lyrics.size() - 1;
        int result = -1;

        while (left <= right) {

            int middle = left + (right - left) / 2;

            long timestamp =
                    lyrics.get(middle).timestampMillis();

            if (timestamp <= playbackMillis) {
                result = middle;
                left = middle + 1;
            } else {
                right = middle - 1;
            }
        }

        if (result == -1) {
            return "";
        }

        return lyrics.get(result).text();
    }

    public String getNextLyric(long playbackMillis) {

        for (LrcParser.LyricLine line : lyrics) {

            if (line.timestampMillis() > playbackMillis) {
                return line.text();
            }
        }

        return "";
    }

    public void clear() {
        lyrics = List.of();
    }

    public boolean hasLyrics() {
        return !lyrics.isEmpty();
    }
}
