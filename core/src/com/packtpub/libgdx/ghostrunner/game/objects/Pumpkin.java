package com.packtpub.libgdx.ghostrunner.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.libgdx.ghostrunner.util.Assets;

/** 
 * Pumpkin consists of only one image and is a collectible item that will turn invisible when
 * it is collected by the player's character
 */
public class Pumpkin extends AbstractGameObject
{
    private TextureRegion regPumpkin;
    public boolean collected;
    
    /**
     * This is pumpkin constructor
     */
    public Pumpkin()
    {
        init();
    }
    
	/**
	 * Initializes pumpkin and sets bounding boc for collision
	 */
	private void init ()
	{
	    dimension.set(0.5f, 0.5f);
	    regPumpkin = Assets.instance.pumpkin.pumpkin;
	    // Set bounding box for collision detection
	    bounds.set(0, 0, dimension.x, dimension.y);
	    collected = false;
	}
	
	/**
	 * This function draws the pumpkin
	 */
	public void render (SpriteBatch batch)
	{
	    if (collected) return;
	        TextureRegion reg = null;
	        reg = regPumpkin;
	        batch.draw(reg.getTexture(), position.x, position.y,
	                origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
	                rotation, reg.getRegionX(), reg.getRegionY(),
	                reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}
	
	/**
	 * Returns the score
	 */
	public int getScore()
	{
	    return 250;
	}

}

