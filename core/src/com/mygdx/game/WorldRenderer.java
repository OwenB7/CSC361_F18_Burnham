package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.packtpub.libgdx.ghostrunner.util.Assets;
import com.packtpub.libgdx.ghostrunner.util.Constants;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.mygdx.game.WorldController;
import com.packtpub.libgdx.ghostrunner.*;
import com.packtpub.libgdx.ghostrunner.game.objects.*;
import com.packtpub.libgdx.ghostrunner.util.*;


/**
 * The class the draws everything as its updated
 */
public class WorldRenderer implements Disposable
{
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private WorldController worldController;
    private OrthographicCamera cameraGUI;
    
    /**
     * Makes an instance of WorldRenderer
     * @param worldController
     */
    public WorldRenderer (WorldController worldController)
    {
        this.worldController = worldController;
        init();
    }
    
    /**
     * initializes
     */
    private void init ()
    {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH,
        Constants.VIEWPORT_HEIGHT);
        camera.position.set(0, 0, 0);
        camera.update();
        cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH,
                Constants.VIEWPORT_GUI_HEIGHT);
                cameraGUI.position.set(0, 0, 0);
                cameraGUI.setToOrtho(true); // flip y-axis
                cameraGUI.update();
    }
    
    /**
     * Renders the test objects
     */
    public void render ()
    {
        renderWorld(batch);
        renderGui(batch);
    }
    
    
    /**
     * Resizes the objects
     * @param width
     * @param height
     */
    public void resize (int width, int height)
    {
        camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) *
                width;
                camera.update();
                cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
                cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT / (float)height) * (float)width;
                cameraGUI.position.set(cameraGUI.viewportWidth / 2,
                cameraGUI.viewportHeight / 2, 0);
                cameraGUI.update();
    }
    
    /**
     * disposes due to c code
     */
    @Override public void dispose ()
    {
        batch.dispose();
    }
    
    /**
     * Calls the render() method of Level to draw all the
     * game objects of the loaded level
     * @param batch
     */
    private void renderWorld (SpriteBatch batch)
    {
    	worldController.cameraHelper.applyTo(camera);
    	batch.setProjectionMatrix(camera.combined);
    	batch.begin();
    	worldController.level.render(batch);
    	batch.end();
    }
    
    /**
     *  This code adds Gui score to top left of screen
     * @param batch
     */
    private void renderGuiScore (SpriteBatch batch)
    {
        float x = -15;
        float y = -15;
        batch.draw(Assets.instance.candyCorn.candyCorn,
        x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
        Assets.instance.fonts.defaultBig.draw(batch,
        "" + worldController.score,
        x + 75, y + 37);
    }
    
    /**
     *  This code adds three running boys to indicate the lives in the GUI
     * @param batch
     */
    private void renderGuiExtraLive (SpriteBatch batch)
    {
        float x = cameraGUI.viewportWidth - 50 -
        Constants.LIVES_START * 50;
        float y = -15;
        
        for (int i = 0; i < Constants.LIVES_START; i++)
        {
        if (worldController.lives <= i)
            batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
            batch.draw(Assets.instance.boy.boy,
            x + i * 50, y, 50, 50, 120, 100, 0.35f, -0.35f, 0);
            batch.setColor(1, 1, 1, 1);
        }
    }

    /**
     * Added frame rate counter to GUI to display
     * @param batch
     */
    private void renderGuiFpsCounter (SpriteBatch batch)
    {
        float x = cameraGUI.viewportWidth - 55;
        float y = cameraGUI.viewportHeight - 15;
        int fps = Gdx.graphics.getFramesPerSecond();
        BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
        if (fps >= 45)
        {
            // 45 or more FPS show up in green
           fpsFont.setColor(0, 1, 0, 1);
        }
        else if (fps >= 30)
        {
            // 30 or more FPS show up in yellow
           fpsFont.setColor(1, 1, 0, 1);
        }
        else
        {
            // less than 30 FPS show up in red
            fpsFont.setColor(1, 0, 0, 1);
        }
        fpsFont.draw(batch, "FPS: " + fps, x, y);
        fpsFont.setColor(1, 1, 1, 1); // white
    }
    
    /**
     * Draws icons to GUI
     * @param batch
     */
    private void renderGui (SpriteBatch batch)
    {
        batch.setProjectionMatrix(cameraGUI.combined);
        batch.begin();
        // draw collected candycorn icon + text
        // (anchored to top left edge)
        renderGuiScore(batch);
        // draw extra lives icon + text (anchored to top right edge)
        renderGuiExtraLive(batch);
        // draw collected pumpkin icon (anchored to top left edgeL
        renderGuiPumpkinPowerup(batch);
        // draw FPS text (anchored to bottom right edge)
        renderGuiFpsCounter(batch);
        // draw game over text
        renderGuiGameOverMessage(batch);
        batch.end();
    } 
    
    /**
     * This function adds the text that says Game Over when lives run out
     * @param batch
     */
    private void renderGuiGameOverMessage (SpriteBatch batch) 
    {
        float x = cameraGUI.viewportWidth / 2;
        float y = cameraGUI.viewportHeight / 2;
        if (worldController.isGameOver()) 
        {
	        BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
	        fontGameOver.setColor(1, 0.75f, 0.25f, 1);
	        fontGameOver.draw(batch, "GAME OVER", x, y, 0, Align.center, false);        // need to fix alignment center
	        fontGameOver.setColor(1, 1, 1, 1);
        }
    }
    
  /**
    * This method first checks whether there is still time left for the feather power-up effect
    * to end. Only if this is the case, a feather icon is drawn in the top-left corner under the
    * gold coin icon. A small number is drawn next to it that displays the rounded time
    * that is still left until the effect vanishes.
    */
    private void renderGuiPumpkinPowerup (SpriteBatch batch) 
    {
        float x = -15;
        float y = 30;
        float timeLeftPumpkinPowerup =
        worldController.level.boy.timeLeftPumpkinPowerup;
        if (timeLeftPumpkinPowerup > 0) 
        {
        	// Start icon fade in/out if the left power-up time
        	// is less than 4 seconds. The fade interval is set
        	// to 5 changes per second.
        	if (timeLeftPumpkinPowerup < 4) 
        	{
        		if (((int)(timeLeftPumpkinPowerup * 5) % 2) != 0) 
        		{	
        			batch.setColor(1, 1, 1, 0.5f);
        		}
        	}
        batch.draw(Assets.instance.pumpkin.pumpkin,
        x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
        batch.setColor(1, 1, 1, 1);
        Assets.instance.fonts.defaultSmall.draw(batch,
        "" + (int)timeLeftPumpkinPowerup, x + 60, y + 57);
        }
    }
    


}

