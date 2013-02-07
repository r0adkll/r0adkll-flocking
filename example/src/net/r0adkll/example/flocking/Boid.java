package net.r0adkll.example.flocking;

import net.r0adkll.flocking.Flockable;
import net.r0adkll.flocking.utils.FRectangle;
import net.r0adkll.flocking.utils.Vector2D;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * This is an example of a Boid Flockable class that can 
 * be manipulated by the flocking engine. 
 * 
 * This can be structured many different ways, and the 
 * possibilities are almost endless, but for a flockable class
 * you almost always need a 
 * 
 *  - position vector
 *  - velocity vector
 *  - rectangle bounds
 *  - rotation (if using rotation)
 * 
 * These variables are directly accessed by the engine and are manipulated to
 * work, so you cant use 'new Vector2D(x, y)' to return the position or velocity
 * or bounds
 * 
 * @author r0adkll
 *
 */
public class Boid implements Flockable{

	/**
	 * STANDARD BOID VARIABLES
	 */
	TextureRegion image;
	Vector2D position;
	Vector2D velocity;
	FRectangle bounds;
	float rotation;
	
	/**
	 * Constructor
	 * @param image		the sprite to draw
	 * @param pos		the initial position of the sprite
	 */
	public Boid(TextureRegion image, Vector2D pos, Vector2D vel){
		this.image = image;
		this.position = pos;
		this.velocity = vel;
		this.bounds = new FRectangle(position.x, position.y, image.getRegionWidth(), image.getRegionHeight());
		System.out.println("New Boid[" + position + "][" + bounds + "]");
	}
	
	
	@Override
	public Vector2D getPosition() {
		return position;
	}

	@Override
	public Vector2D getVelocity() {
		return velocity;
	}

	@Override
	public FRectangle getBounds() {
		bounds.x = position.x;
		bounds.y = position.y;
		bounds.width = image.getRegionWidth();
		bounds.height = image.getRegionHeight();
		return bounds;
	}

	@Override
	public void setRotation(float value) {
		rotation = value;
	}
	
	
	/**
	 * Render the Boid
	 * @param batch		the sprite batch
	 */
	public void draw(SpriteBatch batch){
		batch.draw(image, position.x, position.y, bounds.width/2f, bounds.height/2f, bounds.width, bounds.height, 1, 1, rotation);
	}

}
