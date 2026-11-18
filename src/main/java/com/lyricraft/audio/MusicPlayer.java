
package com.lyricraft.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.IOException;
import java.nio.file.Path;

public class MusicPlayer {

    private Clip clip;
    private boolean paused = false;
    private long pausePosition = 0;
    private float volume = 0.8f;

    public void loadAndPlay(Path song)
            throws IOException,
            UnsupportedAudioFileException,
            LineUnavailableException {

        stop();

        try (AudioInputStream audio =
                     AudioSystem.getAudioInputStream(song.toFile())) {

            clip = AudioSystem.getClip();
            clip.open(audio);
        }

        paused = false;
        pausePosition = 0;

        applyVolume();
        clip.start();
    }

    public void pause() {

        if (clip != null && clip.isRunning()) {

            pausePosition = clip.getMicrosecondPosition();

            clip.stop();
            paused = true;
        }
    }

    public void resume() {

        if (clip != null && paused) {

            clip.setMicrosecondPosition(pausePosition);

            paused = false;
            clip.start();
        }
    }

    public void togglePlayback() {

        if (clip == null) {
            return;
        }

        if (clip.isRunning()) {
            pause();
        } else {
            resume();
        }
    }

    public void stop() {

        if (clip != null) {

            clip.stop();
            clip.close();
            clip = null;
        }

        paused = false;
        pausePosition = 0;
    }

    public void setVolume(float value) {

        volume = Math.max(0.0f, Math.min(1.0f, value));

        applyVolume();
    }

    private void applyVolume() {

        if (clip == null) {
            return;
        }

        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {

            FloatControl control =
                    (FloatControl) clip.getControl(
                            FloatControl.Type.MASTER_GAIN
                    );

            float decibels = volume == 0
                    ? control.getMinimum()
                    : (float) (20.0 * Math.log10(volume));

            control.setValue(
                    Math.max(
                            control.getMinimum(),
                            Math.min(control.getMaximum(), decibels)
                    )
            );
        }
    }

    public long getPositionMillis() {

        if (clip == null) {
            return 0;
        }

        return clip.getMicrosecondPosition() / 1000;
    }

    public long getDurationMillis() {

        if (clip == null) {
            return 0;
        }

        return clip.getMicrosecondLength() / 1000;
    }

    public boolean isPlaying() {
        return clip != null && clip.isRunning();
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isLoaded() {
        return clip != null;
    }

    public float getVolume() {
        return volume;
    }
}
