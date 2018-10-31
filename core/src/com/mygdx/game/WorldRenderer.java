package com.mygdx.game;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * The class the draws everything as its updated
 */
public class WorldRenderer implements Disposable
{
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private WorldController worldController;
    
    public WorldRenderer (WorldController worldController)
    {
        this.worldController = worldController;
        init();
    }
    private void init ()
    {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH,
        Constants.VIEWPORT_HEIGHT);
        camera.position.set(0, 0, 0);
        camera.update();
    }
    
    /**
     * Renders the test objects
     */
    public void render ()
    {
        renderTestObjects();
    }
    
    private void renderTestObjects()
    {
        worldController.cameraHelper.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(Sprite sprite : worldController.testSprites)
        {
        sprite.draw(batch);
        }
        batch.end();
    }
    
    /**
     * Resizes the objects
     * @param width
     * @param height
     */
    public void resize (int width, int height)
    {
        camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) *
                width;
                camera.update();
    }
    
    /**
     * disposes due to c code
     */
    @Override public void dispose ()
    {
        batch.dispose();
    }
    
    
}

