package com.mygdx.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IdentityMap.Entry;
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
import com.packtpub.libgdx.ghostrunner.game.objects.AbstractGameObject;
import com.packtpub.libgdx.ghostrunner.game.objects.Rock;
import com.packtpub.libgdx.ghostrunner.game.objects.CandyCorn;
import com.packtpub.libgdx.ghostrunner.game.objects.Pumpkin;
import com.packtpub.libgdx.ghostrunner.game.objects.Boy;
import com.packtpub.libgdx.ghostrunner.game.objects.Boy.JUMP_STATE;
import com.packtpub.libgdx.ghostrunner.util.Constants;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.Game;
import com.packtpub.libgdx.ghostrunner.screens.MenuScreen;
import com.packtpub.libgdx.ghostrunner.util.AudioManager;


/** will need to create package for this */
/** testing */

public class WorldController extends InputAdapter
{
    
    private static final String TAG =
        WorldController.class.getName();
    
    private Game game;
  
    
    public CameraHelper cameraHelper;
    public Level level;
    public int lives;
    public int score;
    public World b2world;
    private float timeLeftGameOverDelay;
    public float livesVisual;
    
    /**
     * Initializes the level
     */
    private void initLevel()
    {
    	score = 0;
    	level = new Level(Constants.LEVEL_01);
    	cameraHelper.setTarget(level.boy);
    	initPhysics();
    }
    
