package io.github.yetti_eng.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.yetti_eng.InputHelper;

import static io.github.yetti_eng.YettiGame.scaled;

// Called "Sprite" in the architecture documentation; renamed to avoid clash with LibGDX class name
public abstract class Entity extends Sprite {
    private float speed;
    private boolean solid;
    private Vector2 movement;
    private final Rectangle hitbox;

    private boolean visible = true;
    private boolean enabled = true;

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

    public void addMovement(float x, float y) {
        movement.add(x, y);
    }

    public void reverseMovement() {
        movement.rotateDeg(180);
    }

    /**
     * Convert this Entity's current queued movement into a unit vector
     * (that is, ensure that this Entity moves exactly the distance of 1 tile).
     */
    public void normaliseMovement() {
        movement = InputHelper.makeUnitVector(movement);
    }

    // Consolidated "moveLeft()", "moveUp()" etc. into a single "doMove()" method to reduce repetition
    public void doMove(float delta) {
        float speedThisFrame = getSpeedThisFrame(delta);
        translateX(movement.x * speedThisFrame);
        translateY(movement.y * speedThisFrame);
        hitbox.setPosition(getX(), getY());
    }

    Rectangle getHitbox() {
        return hitbox;
    }

    public boolean collidedWith(Entity other) {
        // If disabled, do not check for collisions
        return enabled && hitbox.overlaps(other.getHitbox());
    }

    public void show() {
        visible = true;
    }

    public void hide() {
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public float getSpeedThisFrame(float delta) {
        return speed * delta;
    }

    void setSolid(boolean solid) {
        this.solid = solid;
    }

    public boolean isSolid() {
        return solid;
    }
}
