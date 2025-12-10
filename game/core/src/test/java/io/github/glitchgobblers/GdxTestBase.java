package io.github.glitchgobblers;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mockito;

public class GdxTestBase {
    @BeforeAll
    public static void init() {
        if (Gdx.app == null) {
            HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
            
            // Create a headless application to set up Gdx.files, Gdx.app etc.
            new HeadlessApplication(new ApplicationListener() {
                @Override public void create() {}
                @Override public void resize(int width, int height) {}
                @Override public void render() {}
                @Override public void pause() {}
                @Override public void resume() {}
                @Override public void dispose() {}
            }, config);

            // Mock OpenGL to prevent crashes when classes try to use graphics
            Gdx.gl = Mockito.mock(GL20.class);
            Gdx.gl20 = Gdx.gl;
        }
    }
}