    /**
     * constructor
     */
    public WorldController(Game game) 
    {
    	this.game = game;
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
        livesVisual = lives;
        timeLeftGameOverDelay = 0;
        initLevel();
        //initPhysics();
        
        
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
    public void update(float deltaTime) throws NumberFormatException, IOException
    {
    	BufferedReader reader = new BufferedReader(
                new FileReader(new File("../core/assets/images/myFile.txt")));
		TreeMap<Integer, String> highestScores = new TreeMap<Integer, String>();
		
		String line = null;
		while ((line = reader.readLine()) != null) 
		{ // read your file line by line
			String[] playerScores = line.split(": ");
			highestScores.put(Integer.valueOf(playerScores[1]), playerScores[0]);
		}
		
		// iterate in descending order
		for (Integer score : highestScores.descendingKeySet()) 
		{
			String highScore = (highestScores.get(score) + ": " + score);
		}
		java.util.Map.Entry<Integer, String> luckyNumber =  highestScores.lastEntry();
		reader.close();
        handleDebugInput(deltaTime);
        if (isGameOver())
        {
        	timeLeftGameOverDelay -= deltaTime;
        	if (timeLeftGameOverDelay < 0)
        	{
        		AudioManager.instance.play(Assets.instance.music.song01);
        		backToMenu();
        	}
        	else
        	{
        		handleInputGame(deltaTime);
        	}
        }
        handleInputGame(deltaTime);
        level.update(deltaTime);
        b2world.step(deltaTime, 8, 3);
        cameraHelper.update(deltaTime);
        if (!isGameOver() && isPlayerBelowLevel())
        {
        	AudioManager.instance.play(Assets.instance.sounds.liveLost);
        	lives--;
        	if (isGameOver())
        	{
        		JOptionPane.showMessageDialog(null, luckyNumber);
        		timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
        	}
        	else
        		initLevel();
        }
        level.graveyard.updateScrollPosition(cameraHelper.getPosition());
        if (livesVisual > lives)
        	livesVisual = Math.max(lives,  livesVisual -  1 * deltaTime);
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
        			? null: Level.boy);
        	Gdx.app.debug(TAG,  "Camera follow enabled: " + cameraHelper.hasTarget());
        }
        // back to menu
        else if (keycode == Keys.ESCAPE || keycode == Keys.BACK)
        {
        	AudioManager.instance.play(Assets.instance.music.song01);
        	backToMenu();
        }
        return false;
    }
    
    /**
     * allows player to be controllable with left and right arrow keys
     * also tells player when to jump
     */
    private void handleInputGame (float deltaTime) 
    {
       if (cameraHelper.hasTarget(Level.boy)) 
       {
           // Player Movement
           if (Gdx.input.isKeyPressed(Keys.A)) 
           {
               Level.boy.body.setLinearVelocity(new Vector2(-3,0));   //velocity.x = -level.boy.terminalVelocity.x;
               Level.boy.velocity.x = -3.0f;
           }
           else if (Gdx.input.isKeyPressed(Keys.D)) 
           {
               Level.boy.body.setLinearVelocity(new Vector2(3,0));    //velocity.x = level.boy.terminalVelocity.x;
               Level.boy.velocity.x = 3.0f;
           }
           
           else 
           {
               // Execute auto-forward movement on non-desktop platform
               if (Gdx.app.getType() != ApplicationType.Desktop) 
               {
                   Level.boy.velocity.x = Level.boy.terminalVelocity.x;
               }
           }
           // Boy Jump
           if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE)) 
           {  
             Level.boy.setJumping(true);
           } 
           else 
           {
             Level.boy.setJumping(false);
           }
       }
    }
    
    /**
     * Initializes the Box2d world
     * Additionally creates all the bodies for each object in the 
     * game and gives them the appropriate characteristics
     */
    public void initPhysics()
    {
    	if (b2world != null) 
    		b2world.dispose();
        b2world = new World(new Vector2(0, -9.81f), true);
        b2world.setContactListener(new CollisionHandler(this));
    	
    	Vector2 origin = new Vector2();
    	
    	
    	
    	// creating the rock body
    	for (Rock rock : level.rocks)
    	{
    		BodyDef bodyDef = new BodyDef();
    		bodyDef.type = BodyType.KinematicBody;
    		bodyDef.position.set(rock.position);
    		Body body = b2world.createBody(bodyDef);
    		// set user data
    		body.setUserData(rock);
    		rock.body = body;
    		PolygonShape polygonShape = new PolygonShape();
    		origin.x = rock.bounds.width / 2.0f;
    		origin.y = rock.bounds.height / 2.0f;
    		polygonShape.setAsBox(rock.bounds.width / 2.0f,
    				rock.bounds.height / 2.0f,origin, 0);
    		FixtureDef fixtureDef = new FixtureDef();
    		fixtureDef.friction = 0.5f;
    		fixtureDef.shape = polygonShape;
    		body.createFixture(fixtureDef);
    		polygonShape.dispose();
    	}
    	
    	// creating the candy corn body
    	for (CandyCorn candyCorn : level.candycorn)
    	{
    		BodyDef bodyDef = new BodyDef();
    		bodyDef.type = BodyType.KinematicBody;
    		bodyDef.position.set(candyCorn.position);
    		Body body = b2world.createBody(bodyDef);
    		body.setUserData(candyCorn);
    		
    		candyCorn.body = body;
    		PolygonShape polygonShape = new PolygonShape();
    		origin.x = candyCorn.bounds.width / 2.0f;
    		origin.y = candyCorn.bounds.height / 2.0f;
    		polygonShape.setAsBox(candyCorn.bounds.width / 2.0f,
    				candyCorn.bounds.height / 2.0f,origin, 0);
    		FixtureDef fixtureDef = new FixtureDef();
    		fixtureDef.shape = polygonShape;
    		fixtureDef.isSensor = true;
    		body.createFixture(fixtureDef);
    		polygonShape.dispose();
    	}
    	
    	// creating the pumpkin body
    	for (Pumpkin pumpkin : level.pumpkins)
    	{
    		BodyDef bodyDef = new BodyDef();
    		bodyDef.type = BodyType.KinematicBody;
    		bodyDef.position.set(pumpkin.position);
    		Body body = b2world.createBody(bodyDef);
    		// set user data
    		body.setUserData(pumpkin);
    		
    		pumpkin.body = body;
    		PolygonShape polygonShape = new PolygonShape();
    		origin.x = pumpkin.bounds.width / 2.0f;
    		origin.y = pumpkin.bounds.height / 2.0f;
    		polygonShape.setAsBox(pumpkin.bounds.width / 2.0f,
    				pumpkin.bounds.height / 2.0f,origin, 0);
    		FixtureDef fixtureDef = new FixtureDef();
    		fixtureDef.shape = polygonShape;
    		fixtureDef.isSensor = true;
    		body.createFixture(fixtureDef);
    		polygonShape.dispose();
    	}
    	
    	// creating the boy body
    	Boy boy = Level.boy;
    
    		BodyDef bodyDef = new BodyDef();
    		bodyDef.type = BodyType.DynamicBody;
    		bodyDef.position.set(boy.position);
    		Body body = b2world.createBody(bodyDef);
    		body.setUserData(boy);
    		boy.body = body;
    		PolygonShape polygonShape = new PolygonShape();
    		origin.x = boy.bounds.width / 2.0f;
    		origin.y = boy.bounds.height / 2.0f;
    		polygonShape.setAsBox(boy.bounds.width / 2.0f,
    				boy.bounds.height / 2.0f,origin, 0);
    		FixtureDef fixtureDef = new FixtureDef();
    		fixtureDef.shape = polygonShape;
    		body.createFixture(fixtureDef);
    		polygonShape.dispose();
    	
    	
    	
    }
   
    /**
     * Sees if the player ran out of lives
     * @return
     */
    public boolean isGameOver()
    {
    	return lives < 0;
    }
    
    /**
     * Sees if the player fell to their death
     * @return
     */
    public boolean isPlayerBelowLevel()
    {
    	return level.boy.position.y < -5;
    }
    
    /**
     * Switches to the menu screen
     */
    private void backToMenu()
    {
    	game.setScreen(new MenuScreen(game));
    }
}
