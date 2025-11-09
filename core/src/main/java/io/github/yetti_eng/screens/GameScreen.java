package io.github.yetti_eng.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.yetti_eng.*;
import io.github.yetti_eng.entities.Dean;
import io.github.yetti_eng.entities.Entity;
import io.github.yetti_eng.entities.Item;
import io.github.yetti_eng.entities.Player;
import io.github.yetti_eng.events.*;

import java.util.ArrayList;

import static io.github.yetti_eng.YettiGame.scaled;

public class GameScreen implements Screen {
    private final YettiGame game;
    private final Stage stage;

    private static final int TIMER_LENGTH = 300; // 300s = 5min

    private Texture ballmanTexture;
    private Texture exitTexture;
    private Texture keyTexture;
    private Texture doorTexture;
    private Texture duckTexture;
    private Texture surprisedTexture;
    private Texture angryTexture;
    private Texture pauseTexture;

    private MapManager mapManager;
    private OrthographicCamera camera;

    private OrthographicCamera interfaceCamera;

    private Player player;
    private Dean dean;
    private Item exit;
    private final ArrayList<Entity> entities = new ArrayList<>();

    private Label hiddenText;
    private Label negativeText;
    private Label positiveText;
    private Label timerText;
    private final ArrayList<Label> messages = new ArrayList<>();
    private Button pauseButton;

    public GameScreen(final YettiGame game) {
        this.game = game;
        stage = new Stage(game.viewport, game.batch);
    }

