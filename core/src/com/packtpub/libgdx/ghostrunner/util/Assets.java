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
   // public AssetFonts fonts;
    
    // singleton: prevent instantiation from other classes
    private Assets() {}
    
    /**
     * Holds the default bitmap font in three 
     * differently configured sizes
     */
   /** public class AssetFonts
    {
        public final BitmapFont defaultSmall;
        public final BitmapFont defaultNormal;
        public final BitmapFont defaultBig;
        
        /**public AssetFonts()
        {
            // create three fonts using Libgdx's 15px bitmap font
            defaultSmall = new BitmapFont(
                    Gdx.files.internal("../core/assets/font/arial-15.fnt"), true);
            defaultNormal = new BitmapFont(
                    Gdx.files.internal("../core/assets/font/arial-15.fnt"), true);
            defaultBig = new BitmapFont(
                    Gdx.files.internal("../core/assets/font/arial-15.fnt"), true);
            
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
*/

    
    public void init (AssetManager assetManager) 
    {
        this.assetManager = assetManager;
        // set asset manager error handler
        assetManager.setErrorListener(this);
        // load texture atlas
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
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
       // fonts = new AssetFonts();
        boy = new AssetBoy(atlas);
        rock = new AssetRock(atlas);
        blackOverlay = new AssetBlackOverlay(atlas);
        candyCorn = new AssetCandyCorn(atlas);
        pumpkin = new AssetPumpkin(atlas);
        ghost = new AssetGhost(atlas);
        levelDecoration = new AssetLevelDecoration(atlas);
    }
    
    /**
     * disposes objects after they're done being used
     */
    @Override
    public void dispose() 
    {
        assetManager.dispose();
        //fonts.defaultSmall.dispose();
        //fonts.defaultNormal.dispose();
        //fonts.defaultBig.dispose();
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
       // public final AtlasRegion cloud03;
        public final AtlasRegion graveyardLeft;
        public final AtlasRegion graveyardRight;
        public final AtlasRegion blackOverlay;
        
        public AssetLevelDecoration (TextureAtlas atlas)
        {
            //cloud01 = atlas.findRegion("cloud01");
            //cloud02 = atlas.findRegion("cloud02");
            //cloud03 = atlas.findRegion("cloud03");
            graveyardLeft = atlas.findRegion("Graveyard");
            graveyardRight = atlas.findRegion("Graveyard");
            blackOverlay = atlas.findRegion("blackOverlay");
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
        public AssetCandyCorn (TextureAtlas atlas) 
        {
        	candyCorn = atlas.findRegion("CandyCorn");
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
    
}

