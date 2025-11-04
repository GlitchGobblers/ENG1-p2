package io.github.yetti_eng.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

// Called "Sprite" in the architecture documentation; renamed to avoid clash with LibGDX class name
public abstract class Entity extends Sprite {
    protected float speed;
    protected Vector2 movement;
    private final Rectangle hitbox;

    public Entity(Texture tex, float x, float y, float width, float height, float speed) {
        super(tex);
        setBounds(x, y, width, height);
        this.hitbox = new Rectangle(x, y, width, height);
        this.speed = speed;
        this.movement = new Vector2(0, 0);
    }

    public void resetMovement() {
        movement.set(0, 0);
    }

    public void addMovement(int x, int y) {
        movement.add(x, y);
    }

    public void reverseMovement() {
        movement.rotateDeg(180);
    }

    // Consolidated "moveLeft()", "moveUp()" etc. into a single "doMove()" method to reduce repetition
    public void doMove() {
        float speedThisFrame = getSpeedThisFrame();
        translateX(movement.x * speedThisFrame);
        translateY(movement.y * speedThisFrame);
        hitbox.setPosition(getX(), getY());
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
