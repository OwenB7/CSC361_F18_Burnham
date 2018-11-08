
package com.packtpub.libgdx.ghostrunner.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.packtpub.libgdx.ghostrunner.*;
import com.packtpub.libgdx.ghostrunner.util.Assets;

/**
 * Rock consists of left edge, middle part, and a right edge
 */
public class Rock extends AbstractGameObject 
{
    
    private TextureRegion regEdge;
    private TextureRegion regMiddle;
    
    private int length;
    
    /**
     * initializes rock
     */
    public Rock() 
    {
        init();
    }
    
    /**
     * initializes objects
     */
    private void init() 
    {
        dimension.set(1, 1.9f);
        
        regEdge = Assets.instance.rock.edge;
        regMiddle = Assets.instance.rock.middle;
        
        // Start length of this rock
        setLength(1);
    }
    
    /**
     * sets length
     */
    public void setLength(int length) 
    {
        this.length = length;
    }
    
    /**
     * increases length
     */
    public void increaseLength(int amount) 
    {
        setLength(length + amount);
    }
    
    /**
     * method inherited from AbstractGameObject
     */
    @Override
    public void render(SpriteBatch batch) 
    {
        TextureRegion reg = null;
        
        float relX = 0;
        float relY = 0;
        
        // Draw left edge
        reg = regEdge;
        relX -= dimension.x / 4;
        batch.draw(reg.getTexture(), position.x + relX, position.y +
                   relY, origin.x, origin.y, dimension.x / 4, dimension.y,
                   scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
                   reg.getRegionWidth(), reg.getRegionHeight(), false, false);
        
        // Draw middle
        relX = 0;
        reg = regMiddle;
        for (int i = 0; i < length; i++) 
        {
             batch.draw(reg.getTexture(), position.x + relX, position.y
                       +  relY, origin.x, origin.y, dimension.x, dimension.y,
                       scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
                       reg.getRegionWidth(), reg.getRegionHeight(), false, false);
             relX += dimension.x;
        }
        
        // Draw right edge
       reg = regEdge;
       batch.draw(reg.getTexture(),position.x + relX, position.y +
                   relY, origin.x + dimension.x / 8, origin.y, dimension.x / 4,
                   dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
                   reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),
                   true, false);
    
    }
    
    
}

