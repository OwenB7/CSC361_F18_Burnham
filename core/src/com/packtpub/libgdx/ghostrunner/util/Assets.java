package com.packtpub.libgdx.ghostrunner.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.packtpub.libgdx.ghostrunner.util.Constants;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;

/**
 * Class that structures and organizes the assets
 */
public class Assets implements Disposable, AssetErrorListener 
{
    
    public static final String TAG = Assets.class.getName();
    
    public static final Assets instance = new Assets();
    
    private AssetManager assetManager;
    
    public AssetBoy boy;
    public AssetRock rock;
    public AssetBlackOverlay blackOverlay;
    public AssetCandyCorn candyCorn;
    public AssetPumpkin pumpkin;
    public AssetGhost ghost;
    public AssetLevelDecoration levelDecoration;
    public AssetFonts fonts;
    public AssetSounds sounds;
    public AssetMusic music;
    
    // singleton: prevent instantiation from other classes
    private Assets() {}
    
    /**
     * Holds the default bitmap font in three 
     * differently configured sizes
     */
    public class AssetFonts
    {
        public final BitmapFont defaultSmall;
        public final BitmapFont defaultNormal;
        public final BitmapFont defaultBig;
        
        public AssetFonts()
        {
            // create three fonts using Libgdx's 15px bitmap font
            defaultSmall = new BitmapFont(
                    Gdx.files.internal("../core/assets/images/arial-15.fnt"), true);
            defaultNormal = new BitmapFont(
                    Gdx.files.internal("../core/assets/images/arial-15.fnt"), true);
            defaultBig = new BitmapFont(
                    Gdx.files.internal("../core/assets/images/arial-15.fnt"), true);
            
            // set font sizes
            defaultSmall.getData().setScale(0.75f);
            defaultNormal.getData().setScale(1.0f);
            defaultBig.getData().setScale(2.0f);
            
            // enable linear texture filtering for smooth fonts
            defaultSmall.getRegion().getTexture().setFilter(
                    TextureFilter.Linear, TextureFilter.Linear);
            defaultNormal.getRegion().getTexture().setFilter(
                    TextureFilter.Linear, TextureFilter.Linear);
            defaultBig.getRegion().getTexture().setFilter(
                    TextureFilter.Linear, TextureFilter.Linear);
        }
    }


    /**
     * initializes the asset manager
     * @param assetManager
     */
    public void init (AssetManager assetManager) 
    {
        this.assetManager = assetManager;
        // set asset manager error handler
        assetManager.setErrorListener(this);
        // load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
        // load sounds
        assetManager.load("sounds/jump.wav", Sound.class);
     	assetManager.load("sounds/jump_with_feather.wav", Sound.class);
     	assetManager.load("sounds/pickup_coin.wav", Sound.class);
     	assetManager.load("sounds/pickup_feather.wav", Sound.class);
     	assetManager.load("sounds/live_lost.wav", Sound.class);
     		// load music
        assetManager.load("music/song.mp3", Music.class);
        // start loading assets and wait until finished
        assetManager.finishLoading();
        Gdx.app.debug(TAG, "# of assets loaded: " +
         assetManager.getAssetNames().size);
        
        for (String a : assetManager.getAssetNames())
            Gdx.app.debug(TAG, "asset: " + a);
        
        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
        
        // enable texture filtering for pixel smoothing
        for (Texture t: atlas.getTextures()) 
        {
            t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }
        
        // create game resource objects
        fonts = new AssetFonts();
        boy = new AssetBoy(atlas);
        rock = new AssetRock(atlas);
        blackOverlay = new AssetBlackOverlay(atlas);
        candyCorn = new AssetCandyCorn(atlas);
        pumpkin = new AssetPumpkin(atlas);
        ghost = new AssetGhost(atlas);
        levelDecoration = new AssetLevelDecoration(atlas);
        sounds = new AssetSounds(assetManager);
        music = new AssetMusic(assetManager);
    }
    
    /**
     * disposes objects after they're done being used
     */
    @Override
    public void dispose() 
    {
        assetManager.dispose();
        fonts.defaultSmall.dispose();
        fonts.defaultNormal.dispose();
        fonts.defaultBig.dispose();
    }
    
    /**
     * Handles an error
     * @param filename
     * @param type
     * @param throwable
     */
    public void error (String filename, Class type, Throwable throwable) 
    {
        Gdx.app.error(TAG, "Couldn't load asset: '" + filename + "'", (Exception)throwable);
    }
    
