package com.steamsworld.flappy.objects;

import javafx.scene.media.AudioClip;
import lombok.SneakyThrows;

/**
 * @author Steamworks (Steamworks#1127)
 * Monday 04 2021 (6:14 PM)
 * flappy (com.steamsworld.flappy.objects)
 */
public class AudioEffect {

    private final AudioClip clip;

    public AudioEffect(String path) {
        this.clip = new AudioClip(
                this.getClass().getResource(path).toExternalForm()
        );
    }

    public void play() {
        clip.play();
    }

}
