package net.r0adkll.example.flocking;

import java.util.ArrayList;
import java.util.List;

import net.r0adkll.flocking.FlockEngine;
import net.r0adkll.flocking.rules.BoundRule;
import net.r0adkll.flocking.utils.FRectangle;
import net.r0adkll.flocking.utils.MathUtils;
import net.r0adkll.flocking.utils.Vector2D;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FlockMain implements ApplicationListener {
	
	/**
	 * Constants
	 */
	private static final float FRAMERATE = 1f/60f;
	private static final int BOID_AMOUNT = 250;
	
	/**
	 * Variables
	 */
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private BitmapFont font;
	
	private FlockEngine engine;
	private BoundRule engineBound;
	
	private TextureRegion image;
	private List<Boid> boids;
	private float elapsed;
	
	@Override
	public void create() {		
		boids = new ArrayList<Boid>();
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		// Initialize the Camera and Sprite batch
		camera = new OrthographicCamera(w, h);
		camera.translate(w/2f, h/2f);
		batch = new SpriteBatch();
		font = new BitmapFont();
		
		// Load the Boid example image
		image = new TextureRegion(new Texture(Gdx.files.internal("data/block_sprite.png")), 8, 8);
		
		// Create Flocking Engine and initialize it with all
		// the basic parameters
		engine = new FlockEngine();
		
		/*
		 * Set the three Reynold flocking rules
		 */
		engine.setAlignment(0.80f);
		engine.setCohesion(0.65f);
		engine.setSeparation(0.60f);
		
		/*
		 * Set the boid interaction and seperation distances
		 */
		engine.setInteractionRadius(30);
		engine.setSeparationRadius(15);
		
		// Set the boid speed limit
		engine.setSpeedLimit(80);	
	
		
		// Initialize all the boids for the example
		for(int i=0; i<BOID_AMOUNT; i++){
			// Create New Sprite
			Boid bird = new Boid(image, new Vector2D(MathUtils.random(0, w), MathUtils.random(0, h)),
										new Vector2D(MathUtils.random(-20, 20), MathUtils.random(-20, 20)));
			
			// Add to the engine
			engine.addBoid(bird);	
			
			// Add to tracking list
			boids.add(bird);
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		image.getTexture().dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		// Rate Controll the update routine
		elapsed += Gdx.graphics.getDeltaTime();

		if(elapsed > FRAMERATE){
			// Update tween manager

			// Call the Update Routine
			update(FRAMERATE);

			// Update Elapsed
			elapsed -= FRAMERATE;			
		}

		// Call Render
		draw(Gdx.graphics.getDeltaTime());
	}
	
	public void update(float delta){
		
		// update the flocking engine
		engine.update(delta);
		
	}
	
	public void draw(float delta){
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		for(Boid s: boids){
			s.draw(batch);
		}
		
		font.draw(batch, Gdx.graphics.getFramesPerSecond() + " fps", 5, 20);
		
		batch.end();		
	}

	@Override
	public void resize(int width, int height) {
		if(engineBound!=null) engine.removeRule(engineBound);
		
		engineBound = new BoundRule(new FRectangle(0, 0, width, height), 150f);
		engine.addRule(engineBound);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
