package com.steamsworld.flappy;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import lombok.Getter;

/**
 * @author Steamworks (Steamworks#1127)
 * Monday 04 2021 (5:51 PM)
 * flappy (com.steamsworld.flappy)
 */
@Getter
public class Sprite {

    private Image image;
    private double posX = 0, posY = 0, velX = 0, velY = 0, width, height;

    public void setImage(Image image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }
    public void resize(String path, int width, int height) {
        this.setImage(
                new Image(path, width, height, false, false)
        );
    }
    public void setPosition(double x, double y) {
        this.posX = x;
        this.posY = y;
    }
    public void setVelocity(double x, double y) {
        this.velX = x;
        this.velY = y;
    }
    public void addVelocity(double x, double y) {
        this.velX += x;
        this.velY += y;
    }

    /**
     * Render the sprite on the display.
     */
    public void render(GraphicsContext  context) {
        context.drawImage(this.image, this.posX, this.posY);
    }

    /**
     * Get the boundary of this Sprite.
     */
    public Rectangle2D getBounds() {
        return new Rectangle2D(
                this.posX,
                this.posY,
                width,
                height
        );
    }

    /**
     * Check if a boundary intersects with this boundary.
     */
    public boolean doesIntersect(Sprite other) {
        return other.getBounds().intersects(this.getBounds());
    }

    /**
     * Update the position of this sprite.
     */
    public void update(double time) {
        this.posX += (velX * time);
        this.posY += (velY * time);
    }

}
