package io.github.yetti_eng.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import static io.github.yetti_eng.YettiGame.scaled;

// Called "Sprite" in the architecture documentation; renamed to avoid clash with LibGDX class name
public abstract class Entity extends Sprite {
    private float speed;
    private boolean solid;
    private final Vector2 movement;
    private final Rectangle hitbox;

    public Entity(Texture tex, float x, float y, float width, float height, float speed, boolean solid) {
        super(tex);

        // Scale all measurements to world size
        x = scaled(x);
        y = scaled(y);
        width = scaled(width);
        height = scaled(height);
        speed = scaled(speed);

        // Set this Entity's bounds and hitbox
        setBounds(x, y, width, height);
        this.hitbox = new Rectangle(x, y, width, height);
        this.speed = speed;
        this.solid = solid;
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
    public void doMove(float delta) {
        float speedThisFrame = getSpeedThisFrame(delta);
        translateX(movement.x * speedThisFrame);
        translateY(movement.y * speedThisFrame);
        hitbox.setPosition(getX(), getY());
    }

    public boolean collidedWith(Entity other) {
        return hitbox.overlaps(other.getHitbox());
    }

    public float getSpeedThisFrame(float delta) {
        return speed * delta;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    void setSolid(boolean solid) {
        this.solid = solid;
    }

    public boolean isSolid() {
        return solid;
    }
}
