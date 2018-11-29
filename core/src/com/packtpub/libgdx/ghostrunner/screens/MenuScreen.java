package com.packtpub.libgdx.ghostrunner.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.packtpub.libgdx.ghostrunner.util.Assets;
import com.packtpub.libgdx.ghostrunner.util.Constants;
//import com.packtpub.libgdx.ghostrunner.util.GamePreferences;
//import com.packtpub.libgdx.ghostrunner.util.CharacterSkin;

/**
 * This class will handle the menu screen
 */
public class MenuScreen extends AbstractGameScreen 
{
	 private static final String TAG = MenuScreen.class.getName();
	 /**
	 * Made by Philip Deppen (Assignment 6)
	 * @param game
	 */
	 public MenuScreen (Game game)
	 {
	        super(game);
	 }
	 
	 /**
	     * constantly clears the screen by filling it with a solid black.
	     * also checks whether the screen has been touched.
	     * @param deltaTime
	     */
	    @Override
	    public void render (float deltaTime)
	    {
	        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	        
	        if (Gdx.input.isTouched())
	        	game.setScreen(new GameScreen(game));
	    }
	    
	    @Override 
	    public void resize (int width, int height)
	    {
	    	
	    }
	    
	    @Override 
	    public void show ()
	    {
	    	
	    }
	    
	    @Override 
	    public void hide ()
	    {
	    	
	    }
	    
	    @Override 
	    public void pause ()
	    {
	    	
	    }

}

