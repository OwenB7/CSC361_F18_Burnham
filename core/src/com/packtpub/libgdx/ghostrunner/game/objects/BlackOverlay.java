package com.packtpub.libgdx.ghostrunner.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.libgdx.ghostrunner.game.*;
import com.packtpub.libgdx.ghostrunner.util.Assets;

/**
 * The black overlay class will consist of an image that
 * overlays the ground of the whole level
 */
public class BlackOverlay extends AbstractGameObject
{
    private TextureRegion regBlackOverlay;
    private float length;
    
    /**
     * @param length
     * Makes an instance of BlackOverlay with
     * it's length
     */
    public BlackOverlay (float length)
    {
        this.length = length;
        init();
    }
    
    /**
     * initializes the black overlay's dimensions
     */
    private void init()
    {
        dimension.set(length * 10, 3);
        
        regBlackOverlay = Assets.instance.levelDecoration.blackOverlay;
        
        origin.x = -dimension.x / 2;
    }
    
    @Override
    /**
     * @param batch
     * renders/draws the appropriate batch for the
     * black overlay
     */
    public void render (SpriteBatch batch)
    {
        TextureRegion reg = null;
        reg = regBlackOverlay;
        batch.draw(reg.getTexture(), position.x + origin.x, position.y + origin.y, origin.x,
                origin.y, dimension.x, dimension.y,  scale.x, scale.y, rotation, reg.getRegionX(),
                reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
    }
}
