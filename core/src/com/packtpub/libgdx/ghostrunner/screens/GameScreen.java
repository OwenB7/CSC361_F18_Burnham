package com.packtpub.libgdx.ghostrunner.screens;
import java.io.IOException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.WorldController;
import com.mygdx.game.WorldRenderer;
import com.packtpub.libgdx.ghostrunner.util.GamePreferences;

/**
 * This class will handle the game screen 
 */
public class GameScreen extends AbstractGameScreen
{
        private static final String TAG = GameScreen.class.getName();
        private WorldController worldController;
        private WorldRenderer worldRenderer;
        private boolean paused;
        public GameScreen (Game game)
        {
            super(game);
        }
    
    @Override
    public void render(float deltaTime)
    {
        // Do not update game world when paused.
        if (!paused)
        {
            // Update game world by the time that has passed
            // since last rendered frame.
            try 
            {
				worldController.update(deltaTime);
			} catch (NumberFormatException e) 
            {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) 
            {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        // Sets the clear screen color to: Purple
        Gdx.gl.glClearColor(0x99 / 255.0f, 0x32 / 255.0f,0xcc /
        255.0f, 0xff / 255.0f);
        // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Render game world to screen
        worldRenderer.render();
    }
    
    @Override
    /**
     * resizes game screen
     */
    public void resize(int width, int height)
    {
        worldRenderer.resize(width, height);
    }
    
    @Override 
    /**
     * Ensures that the game screen will always work with the
     * latest game settings
     */
    public void show()
    {
        GamePreferences.instance.load();
        worldController = new WorldController(game);
        worldRenderer = new WorldRenderer(worldController);
        Gdx.input.setCatchBackKey(true);
    }
    
    @Override
    /**
     * disposes 
     */
    public void hide()
    {
        worldRenderer.dispose();
        Gdx.input.setCatchBackKey(false);
    }
    
    @Override
    /**
     * changes to a paused state
     */
    public void pause()
    {
        paused = true;
    }
    
    @Override
    /**
     * unpaused state
     */
    public void resume()
    {
        super.resume();
        // Only called on Android!
        paused = false;
    }
        
  }