    /**
     * handles an error
     */
    @Override
    public void error (AssetDescriptor asset, Throwable throwable) 
    {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception)throwable);
    }
    
    public class AssetLevelDecoration
    {
        //public final AtlasRegion cloud01;
       // public final AtlasRegion cloud02;
        //public final AtlasRegion cloud03;
        public final AtlasRegion graveyardLeft;
        public final AtlasRegion graveyardRight;
        public final AtlasRegion blackOverlay;
        
        public AssetLevelDecoration (TextureAtlas atlas)
        {
            //cloud01 = atlas.findRegion("cloud01");
            //cloud02 = atlas.findRegion("cloud02");
            //cloud03 = atlas.findRegion("cloud03");
            graveyardLeft = atlas.findRegion("Graveyard2");
            graveyardRight = atlas.findRegion("Graveyard2");
            blackOverlay = atlas.findRegion("BlackOverlay");
        }
        
    }
    
    /**
     * Class that loads the boy
     */
    public class AssetBoy
    {
        public final AtlasRegion boy;
        
        public AssetBoy (TextureAtlas atlas) 
        {
            boy = atlas.findRegion("RunningBoy");
        }
    }
    
    /**
     * Class that loads the rock edge image and rock middle image
     */
    public class AssetRock 
    {
        public final AtlasRegion edge;
        public final AtlasRegion middle;
        public AssetRock (TextureAtlas atlas) 
        {
            edge = atlas.findRegion("PlatformEdge");
            middle = atlas.findRegion("PlatformMiddle");
        }
    }
    
    /**
     * Class that loads in the black overlay image
     */
    public class AssetBlackOverlay
    {
    	public final AtlasRegion blackOverlay;
    	public AssetBlackOverlay (TextureAtlas atlas)
    	{
    		blackOverlay = atlas.findRegion("BlackOverlay");
    	}
    }
    
    /**
     * Class that loads the candy corn image
     */
    public class AssetCandyCorn 
    {
        public final AtlasRegion candyCorn;
        public final Animation animCandyCorn; 
        public AssetCandyCorn (TextureAtlas atlas) 
        {
        	candyCorn = atlas.findRegion("CandyCorn");
        	
        	// Animation: Gold Coin
        	Array <AtlasRegion> regions = atlas.findRegions("anim_candy_corn");
        	AtlasRegion region = regions.first();
        				
        	for (int i = 0; i < 10; i++) 
        	{
        		regions.insert(0,  region);
        	}
        				
        	animCandyCorn = new Animation (1.0f / 20.0f, regions, Animation.PlayMode.LOOP_RANDOM);
        }
    }
    
    /**
     * Class that loads the Pumpkin image
     */
    public class AssetPumpkin 
    {
        public final AtlasRegion pumpkin;
        public AssetPumpkin (TextureAtlas atlas) 
        {
        	pumpkin = atlas.findRegion("Pumpkin");
        }
    }
    
    /**
     * Class that loads the Ghost image
     */
    public class AssetGhost 
    {
        public final AtlasRegion ghost;
        public AssetGhost (TextureAtlas atlas) 
        {
        	ghost = atlas.findRegion("Ghost");
        }
    }
    
    /**
	 * Inner class that holds the loaded sound instances
	 */
	public class AssetSounds
	{
		public final Sound jump;
		public final Sound jumpWithPumpkin;
		public final Sound pickupCandyCorn;
		public final Sound pickupPumpkin;
		public final Sound liveLost;
		
		public AssetSounds (AssetManager am)
		{
			jump = am.get("sounds/jump.wav", Sound.class);
			jumpWithPumpkin = am.get("sounds/jump_with_feather.wav", Sound.class);
			pickupCandyCorn = am.get("sounds/pickup_coin.wav", Sound.class);
			pickupPumpkin = am.get("sounds/pickup_feather.wav", Sound.class);
			liveLost = am.get("sounds/live_lost.wav", Sound.class);
		}
	}
	
	/**
	 * Inner class that holds the loaded music instance
	 */
	public class AssetMusic
	{
		public final Music song01;
		
		public AssetMusic (AssetManager am)
		{
			song01 = am.get("music/song.mp3", Music.class);
		}
	}


    
}

