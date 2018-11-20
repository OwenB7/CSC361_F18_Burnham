package com.packtpub.libgdx.ghostrunner.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.packtpub.libgdx.ghostrunner.game.objects.*;
import com.packtpub.libgdx.ghostrunner.util.Assets;
import com.packtpub.libgdx.ghostrunner.util.Constants;
import com.packtpub.libgdx.ghostrunner.Level;
/**
 * This class is for the boy object.
 * It is the player's character and consists of only
 * one image, but has to account for jumping, falling,
 * and picking up objects
 */
public class Boy extends AbstractGameObject
{
    public static final String TAG = Boy.class.getName();
    
    private final float JUMP_TIME_MAX = 0.3f;
    private final float JUMP_TIME_MIN = 0.1f;
    private final float JUMP_TIME_OFFSET_FLYING =
            JUMP_TIME_MAX - 0.018f;
    
    // different directions views
    public enum VIEW_DIRECTION { LEFT, RIGHT }
    
    // different jump states
    public enum JUMP_STATE
    {
        GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING
    }
    
    private TextureRegion regBoy;
    
    public VIEW_DIRECTION viewDirection;
    public float timeJumping;
    public JUMP_STATE jumpState;
    public boolean hasPumpkinPowerup;
    public float timeLeftPumpkinPowerup;
    
    /**
     * initializes the boy object
     */
    public Boy()
    {
        init();
    }
    
    /**
     * Initializes the boy game object by setting
     * its physics values, a starting view direction, and
     * jump state.  Also deactivates the feather power-up
     * effect.
     */
    public void init ()
    {
        dimension.set(1, 1);
        regBoy = Assets.instance.boy.boy;
        // Center image on game object
        origin.set(dimension.x / 2, dimension.y / 2);
        // Bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
        // Set physics values
        terminalVelocity.set(3.0f, 4.0f);
        friction.set(12.0f, 0.0f);
        acceleration.set(0.0f, -25.0f);
        // View direction
        viewDirection = VIEW_DIRECTION.RIGHT;
        // Jump state
        jumpState = JUMP_STATE.FALLING;
        timeJumping = 0;
        // Power-ups
        hasPumpkinPowerup = false;
        timeLeftPumpkinPowerup = 0;
    }
    
    /**
     * Allows us to make the boy jump.
     * The state handling in the code will decide whether
     * jumping is currently possible and whether it is a
     * single or a multi jump.
     * @param jumpKeyPressed
     */
    public void setJumping (boolean jumpKeyPressed)
    {
        
        switch (jumpState)    
        {
        case GROUNDED: // Character is standing on a platform
        
            if (jumpKeyPressed)
            {
                // Start counting jump time from the beginning
                timeJumping = 0;
                jumpState = JUMP_STATE.JUMP_RISING;
                body.setLinearVelocity(new Vector2(0, 7));
            }
            break;
        case JUMP_RISING: // Rising in the air
            if (!jumpKeyPressed)
                jumpState = JUMP_STATE.JUMP_FALLING;
            break;
        case FALLING: // Falling down
        case JUMP_FALLING: // Falling down after jump
            if (jumpKeyPressed && hasPumpkinPowerup)
            {
                timeJumping = JUMP_TIME_OFFSET_FLYING;
                jumpState = JUMP_STATE.JUMP_RISING;
            }
            break;
        }
    }
    
    /**
     * Allows us to toggle the pumpkin power-up effect
     * @param pickedUp
     */
    public void setPumpkinPowerup (boolean pickedUp)
    {
        hasPumpkinPowerup = pickedUp;
        if (pickedUp)
        {
            timeLeftPumpkinPowerup =
                    Constants.ITEM_PUMPKIN_POWERUP_DURATION;
        }
    }
    
    /**
     * Finds out whether the power-up is still active
     * @return if power-up is still still active
     */
    public boolean hasPumpkinPowerup ()
    {
        return hasPumpkinPowerup && timeLeftPumpkinPowerup > 0;
    }
    
    @Override
    /**
     * Handles the switching of the viewing direction according
     * to the current move direction.  The time remaining of the
     * power-up effect is also checked.  If the time is up, the
     * feather power-up effect is disabled.
     */
    public void update (float deltaTime)
    {
        super.update(deltaTime);
        if (velocity.x != 0)
        {
            viewDirection = velocity.x < 0 ? VIEW_DIRECTION.LEFT :
                VIEW_DIRECTION.RIGHT;
        }
        if (timeLeftPumpkinPowerup > 0)
        {
            timeLeftPumpkinPowerup -= deltaTime;
            if (timeLeftPumpkinPowerup < 0)
            {
                // disable power-up
                timeLeftPumpkinPowerup = 0;
                setPumpkinPowerup(false);
            }
        }
    }
    
    @Override
    /**
     * Handles the calculations and switching of states that
     * is needed to enable jumping and falling
     */
    protected void updateMotionY (float deltaTime)
    {
        System.out.println("TESTING");
        switch (jumpState)
        {
        case GROUNDED:
            jumpState = JUMP_STATE.FALLING;
            break;
        case JUMP_RISING:
            // Keep track of jump time
            timeJumping += deltaTime;
            // Jump time left?
            if (timeJumping <= JUMP_TIME_MAX)
            {
                // Still jumping
                //velocity.y = terminalVelocity.y;
            	System.out.println("Jumping");
            	body.setLinearVelocity(new Vector2(0, 50));
            }
            break;
        case FALLING:
            break;
        case JUMP_FALLING:
            // Add delta times to track jump time
            timeJumping += deltaTime;
            // Jump to minimal height if jump key was pressed to short
            if (timeJumping > 0 && timeJumping <= JUMP_TIME_MIN)
            {
                // Still Jumping
                velocity.y = terminalVelocity.y;
            }
        }
        if (jumpState != JUMP_STATE.GROUNDED)
            super.updateMotionY(deltaTime);
    }
    
    @Override
    /**
     * Handles the drawing of the image for the bunny head
     * game object.  Image will be tinted orange if the feather
     * power-up effect is active.
     */
    public void render (SpriteBatch batch)
    {
        TextureRegion reg = null;
        
        // Set special color when game object has a feather power-up
        if (hasPumpkinPowerup)
        {
            batch.setColor(1.0f, 0.8f, 0.0f, 1.0f);
        }
        
        // Draw image
        reg = regBoy;
        batch.draw(reg.getTexture(), position.x, position.y, origin.x,
                origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
                reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
                reg.getRegionHeight(), viewDirection == VIEW_DIRECTION.LEFT,
                false);
        
        // Reset color to white
        batch.setColor(1, 1, 1, 1);
    }
}

