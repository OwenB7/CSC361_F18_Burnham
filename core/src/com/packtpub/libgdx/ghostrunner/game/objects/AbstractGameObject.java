package com.packtpub.libgdx.ghostrunner.game.objects;
 
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
/**
 * This class will hold all the common attributes
 * and fucntionalities that each of our game objects
 * inherit from
 */
public abstract class AbstractGameObject
{
    public Vector2 position;
    public Vector2 dimension;
    public Vector2 origin;
    public Vector2 scale;
    public float rotation;
    
    /**
     * Makes instances of all the variables for the
     * abstract game object 
     */
    public AbstractGameObject()
    {
        position = new Vector2();
        dimension = new Vector2(1, 1);
        origin = new Vector2();
        scale = new Vector2(1, 1);
        rotation = 0;
    }
    
    /**
     * @param deltaTime
     * updates objects accordingly in relevance
     * to the delta time
     */
    public void update(float deltaTime)
    {
        
    }
    
    /**
     * @param batch
     * objects are rendered accordingly
     */
    public abstract void render(SpriteBatch batch);
}
