
package com.lyricraft.lyrics;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LrcParser {

    private static final Pattern TIMESTAMP_PATTERN =
            Pattern.compile("\\[(\\d{1,2}):(\\d{2})(?:\\.(\\d{1,3}))?\\]");

    public record LyricLine(long timestampMillis, String text) {}

    public static List<LyricLine> parse(Path file)
            throws IOException {

        List<LyricLine> lyrics = new ArrayList<>();

        List<String> lines = Files.readAllLines(
                file,
                StandardCharsets.UTF_8
        );

        for (String line : lines) {

            Matcher matcher =
                    TIMESTAMP_PATTERN.matcher(line);

            List<Long> timestamps = new ArrayList<>();

            while (matcher.find()) {

                long minutes =
                        Long.parseLong(matcher.group(1));

                long seconds =
                        Long.parseLong(matcher.group(2));

                String fraction = matcher.group(3);

                long milliseconds = 0;

                if (fraction != null) {

                    String padded =
                            (fraction + "00").substring(0, 3);

                    milliseconds =
                            Long.parseLong(padded);
                }

                long timestamp =
                        (minutes * 60 + seconds) * 1000
                                + milliseconds;

                timestamps.add(timestamp);
            }

            if (timestamps.isEmpty()) {
                continue;
            }

            String lyricText =
                    matcher.replaceAll("").trim();

            for (long timestamp : timestamps) {

                lyrics.add(
                        new LyricLine(
                                timestamp,
                                lyricText
                        )
                );
            }
        }

        lyrics.sort(
                Comparator.comparingLong(
                        LyricLine::timestampMillis
                )
        );

        return lyrics;
    }
}
