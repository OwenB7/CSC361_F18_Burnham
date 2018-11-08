package com.packtpub.libgdx.ghostrunner.game.objects;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.packtpub.libgdx.ghostrunner.game.objects.AbstractGameObject;
import com.packtpub.libgdx.ghostrunner.util.Assets;

/** 
 * Code consists of graveyards that have their own layer.
 * A tint, color and offset can be specified for each layer.
 */


public class Graveyard extends AbstractGameObject
{
    private TextureRegion regGraveyardLeft;
    private TextureRegion regGraveyardRight;
    private int length;
    
    /**
     * instance of graveyard
     * @param length
     */
    public Graveyard(int length)
    {
      this.length = length;
      init();
    }
    
    /**
     * initializes graveyard
     */
    private void init()
    {
        dimension.set(10, 2);
        regGraveyardLeft = Assets.instance.levelDecoration.graveyardLeft;
        regGraveyardRight = Assets.instance.levelDecoration.graveyardRight;
        // shift graveyard and extend length
        origin.x = -dimension.x * 2;
        length += dimension.x * 2;
    }
    
    	/**
    	 * Draws the graveyard
    	 * @param batch
    	 * @param offsetX
    	 * @param offsetY
    	 * @param tintColor
    	 */
        private void drawGraveyard (SpriteBatch batch, float offsetX, float offsetY, float tintColor)
        {
            TextureRegion reg = null;
            batch.setColor(tintColor, tintColor, tintColor, 1);
            float xRel = dimension.x * offsetX;
            float yRel = dimension.y * offsetY;
            // graveyards span the whole level
            int graveyardLength = 0;
            graveyardLength += MathUtils.ceil(length / (2 * dimension.x));
            graveyardLength += MathUtils.ceil(0.5f + offsetX);
            for (int i = 0; i < graveyardLength; i++)
            {
                // graveyard left
                reg = regGraveyardLeft;
                batch.draw(reg.getTexture(), origin.x + xRel, position.y +
                origin.y + yRel, origin.x, origin.y, dimension.x, dimension.y,
                scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
                reg.getRegionWidth(), reg.getRegionHeight(), false, false);
                xRel += dimension.x;
                // graveyard right
                reg = regGraveyardRight;
                batch.draw(reg.getTexture(),origin.x + xRel, position.y +
                origin.y + yRel, origin.x, origin.y, dimension.x, dimension.y,
                scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
                reg.getRegionWidth(), reg.getRegionHeight(), false, false);
                xRel += dimension.x;
             }
        // reset color to white
        batch.setColor(1, 1, 1, 1);
        }
        
        @Override
        /**
         * renders the graveyard
         */
        public void render(SpriteBatch batch)
        {
        	// distant graveyard (dark gray)
        	drawGraveyard(batch, 0.5f, 0.5f, 0.5f);
        	// distant graveyard (gray)
        	drawGraveyard(batch, 0.25f, 0.25f, 0.7f);
        	// distant graveyard (light gray)
        	drawGraveyard(batch, 0.0f, 0.0f, 0.9f);
        }

        
}

