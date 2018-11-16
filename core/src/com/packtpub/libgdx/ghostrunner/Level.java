package com.packtpub.libgdx.ghostrunner;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.ghostrunner.game.objects.AbstractGameObject;
//import com.packtpub.libgdx.ghostrunner.game.objects.Clouds;
import com.packtpub.libgdx.ghostrunner.game.objects.Graveyard;
import com.packtpub.libgdx.ghostrunner.game.objects.Rock;
import com.packtpub.libgdx.ghostrunner.game.objects.BlackOverlay;
import com.packtpub.libgdx.ghostrunner.game.objects.Boy;
import com.packtpub.libgdx.ghostrunner.game.objects.Pumpkin;
import com.packtpub.libgdx.ghostrunner.game.objects.CandyCorn;


/** 
 * This class is the loader that will read and interpret the image data
 * All game objects are filled in level class
 */
public class Level
{
    public static final String TAG = Level.class.getName();
    public static Boy boy;
    public Array<CandyCorn> candycorn;
    public Array<Pumpkin> pumpkins;
    
    public enum BLOCK_TYPE
    {
        EMPTY(0, 0, 0), // black
        ROCK(0, 255, 0), // green
        PLAYER_SPAWNPOINT(255, 255, 255), // white
        ITEM_PUMPKIN(255, 0, 255), // purple
        ITEM_CANDY_CORN(255, 255, 0); // yellow
        private int color;
        
        /**
         * limitations for block type
         * @param r
         * @param g
         * @param b
         */
        private BLOCK_TYPE (int r, int g, int b)
        {
            color = r << 24 | g << 16 | b << 8 | 0xff;
        }
        
        /**
         * checks to see if same color
         * @param color
         * @return
         */
        public boolean sameColor (int color)
        {
            return this.color == color;
        }
        
        /**
         * returns the color
         * @return
         */
        public int getColor ()
        {
            return color;
        }
    }
        // objects
        public Array<Rock> rocks;
        
        // decoration
        //public Clouds clouds;
        public Graveyard graveyard;
        public BlackOverlay blackOverlay;
        
        /**
         * creates instance of level
         * @param filename
         */
        public Level (String filename)
        {
            init(filename);
        }
        
        /**
         * initializes the level
         * @param filename
         */
        private void init (String filename)
        {
        	// player character
        	boy = null;
            // objects
            rocks = new Array<Rock>();
            candycorn = new Array<CandyCorn>();
            pumpkins = new Array<Pumpkin>();
            
            // load image file that represents the level data
            Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
            
            // scan pixels from top-left to bottom-right
            int lastPixel = -1;
            for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++)
            {
                for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++)
                {
                AbstractGameObject obj = null;
                float offsetHeight = 0;
                // height grows from bottom to top
                float baseHeight = pixmap.getHeight() - pixelY;
                // get color of current pixel as 32-bit RGBA value
                int currentPixel = pixmap.getPixel(pixelX, pixelY);
                // find matching color value to identify block type at (x,y)
                // point and create the corresponding game object if there is
                // a match
                // empty space
                if (BLOCK_TYPE.EMPTY.sameColor(currentPixel))
                {
                    // do nothing
                }
            // rock
            else if (BLOCK_TYPE.ROCK.sameColor(currentPixel))
            {
                if (lastPixel != currentPixel)
                {
                obj = new Rock();
                float heightIncreaseFactor = 0.25f;
                offsetHeight = -3.0f;
                obj.position.set(pixelX, baseHeight * obj.dimension.y
                * heightIncreaseFactor + offsetHeight);
                rocks.add((Rock)obj);
                }
                else
                {
                    rocks.get(rocks.size - 1).increaseLength(1);
                }
            }
            // player spawn point
            else if
            (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel))
            {
            	obj = new Boy();
                offsetHeight = -3.0f;
                obj.position.set(pixelX,baseHeight * obj.dimension.y +
                        offsetHeight);
                        boy = (Boy)obj;
            }
            // pumpkin
            else if (BLOCK_TYPE.ITEM_PUMPKIN.sameColor(currentPixel))
            {
            	obj = new Pumpkin();
                offsetHeight = -1.5f;
                obj.position.set(pixelX,baseHeight * obj.dimension.y
                + offsetHeight);
                pumpkins.add((Pumpkin)obj);

            }
            // candy corn
            else if (BLOCK_TYPE.ITEM_CANDY_CORN.sameColor(currentPixel))
            {
            	 obj = new CandyCorn();
                 offsetHeight = -1.5f;
                 obj.position.set(pixelX,baseHeight * obj.dimension.y
                 + offsetHeight);
                 candycorn.add((CandyCorn)obj); 
            }
            // unknown object/pixel color
            else {
                int r = 0xff & (currentPixel >>> 24); //red color channel
                int g = 0xff & (currentPixel >>> 16); //green color channel
                int b = 0xff & (currentPixel >>> 8); //blue color channel
                int a = 0xff & currentPixel; //alpha channel
                Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<"
                + pixelY + ">: r<" + r+ "> g<" + g + "> b<" + b + "> a<" + a + ">");
            }
            lastPixel = currentPixel;
        }
    }
            // decoration
            //clouds = new Clouds(pixmap.getWidth());
            //clouds.position.set(0, 2);
            graveyard = new Graveyard(pixmap.getWidth());
            graveyard.position.set(-1, -1);
            blackOverlay = new BlackOverlay(pixmap.getWidth());
            blackOverlay.position.set(0, -3.75f);
            // free memory
            pixmap.dispose();
            Gdx.app.debug(TAG, "level '" + filename + "' loaded");
        }
        
        /**
         * Render matches lastPixel with currentPixel and 
         * used to detect rock pixels/colors.
         * @param batch
         */
        public void render(SpriteBatch batch)
        {
            // Draw Graveyards
            graveyard.render(batch);
            
            // Draw Rocks
            for (Rock rock : rocks)
                rock.render(batch);
            
         // Draw Candy Corn
            for (CandyCorn goldCoin : candycorn)
            goldCoin.render(batch);
            
            // Draw Pumpkins
            for (Pumpkin feather : pumpkins)
            feather.render(batch);
            
            // Draw Player Character
            boy.render(batch);

            
            // Draw Black Overlay
            blackOverlay.render(batch);
            
            // Draw Clouds
            //clouds.render(batch);
        }
        
        /**
         * Collectively updates all the game world
         * objects in a level in one call
         * @param deltaTime
         */
        public void update (float deltaTime)
        {
            boy.update(deltaTime);
            for(Rock rock : rocks)
            	rock.update(deltaTime);
            for(CandyCorn candyCorn : candycorn)
            	candyCorn.update(deltaTime);
            for(Pumpkin pumpkin : pumpkins)
                pumpkin.update(deltaTime);
        }
            

            
            
}

