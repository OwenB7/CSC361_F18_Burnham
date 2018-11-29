package com.packtpub.libgdx.ghostrunner.util;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

/**
 * Abstracts the process of loading and
 * saving all of our game settings 
 */
public class GamePreferences
{

    public static final String TAG = GamePreferences.class.getName();
    
    public static final GamePreferences instance = new GamePreferences();
    public boolean sound;
    public boolean music;
    public float volSound;
    public float volMusic;
    public int charSkin;
    public boolean showFpsCounter;
    private Preferences prefs;
    
    // singleton: prevent instantiation from other classes
    private GamePreferences()
    {
        prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
    }
    
    
    /** 
     * Loads the music and sound into
    * main screen
    */
    public void load()
    {
        sound = prefs.getBoolean("sound", true);
        music = prefs.getBoolean("music", true);
        volSound = MathUtils.clamp(prefs.getFloat("volSound", 0.5f),
        0.0f, 1.0f);
        volMusic = MathUtils.clamp(prefs.getFloat("volMusic", 0.5f),
                0.0f, 1.0f);
                charSkin = MathUtils.clamp(prefs.getInteger("charSkin", 0),
                0, 2);
                showFpsCounter = prefs.getBoolean("showFpsCounter", false);
                
    }
    

    /** 
    *   Takes the current values of its
    *   This takes the current values of the public variables and puts them into the map of the preferences file. Finally, flush()
    *   is called on the preferences file to actually write the changed values into the file.
    */
    public void save()
    {
        prefs.putBoolean("sound", sound);
        prefs.putBoolean("music", music);
        prefs.putFloat("volSound", volSound);
        prefs.putFloat("volMusic", volMusic);
        prefs.putInteger("charSkin", charSkin);
        prefs.putBoolean("showFpsCounter", showFpsCounter);
        prefs.flush();
        
    }
    
}

