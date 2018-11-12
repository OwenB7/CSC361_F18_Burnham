package com.packtpub.libgdx.ghostrunner.game.objects;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.libgdx.ghostrunner.util.Assets;

/**
 * Candy Corn object.  It is a collectible item, 
 * and as a result of being collected it will turn 
 * invisible and give the player score
 */
public class CandyCorn extends AbstractGameObject 
{
    private TextureRegion regCandyCorn;
    
    public boolean collected;
    
    /**
     * Creates instance of candy corn
     */
    public CandyCorn() 
    {
        init();
    }
    
    /**
     * Initializes candy corn object
     */
    private void init() 
    {
        dimension.set(0.5f, 0.5f);
        
        regCandyCorn = Assets.instance.candyCorn.candyCorn;
        
        // set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
        
        collected = false;
    }
    
    /**
     * Renders the candy corn
     */
    public void render (SpriteBatch batch) 
    {
        if (collected) return;
        
        TextureRegion reg = null;
        reg = regCandyCorn;
        
        batch.draw(reg.getTexture(), position.x, position.y,
                   origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
                   rotation, reg.getRegionX(), reg.getRegionY(),
                   reg.getRegionWidth(), reg.getRegionHeight(), false, false);
        
    }
    
    /**
     * returns the default score
     */
    public int getScore() 
    {
        return 100;
    }
    
}