    @Override
    public void show() {
        //load textures for entities and items
        ballmanTexture = new Texture("placeholder/ballman.png");
        exitTexture = new Texture("placeholder/exit.png");
        keyTexture = new Texture("placeholder/key.png");
        doorTexture = new Texture("placeholder/door.png");
        duckTexture = new Texture("placeholder/duck.png");
        surprisedTexture = new Texture("placeholder/surprised.png");
        angryTexture = new Texture("placeholder/angry.png");
        pauseTexture = new Texture("placeholder/pause.png");

        //main camera for map rendering
        camera = new  OrthographicCamera();
        camera.setToOrtho(false, 90, 60);
        //secondary camera for user interface (timer, labels)
        interfaceCamera = new  OrthographicCamera();
        interfaceCamera.setToOrtho(false, scaled(16), scaled(9));
        //loads tilemap
        mapManager = new MapManager(camera);
        mapManager.loadMap("map/map.tmx");

        //creates player, enemy and exit
        player = new Player(ballmanTexture, 55, 25);
        exit = new Item(new WinEvent(), "exit", exitTexture, 80.5f, 52);
        dean = new Dean(angryTexture, -2, 4.5f);
        dean.disable();
        dean.hide();

        //new key checkin code item set to 2x bigger
        Item checkin = new Item(new KeyEvent(), "key", keyTexture, 34, 29);
        checkin.setOriginCenter();
        checkin.setScale(2f);
        entities.add(checkin);

        //create negative event: locked door
        Item classroomDoor = new Item(new DoorEvent(), "door", doorTexture, 44.5f, 21.5f, false, true);
        classroomDoor.setOriginCenter();
        classroomDoor.setScale(2f); //door set to 2x bigger
        classroomDoor.enable();
        entities.add(classroomDoor);

        // create positive event: longboi item
        Item longBoi = new Item(new IncreasePointsEvent(), "long_boi", duckTexture, 3, 9);
        longBoi.setOriginCenter();
        longBoi.setScale(2f); //make longboi twice as big
        entities.add(longBoi);

        //define hidden event: colliding with student
        Item hiddenStudent = new Item(new HiddenDeductPointsEvent(), "surprised_student", surprisedTexture, 60, 11, true, true);
        hiddenStudent.setOriginCenter();
        hiddenStudent.setScale(2f); //make hidden student twice as big
        entities.add(hiddenStudent);

        //start new timer
        game.timer = new Timer(TIMER_LENGTH);
        game.timer.play();
        //create labels and position timer and event counters on screen
        timerText = new Label(null, new Label.LabelStyle(game.font, Color.WHITE.cpy()));
        timerText.setPosition(0, scaled(8.5f));
        hiddenText = new Label(null, new Label.LabelStyle(game.fontBorderedSmall, Color.WHITE.cpy()));
        hiddenText.setPosition(scaled(4f), scaled(8.5f));
        negativeText = new Label(null, new Label.LabelStyle(game.fontBorderedSmall, Color.WHITE.cpy()));
        negativeText.setPosition(scaled(7f), scaled(8.5f));
        positiveText = new Label(null, new Label.LabelStyle(game.fontBorderedSmall, Color.WHITE.cpy()));
        positiveText.setPosition(scaled(10f), scaled(8.5f));

        Gdx.input.setInputProcessor(stage);
        pauseButton = new Button(new TextureRegionDrawable(pauseTexture)); //create pause button
        pauseButton.setSize(scaled(1), scaled(1));
        pauseButton.setPosition(scaled(3f), scaled(8.5f), Align.center);
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (game.isPaused()) {
                    game.resume();
                } else {
                    game.pause();
                }
            }
        });
        stage.addActor(pauseButton);
    }

    @Override
    public void render(float delta) {
        input(delta); //handles player input
        logic(delta); //handles collisions and events
        draw(delta); //draws map and entities to screen
        postLogic(delta); // Used for logic that should happen after rendering, normally screen changes
    }

    private void input(float delta) {
        float dx = 0, dy = 0;
        float currentX = player.getX();
        float currentY = player.getY();
        float speed = player.getSpeedThisFrame(delta);

        player.resetMovement();
        //horizontal movement
        if (InputHelper.moveRightPressed()) {
            dx += speed; }
        if (InputHelper.moveLeftPressed()) {
            dx -= speed; }
        //vertical movement
        if (InputHelper.moveUpPressed()) {
            dy  += speed; }
        if (InputHelper.moveDownPressed()) {
            dy -= speed; }

        Rectangle hitbox = player.getHitbox();
        //tests if collision occurs after x movement
        hitbox.setPosition(currentX + dx, currentY);
        if (!mapManager.isRectInvalid(player.getHitbox())) {
            player.addMovement(dx, 0);
        }
        //tests if collision occurs after y movement
        hitbox.setPosition(currentX, currentY + dy );
        if (!mapManager.isRectInvalid(player.getHitbox())) {
            player.addMovement(0, dy);
        }
        //sets the hitbox to correct player location
        hitbox.setPosition(player.getX(), player.getY());
    }

    private void logic(float delta) {
        Vector2 currentPos = player.getCurrentPos(); //save initial position of player
        //move only if game isn't paused
        if (!game.isPaused()) {
            player.doMove(delta, true);
            if (dean.isEnabled()) {
                dean.calculateMovement(player);
                dean.doMove(delta);
            }
        }

        //checks for collisions with entities
        entities.forEach(e -> {
            if (player.collidedWith(e) && e.isEnabled()) {
                //solid entities cause player to remain in position
                if (e.isSolid()) {
                    //set the position of player to previous position
                    player.setPosition(currentPos.x, currentPos.y);
                }
                //collision with item causes event
                if (e instanceof Item item) {
                    item.interact(game, player); //triggers event interaction
                }
            }
        });

        // Clamp to edges of screen
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();

        float playerWidth = player.getWidth();
        float playerHeight = player.getHeight();

        player.setX(MathUtils.clamp(player.getX(), 0, worldWidth - playerWidth));
        player.setY(MathUtils.clamp(player.getY(), 0, worldHeight - playerHeight));

        // Calculate remaining time
        int timeRemaining = game.timer.getRemainingTime();
        String text = (timeRemaining / 60) + ":" + String.format("%02d", timeRemaining % 60);
        timerText.setText(text);
        timerText.setStyle(new Label.LabelStyle(game.fontBordered, (game.timer.isActive() ? Color.WHITE : Color.RED).cpy()));

        //updates event counters
        hiddenText.setText("Hidden:" + EventCounter.getHiddenCount());
        positiveText.setText("Positive:" + EventCounter.getPositiveCount());
        negativeText.setText("Negative:" + EventCounter.getNegativeCount());

        // Release the Dean if the timer is at 60 or less
        if (timeRemaining <= 60 && !dean.isEnabled()) {
            spawnLargeMessage("Run! The dean is coming!");
            dean.show();
            dean.enable();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) { //pauses game when escape is pressed
            if (game.isPaused()) {
                game.resume();
            } else {
                game.pause();
            }
        }
    }

    private void draw(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 1f); //clears screen
        camera.update();
        //draw map
        mapManager.render();
        game.viewport.apply();

        //main camera with map and entities
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        // Draw only visible entities
        entities.forEach(e -> { if (e.isVisible()) e.draw(game.batch); });
        // Draw exit, player, and dean on top of other entities
        if (exit.isVisible()) exit.draw(game.batch);
        if (player.isVisible()) player.draw(game.batch);
        if (dean.isVisible()) dean.draw(game.batch);
        game.batch.end();

        //separate user interface camera for text on screen
        game.batch.setProjectionMatrix(interfaceCamera.combined);
        game.batch.begin();

        if (game.isPaused()) {
            game.fontBordered.draw(
                game.batch, "PAUSED",
                0, interfaceCamera.viewportHeight / 2, interfaceCamera.viewportWidth,
                Align.center, false
            );
        }

        //draw timer and event counters to screen
        timerText.draw(game.batch, 1.0f);
        hiddenText.draw(game.batch, 1.0f);
        positiveText.draw(game.batch, 1.0f);
        negativeText.draw(game.batch, 1.0f);

        //draws messages fading out in an upwards direction
        for (Label l : messages) {
            l.setY(l.getY()+1);
            l.getColor().add(0, 0, 0, -0.01f);
            l.draw(game.batch, 1);
        }
        messages.removeIf(l -> l.getColor().a <= 0);

        game.batch.end();

        // Finally, draw elements on the stage (clickable elements)
        stage.draw();
    }

    private void postLogic(float delta) {
        // Exit collision
        if (player.collidedWith(exit) && exit.isEnabled()) {
            exit.interact(game, player);
            return;
        }
        // Dean collision
        if (player.collidedWith(dean) && dean.isEnabled()) {
            dean.getsPlayer(game);
            return;
        }
        // Timer runs out then player loses
        if (game.timer.hasElapsed()) {
            game.timer.finish();
            game.setScreen(new LoseScreen(game));
            dispose();
            return;
        }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        game.timer.pause();
    }

    @Override
    public void resume() {
        game.timer.play();
    }

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        ballmanTexture.dispose();
        exitTexture.dispose();
        keyTexture.dispose();
        doorTexture.dispose();
        duckTexture.dispose();
        surprisedTexture.dispose();
        angryTexture.dispose();
        pauseTexture.dispose();
        mapManager.dispose();
        stage.dispose();
    }

    /**
     * Spawn a text label at the centre of the screen
     * that floats upwards and fades out. Used to alert the player.
     * @param text The text that should be displayed.
     */
    public void spawnLargeMessage(String text) {
        Label label = new Label(text, new Label.LabelStyle(game.fontBordered, Color.WHITE.cpy()));
        label.setPosition(scaled(8), scaled(4.5f), Align.center);
        messages.add(label);
    }

    /**
     * Spawn a small text label at the bottom right of the screen
     * that floats upwards and fades out. Used when interacting with Items.
     * @param text The text that should be displayed.
     */
    public void spawnInteractionMessage(String text) {
        Label label = new Label(text, new Label.LabelStyle(game.fontBorderedSmall, Color.WHITE.cpy()));
        label.setPosition(interfaceCamera.viewportWidth, label.getHeight(), Align.right);
        messages.add(label);
    }
}
