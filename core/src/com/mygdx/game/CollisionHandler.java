package com.mygdx.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.packtpub.libgdx.ghostrunner.util.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.packtpub.libgdx.ghostrunner.Level;
import com.packtpub.libgdx.ghostrunner.game.objects.AbstractGameObject;
import com.packtpub.libgdx.ghostrunner.game.objects.Rock;
import com.packtpub.libgdx.ghostrunner.game.objects.CandyCorn;
import com.packtpub.libgdx.ghostrunner.game.objects.Pumpkin;
import com.packtpub.libgdx.ghostrunner.game.objects.Boy;
import com.packtpub.libgdx.ghostrunner.game.objects.Boy.JUMP_STATE;
import com.packtpub.libgdx.ghostrunner.util.Constants;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.mygdx.game.WorldController;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;


/**
 * This class takes into account all of the collisions
 */
public class CollisionHandler implements ContactListener
{
	private WorldController controller;
	private ObjectMap<Short, ObjectMap<Short, ContactListener>> listeners;
	
	public CollisionHandler(WorldController w)
	{
		controller = w;
		listeners = new ObjectMap<Short, ObjectMap<Short, ContactListener>>();
	}
	/**
     * Determines which objects are colliding and calls the 
     * appropriate method
     * @param contact
     */
    private void processContact(Contact contact)
    {
    	Fixture fixtureA = contact.getFixtureA();
    	Fixture fixtureB = contact.getFixtureB();
    	AbstractGameObject objA = (AbstractGameObject) fixtureA.getBody().getUserData();
    	AbstractGameObject objB = (AbstractGameObject) fixtureB.getBody().getUserData();
    	if (((objA instanceof Boy) && (objB instanceof CandyCorn))  || ((objA instanceof CandyCorn) && (objB instanceof Boy)))
    	{
    		System.out.println("YOO");
    		processCandyCornContact(fixtureA, fixtureB);
    	}
    	
    	if (((objA instanceof Boy) && (objB instanceof Rock))  || ((objA instanceof Rock) && (objB instanceof Boy)))
    	{
    		processRockContact(fixtureA,fixtureB);
    	}
    	
    	if (((objA instanceof Boy) && (objB instanceof Pumpkin)) || ((objA instanceof Pumpkin) && (objB instanceof Boy)))
    	{
    		processPumpkinContact(fixtureA,fixtureB);
    	}
    	
    	//if ((objA instanceof Boy) && (objB instanceof Ghost))
    	//{
    	//	processGhostContact(fixtureA,fixtureB);
    	//}
    
    }
    
    /**
     * Handles collision between boy and candy corn
     * @param boyFixture
     * @param candyCornFixture
     */
    private void processCandyCornContact(Fixture boyFixture, Fixture candyCornFixture)
    {
    	Boy boy = (Boy) candyCornFixture.getBody().getUserData();
    	CandyCorn candyCorn = (CandyCorn) boyFixture.getBody().getUserData();
    	System.out.println("YO");
    	candyCorn.collected = true;
    	controller.score += candyCorn.getScore();
    	Gdx.app.log("CollisionHandler", "Piece of Candy Corn collected");
    }
    
    /**
     * Handles collision between boy and rock
     * @param boyFixture
     * @param rockFixture
     */
    private void processRockContact(Fixture boyFixture, Fixture rockFixture)
    {
    	Boy boy = (Boy) rockFixture.getBody().getUserData();
    	Rock rock = (Rock) boyFixture.getBody().getUserData();
    	boy = Level.boy;
    	float heightDifference = Math.abs(boy.position.y - (  rock.position.y + rock.bounds.height));
         
         if (heightDifference > 0.25f) 
         {
             boolean hitRightEdge = boy.position.x > (rock.position.x + rock.bounds.width / 2.0f);
             if (hitRightEdge) 
             {
                 boy.position.x = rock.position.x + rock.bounds.width;
             }
             else 
             {
                 boy.position.x = rock.position.x - boy.bounds.width;
             }
             return;
         }
         
         switch (boy.jumpState) 
         {
             case GROUNDED:
                 break;
             case FALLING:
             case JUMP_FALLING:
                 boy.position.y = rock.position.y + boy.bounds.height  + boy.origin.y;
                 boy.jumpState = JUMP_STATE.GROUNDED;
                 break;
             case JUMP_RISING:
                  boy.position.y = rock.position.y + boy.bounds.height + boy.origin.y;
                 break;
         }

    }
    
    /**
     * Handles collision between boy and pumpkin
     * @param boyFixture
     * @param candyCornFixture
     */
    private void processPumpkinContact(Fixture boyFixture, Fixture pumpkinFixture)
    {
    	Boy boy = (Boy) pumpkinFixture.getBody().getUserData();
    	Pumpkin pumpkin = (Pumpkin) boyFixture.getBody().getUserData();
    	pumpkin.collected = true;
    	controller.score += pumpkin.getScore();
    	controller.level.boy.setPumpkinPowerup(true);
    	Gdx.app.log("CollisionHandler", "Pumpkin collected");
    }

	@Override
	public void beginContact(Contact contact) 
	{
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		processContact(contact);

		Gdx.app.log("CollisionHandler-begin A", "begin");

		ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
		if (listener != null)
		{
		    listener.beginContact(contact);
	    }	
	}

	@Override
	public void endContact(Contact contact) 
	{
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();

		Gdx.app.log("CollisionHandler-end A", "end");

		 Gdx.app.log("CollisionHandler-end A", fixtureA.getBody().getLinearVelocity().x+" : "+fixtureA.getBody().getLinearVelocity().y);
		 Gdx.app.log("CollisionHandler-end B", fixtureB.getBody().getLinearVelocity().x+" : "+fixtureB.getBody().getLinearVelocity().y);
		ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
	    if (listener != null)
		{
		    listener.endContact(contact);
		}
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) 
	{
		//Gdx.app.log("CollisionHandler-preSolve A", "preSolve");
		//Fixture fixtureA = contact.getFixtureA();
		////Fixture fixtureB = contact.getFixtureB();
		//ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
		////if (listener != null)
		//{
		//    listener.preSolve(contact, oldManifold);
		//}
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) 
	{
		// TODO Auto-generated method stub
		this.processContact(contact);
	}
	
	private ContactListener getListener(short categoryA, short categoryB)
	{
		ObjectMap<Short, ContactListener> listenerCollection = listeners.get(categoryA);
		if (listenerCollection == null)
		{
		    return null;
		}
		return listenerCollection.get(categoryB);
	}
	
	

}
