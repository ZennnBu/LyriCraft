
package com.lyricraft.audio;

import com.lyricraft.LyriCraftClient;
import com.lyricraft.lyrics.LyricManager;
import com.lyricraft.lyrics.LyricsLoader;

import java.nio.file.Path;
import java.util.List;

public class MusicController {

    private final MusicPlayer musicPlayer = new MusicPlayer();
    private final SongManager songManager = new SongManager();
    private final LyricsLoader lyricsLoader = new LyricsLoader();
    private final LyricManager lyricManager = new LyricManager();

    private int currentSongIndex = -1;

    public void refreshSongs() {
        songManager.refreshSongs();

        if (songManager.getSongCount() == 0) {
            currentSongIndex = -1;
        }
    }

    public boolean playSong(int index) {

        Path song = songManager.getSong(index);

        if (song == null) {
            return false;
        }

        try {
            musicPlayer.loadAndPlay(song);

            currentSongIndex = index;

            lyricManager.setLyrics(
                    lyricsLoader.loadLyrics(song)
            );

            LyriCraftClient.LOGGER.info(
                    "Now playing: {}",
                    song.getFileName()
            );

            return true;

        } catch (Exception exception) {

            LyriCraftClient.LOGGER.error(
                    "Unable to play song: {}",
                    song.getFileName(),
                    exception
            );

            return false;
        }
    }

    public boolean playNext() {

        int nextIndex = currentSongIndex + 1;

        if (nextIndex >= songManager.getSongCount()) {
            nextIndex = 0;
        }

        return playSong(nextIndex);
    }

    public boolean playPrevious() {

        int previousIndex = currentSongIndex - 1;

        if (previousIndex < 0) {
            previousIndex = songManager.getSongCount() - 1;
        }

        return playSong(previousIndex);
    }

    public void togglePlayback() {
        musicPlayer.togglePlayback();
    }

    public void pause() {
        musicPlayer.pause();
    }

    public void resume() {
        musicPlayer.resume();
    }

    public void stop() {
        musicPlayer.stop();
        lyricManager.clear();
    }

    public void setVolume(float volume) {
        musicPlayer.setVolume(volume);
    }

    public String getCurrentLyric() {
        return lyricManager.getCurrentLyric(
                musicPlayer.getPositionMillis()
        );
    }

    public String getNextLyric() {
        return lyricManager.getNextLyric(
                musicPlayer.getPositionMillis()
        );
    }

    public List<Path> getSongs() {
        return songManager.getSongs();
    }

    public String getCurrentSongName() {

        Path song = songManager.getSong(currentSongIndex);

        return song == null
                ? "No song selected"
                : song.getFileName().toString();
    }

    public long getPositionMillis() {
        return musicPlayer.getPositionMillis();
    }

    public long getDurationMillis() {
        return musicPlayer.getDurationMillis();
    }

    public boolean isPlaying() {
        return musicPlayer.isPlaying();
    }

    public boolean isPaused() {
        return musicPlayer.isPaused();
    }

    public boolean hasSongs() {
        return songManager.hasSongs();
    }

    public int getCurrentSongIndex() {
        return currentSongIndex;
    }
}
