package io.github.yetti_eng.events;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import io.github.yetti_eng.YettiGame;
import io.github.yetti_eng.entities.Entity;
import io.github.yetti_eng.entities.Item;
import io.github.yetti_eng.entities.Player;
import io.github.yetti_eng.screens.GameScreen;

public class Event extends Item {
    private int counter = 0;
    private String interactionMessage;
    private String interactionImagePath;
    private Texture interactionImage;
    private Vector2 interactionPosition;
    private Vector2 interactionSize;
    private int scoreModifier;
    private boolean visible;
    private SpriteBatch batch;
    private boolean used = false;
    private String key = null;
    private String lock = null;
    public Event(MapProperties properties){
        super(new Texture((String) properties.get("interactionImagePath")),
            (Float) properties.get("x") / 48,
            (Float) properties.get("y") / 48,
            (Float) properties.get("width") / 48,
            (Float) properties.get("height") / 48,
            (Boolean) properties.get("visible"),
            (Boolean) properties.get("solid"));
        interactionMessage = (String) properties.get("interactionMessage");
        interactionImagePath = (String) properties.get("interactionImagePath");
        interactionImage = new Texture(interactionImagePath);
        interactionPosition = new Vector2((Float) properties.get("x") / 48, (Float) properties.get("y") / 48);
        interactionSize = new Vector2((Float) properties.get("width") / 48, (Float) properties.get("height") / 48);
        visible = (Boolean) properties.get("visible");
        scoreModifier = (Integer) properties.get("scoreModifier");
        if (properties.get("key")!=null){
            key = (String) properties.get("key");
            super.addKey(key);
        }
        if (properties.get("lock")!=null){
            lock = (String) properties.get("lock");
        }

    }
    public int getScoreModifier(){
        return scoreModifier;
    }
    public void showInteractionMessage(GameScreen screen){
        screen.spawnInteractionMessage(interactionMessage);
    }
    @Override
    public void render(SpriteBatch batch) {
            batch.draw(interactionImage, interactionPosition.x, interactionPosition.y, interactionSize.x, interactionSize.y);
    }



    public boolean activate(final GameScreen screen, Player player, Item item){
        if (lock!=null) {
            if (player.hasKey(lock)) {
                super.setSolid(false);
                this.setSolid(false);
                if (!used) {
                    used = true;
                    this.toggleVisibility();
                    screen.spawnInteractionMessage(interactionMessage);
                    return true;
                }
            }
        }
        else {
            if (!used) {
                used = true;
                this.toggleVisibility();
                screen.spawnInteractionMessage(interactionMessage);
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean isVisible(){
        return visible;
    }
    private void toggleVisibility() {
        visible = !visible;
    }


    // Consolidated "scoreIncrement" and "scoreDecrement" into single "modifyScore" method
    public void modifyScore(final YettiGame game) {
        game.score += getScoreModifier();
    }


    public int getCounter() {
        return counter;
    }

    public void incrementCounter() {
        this.counter += 1;
    }
}
