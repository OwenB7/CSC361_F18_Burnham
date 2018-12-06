package com.packtpub.libgdx.ghostrunner;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.WorldController;
import com.mygdx.game.WorldRenderer;
import com.packtpub.libgdx.ghostrunner.util.Assets;
import com.packtpub.libgdx.ghostrunner.screens.MenuScreen;
import com.packtpub.libgdx.ghostrunner.util.AudioManager;
import com.packtpub.libgdx.ghostrunner.util.GamePreferences;

/**
 * This class has the major credentials for the game
 * in order for it to run and update as needed
 */
public class GhostRunnerMain extends Game
{
    
    /**
     * This creates and initializes the controller and renderer
     * Will also ensure the game world is active on start and that
     * the assets are loaded in
     * Additionally LibGDX is instructed through a call of the setScreen()
     * method by the Game class to change the current screen.
     * We pass a new instance of MenuScreen
     */
    @Override public void create ()
    {
        // Set Libgdx log level
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        // Load assets
        Assets.instance.init(new AssetManager());
        // Load preferences for audio settings and start playing music
        GamePreferences.instance.load();
        AudioManager.instance.play(Assets.instance.music.song01);
        // Start game at menu screen
        setScreen (new MenuScreen(this));
    }

}

