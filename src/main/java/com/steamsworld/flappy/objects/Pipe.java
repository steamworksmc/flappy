package com.steamsworld.flappy.objects;

import com.steamsworld.flappy.Sprite;
import lombok.Getter;

/**
 * @author Steamworks (Steamworks#1127)
 * Monday 04 2021 (6:12 PM)
 * flappy (com.steamsworld.flappy.objects)
 *
 * A class that represents a pie.
 */
public class Pipe {

    @Getter
    private final Sprite sprite;
    private final double x, y, height, width;

    public Pipe(boolean facingUp, int height) {
        this.sprite = new Sprite();
        this.sprite.resize("/pipe/" + (facingUp ? "up" : "down") + ".png", 70, height);

        this.width = 70;
        this.height = height;
        this.x = 400;
        this.y = facingUp ? 600 - height : 0;
        this.sprite.setPosition(x, y);
    }

}
