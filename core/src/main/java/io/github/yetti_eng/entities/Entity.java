package io.github.yetti_eng.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

// Called "Sprite" in the architecture documentation; renamed to avoid clash with LibGDX class name
public abstract class Entity extends Sprite {
    protected float speed;
    private final Rectangle hitbox;

    public Entity(Texture tex, float x, float y, float width, float height, float speed) {
        super(tex);
        setBounds(x, y, width, height);
        hitbox = new Rectangle(x, y, width, height);
        this.speed = speed;
    }

    public void moveLeft() {
        translateX(-getSpeedThisFrame());
        hitbox.setX(getX());
    }

    public void moveRight() {
        translateX(getSpeedThisFrame());
        hitbox.setX(getX());
    }

    public void moveUp() {
        translateY(getSpeedThisFrame());
        hitbox.setY(getY());
    }


    public void moveDown() {
        translateY(-getSpeedThisFrame());
        hitbox.setY(getY());
    }

    public boolean collidedWith(Entity other) {
        return hitbox.overlaps(other.getHitbox());
    }

    public float getSpeed() {
        return speed;
    }

    public float getSpeedThisFrame() {
        return speed * Gdx.graphics.getDeltaTime();
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}
