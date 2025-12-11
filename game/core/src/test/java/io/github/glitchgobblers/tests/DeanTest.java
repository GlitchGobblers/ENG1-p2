package io.github.glitchgobblers.tests;

import com.badlogic.gdx.graphics.Texture;
import io.github.glitchgobblers.GdxTestBase;
import io.github.glitchgobblers.entities.Dean;
import io.github.glitchgobblers.entities.Player;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class DeanTest extends GdxTestBase {

@Test
  public void CalculateMovement(){
  //first create fake texture for the player and the dean
  Texture tex = Mockito.mock(Texture.class);
  Mockito.when(tex.getWidth()).thenReturn(32);
  Mockito.when(tex.getHeight()).thenReturn(32);

  Dean dean =new Dean(tex,0,0); //dean at position 0
  Player player = new Player(tex,-1,0); // player at the left
  dean.calculateMovement(player);
  dean.doMove(1f);// dean moves toward the player
  assertTrue(dean.getX()<0,"dean should move left when the player is in the left");


}



}

