package com.packtpub.libgdx.ghostrunner;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.assets.AssetManager;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.WorldController;
import com.mygdx.game.WorldRenderer;
/**
 * This class has the major credentials for the game
 * in order for it to run and update as needed
 */
public class GhostRunnerMain implements ApplicationListener
{
	private static final String TAG = 
	GhostRunnerMain.class.getName();
	
	private com.mygdx.game.WorldController worldController;
	private WorldRenderer worldRenderer;
	private boolean paused;
	
	/**
	 * This creates and initializes the controller and renderer
	 * Will also ensure the game world is active on start and that
	 * the assets are loaded in
	 */
	@Override public void create () 
	{ 
		// Set Libgdx log level to DEBUG
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Load assets
		Assets.instance.init(new AssetManager());
		// Initialize controller and renderer
		worldController = new WorldController();
		worldRenderer = new WorldRenderer(worldController);
		// Game world is active on start
		paused = false;
		// Load assets
		Assets.instance.init(new AssetManager());
	}
	
	/**
	 * Game is rendered and updated when needed
	 */
	@Override public void render () 
	{ 
		// Do not update game world when paused.
		if (!paused) {
		// Update game world by the time that has passed
		// since last rendered frame.
		worldController.update(Gdx.graphics.getDeltaTime());
		}
		// Sets the clear screen color to: Cornflower Blue
		Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f, 0xff/255.0f);
		// Clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Render game world to screen
		worldRenderer.render();
	}
	
	/**
	 * This method resizes the world renderer as needed
	 */
	@Override public void resize (int width, int height) 
	{
		worldRenderer.resize(width, height);
	}
	
	/**
	 * This method pauses the game
	 */
	@Override public void pause () 
	{ 
		paused = true;
	}
	
	/**
	 * This method resumes the game
	 */
	@Override public void resume () 
	{ 
		paused = false;
	}
	
	/**
	 * This method disposes the worldRenderer as needed
	 */
	@Override public void dispose () 
	{
		worldRenderer.dispose();
		Assets.instance.dispose();
	}
}
