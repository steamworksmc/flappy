package com.steamsworld.flappy.sprites;

import com.steamsworld.flappy.Sprite;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Steamworks (Steamworks#1127)
 * Monday 04 2021 (6:07 PM)
 * flappy (com.steamsworld.flappy.sprites)
 */
public class BirdSprite {

    @Getter
    private final Sprite bird;
    private final List<Sprite> flying = new ArrayList<Sprite>();
    private int current = 0;
    private final int width = 50, height = 45;
    private final double locationX = 70, locationY = 200;

    public BirdSprite() {
        bird = new Sprite();

        bird.resize("bird/main.png", width, height);
        bird.setPosition(locationX, locationY);

        runAnimation();
    }

    private void runAnimation() {
        Sprite bird1 = new Sprite();
        Sprite bird2 = new Sprite();
        Sprite bird3 = new Sprite();

        bird1.resize("bird/main.png", width, height);
        bird1.setPosition(locationX, locationY);

        bird1.resize("bird/variant.png", width, height);
        bird1.setPosition(locationX, locationY);

        bird1.resize("bird/variant1.png", width, height);
        bird1.setPosition(locationX, locationY);

        flying.addAll(Arrays.asList(bird, bird1, bird2, bird3));
    }

    public Sprite animate() {
        if(current == flying.size() - 1)
            current = 0;

        return flying.get(current++);
    }

}
