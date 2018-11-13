package com.mygdx.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.ghostrunner.util.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.packtpub.libgdx.ghostrunner.Level;
import com.packtpub.libgdx.ghostrunner.game.objects.Rock;
import com.packtpub.libgdx.ghostrunner.util.Constants;

/** will need to create package for this */
/** testing */

public class WorldController extends InputAdapter
{
    
    private static final String TAG =
        WorldController.class.getName();
    
    
    public CameraHelper cameraHelper;
    public Level level;
    public int lives;
    public int score;
    
    /**
     * Initializes the level
     */
    private void initLevel()
    {
    	score = 0;
    	level = new Level(Constants.LEVEL_01);
    	cameraHelper.setTarget(level.boy);
    }
    
    /**
     * constructor
     */
    public WorldController() 
    {
        init();
    }
    
    /**
     * internal init (useful when reseting an object)
     */
    public void init() 
    {
        Gdx.input.setInputProcessor(this);
        cameraHelper = new CameraHelper();
        lives = Constants.LIVES_START;
        initLevel();
    }
    
    
    /**
     * Creates the procedural pixmap
     * @param width
     * @param height
     * @return
     */
    private Pixmap createProceduralPixmap (int width, int height) 
    {
        Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
        // Fill square with red color at 50% opacity
        pixmap.setColor(1, 0, 0, 0.5f);
        pixmap.fill();
        // Draw a yellow-colored X shape on square
        pixmap.setColor(1, 1, 0, 1);
        pixmap.drawLine(0, 0, width, height);
        pixmap.drawLine(width, 0, 0, height);
        // Draw a cyan-colored border around square
        pixmap.setColor(0, 1, 1, 1);
        pixmap.drawRectangle(0, 0, width, height);
        return pixmap;
    }
    
    /**
     * contains game logic
     * called several hundred times per sec
     * @param deltaTime
     */
    public void update(float deltaTime) 
    {
        handleDebugInput(deltaTime);
        cameraHelper.update(deltaTime);
        handleInputGame(deltaTime);
        level.update(deltaTime);
    }
    
    /**
     * Handles the debug input
     * @param deltaTime
     */
    private void handleDebugInput (float deltaTime) 
    {
        if (Gdx.app.getType() != ApplicationType.Desktop)
            return;
        
        if (!cameraHelper.hasTarget(level.boy))
        {	
	        // Camera Controls (move)
	        float camMoveSpeed = 5 * deltaTime;
	        float camMoveSpeedAccelerationFactor = 5;
	        if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
	            camMoveSpeed *= camMoveSpeedAccelerationFactor;
	        if (Gdx.input.isKeyPressed(Keys.LEFT))
	            moveCamera(-camMoveSpeed, 0);
	        if (Gdx.input.isKeyPressed(Keys.RIGHT))
	            moveCamera(camMoveSpeed, 0);
	        if (Gdx.input.isKeyPressed(Keys.UP))
	            moveCamera(0, camMoveSpeed);
	        if (Gdx.input.isKeyPressed(Keys.DOWN))
	            moveCamera(0, -camMoveSpeed);
	        if (Gdx.input.isKeyPressed(Keys.BACKSPACE))
	            cameraHelper.setPosition(0, 0);
        }
        
        // Camera Controls (zoom)
        float camZoomSpeed = 1 * deltaTime;
        float camZoomSpeedAccelerationFactor = 5;
        if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
            camZoomSpeed *= camZoomSpeedAccelerationFactor;
        if (Gdx.input.isKeyPressed(Keys.COMMA))
            cameraHelper.addZoom(camZoomSpeed);
        if (Gdx.input.isKeyPressed(Keys.PERIOD))
            cameraHelper.addZoom(-camZoomSpeed);
        if (Gdx.input.isKeyPressed(Keys.SLASH))
            cameraHelper.setZoom(1);
    }
    
    /**
     * Moves camera accordingly
     * @param x
     * @param y
     */
    private void moveCamera (float x, float y) 
    {
        x += cameraHelper.getPosition().x;
        y += cameraHelper.getPosition().y;
        cameraHelper.setPosition(x, y);
    }
    
    
    
    @Override
    /**
     * Appropriate key codes
     */
    public boolean keyUp (int keycode) 
    {
        // Reset game world
        if (keycode == Keys.R) 
        {
            init();
            Gdx.app.debug(TAG, "Game world resetted");
        }
        //Toggle camera follow
        else if (keycode == Keys.ENTER)
        {	
        	cameraHelper.setTarget(cameraHelper.hasTarget()
        			? null: level.boy);
        	Gdx.app.debug(TAG,  "Camera follow enabled: " + cameraHelper.hasTarget());
        }
        return false;
    }
    
    /**
     * allows player to be controllable with left and right arrow keys
     */
    private void handleInputGame (float deltaTime) {
       if (cameraHelper.hasTarget(level.boy)) {
           // Player Movement
           if (Gdx.input.isKeyPressed(Keys.LEFT)) {
               level.boy.velocity.x = -level.boy.terminalVelocity.x;
           }
           else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
               level.boy.velocity.x = level.boy.terminalVelocity.x;
           }
           else {
               // Execute auto-forward movement on non-desktop platform
               if (Gdx.app.getType() != ApplicationType.Desktop) {
                   level.boy.velocity.x = level.boy.terminalVelocity.x;
               }
           }
           // Bunny Jump
           if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE)) {
             level.boy.setJumping(true);
           } else {
             level.boy.setJumping(false);
           }
       }
       }

}
