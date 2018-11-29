package com.packtpub.libgdx.ghostrunner.screens;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.packtpub.libgdx.ghostrunner.util.Assets;

/**
 * Contains the information that all game screens
 * will inherit from
 */
public abstract class AbstractGameScreen implements Screen 
{
    protected Game game;
    
    /**
     * @param game
     * Creates an instance of game
     */
    public AbstractGameScreen (Game game)
    {
        this.game = game;
    }
    
    public abstract void render (float deltaTime);
    public abstract void resize (int width, int height);
    public abstract void show();
    public abstract void hide();
    public abstract void pause();
    
    /**
     * Runs the resume method to resume the game
     */
    public void resume()
    {
        Assets.instance.init(new AssetManager());
    }
    
    /**
     * Runs the dispose method
     */
    public void dispose ()
    {
        Assets.instance.dispose();
    }

}

