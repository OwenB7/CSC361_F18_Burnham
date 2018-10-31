package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * This class assists us with managing and 
 * manipulating certain parameters of the camera
 * used to render the game world
 */
public class CameraHelper
{
    private static final String TAG = CameraHelper.class.getName();
    private final float MAX_ZOOM_IN = 0.25f;
    private final float MAX_ZOOM_OUT = 10.0f;
    private Vector2 position;
    private float zoom;
    
    private Sprite target;
    
    /**
     * instance of camera helper
     */
    public CameraHelper ()
    {
    	position = new Vector2();
    	zoom = 1.0f;
    }
    
    /**
     * updates position of camera in accordance
     * to time
     * @param deltaTime
     */
    public void update (float deltaTime)
    {
    	if (!hasTarget()) return;
    	position.x = target.getX() + target.getOriginX();
    	position.y = target.getY() + target.getOriginY();
    }
    
    /**
     * sets position of camera
     * @param x
     * @param y
     */
    public void setPosition (float x, float y)
    {
    	this.position.set(x, y);
    }
    
    /** 
     * @return position of camera
     */
    public Vector2 getPosition ()
    {
        return position;
    }
    
    /** 
     * adds zoom amount
     * @param amount
     */
    public void addZoom (float amount)
    {
        setZoom(zoom + amount);
    }
    
    /**
     * sets zoom amount
     * @param zoom
     */
    public void setZoom (float zoom)
    {
    	this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
    }
    
    /**
     * @return the zoom amount
     */
    public float getZoom ()
    {
        return zoom;
    }
    
    /**
     * sets the target
     * @param target
     */
    public void setTarget (Sprite target)
    {
        this.target = target;
    }
    
    /**
     * returns the target
     */
    public Sprite getTarget ()
    {
        return target;
    }
    
    /**
     * Does it have the target?
     * @return
     */
    public boolean hasTarget ()
    {
        return target != null;
    }
    
    public boolean hasTarget (Sprite target)
    {
    return hasTarget() && this.target.equals(target);
    }
    
    /** 
     * applies udpates to the orthographic camera
     * @param camera
     */
    public void applyTo (OrthographicCamera camera)
    {
        camera.position.x = position.x;
        camera.position.y = position.y;
        camera.zoom = zoom;
        camera.update();
    }
}
