package net.r0adkll.example.flocking;

import java.util.ArrayList;
import java.util.List;

import net.r0adkll.flocking.FlockEngine;
import net.r0adkll.flocking.Flockable;
import net.r0adkll.flocking.rules.BoundRule;
import net.r0adkll.flocking.rules.HazardRule;
import net.r0adkll.flocking.rules.TargetRule;
import net.r0adkll.flocking.rules.WindRule;
import net.r0adkll.flocking.utils.FRectangle;
import net.r0adkll.flocking.utils.MathUtils;
import net.r0adkll.flocking.utils.Vector2D;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class FlockMain implements ApplicationListener , InputProcessor{
	
	/**
	 * Constants
	 */
	private static final float FRAMERATE = 1f/60f;
	private static final int BOID_AMOUNT = 1200;
	private static final int GRID_SIZE = 18;
	
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	
	/**
	 * Variables
	 */
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ShapeRenderer shapeBatch;
	private BitmapFont font;
	
	private FlockEngine engine;
	private BoundRule engineBound;
	private TargetRule touchTarget;
	private HazardRule touchHazard;
	private TouchTracker touch = new TouchTracker();
	
	private TextureRegion image;
	private List<Boid> boids;
	private float elapsed;
	
	@Override
	public void create() {			
		// Set this class as an input processor
		Gdx.input.setInputProcessor(this);
		
		// Initialize Variables and such
		boids = new ArrayList<Boid>();

		// Initialize the Camera and Sprite batch
		float w = WIDTH;
		float h = HEIGHT;
		camera = new OrthographicCamera(w, h);
		camera.translate(w/2f, h/2f);
		batch = new SpriteBatch();
		shapeBatch = new ShapeRenderer();
		font = new BitmapFont();
		
		// Load the Boid example image
		image = new TextureRegion(new Texture(Gdx.files.internal("data/block_sprite.png")), 8, 8);
		//image = new TextureRegion(new Texture(Gdx.files.internal("data/fire_alpha.png")), 256, 256);
		
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
		engine.setInteractionRadius(25);
		engine.setSeparationRadius(12);
		engine.setInnerSeperationRadius(7);
		
		// Set the boid speed limit
		engine.setSpeedLimit(80);	
	
		// Enable the Engine Grid Optimization
		engine.enableGrid(GRID_SIZE, new FRectangle(0, 0, w, h));
		
		// Initialize all the boids for the example
		System.out.println("Boids: " + BOID_AMOUNT);
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
		//debugDraw(Gdx.graphics.getDeltaTime());
		draw(Gdx.graphics.getDeltaTime());
	}
	
	/**
	 * This is the Update Routine
	 * - Handle all Logic & Update code here
	 * 
	 * @param delta
	 */
	public void update(float delta){
		
		// update the flocking engine
		engine.update(delta);
		
	}
	
	/**
	 * Render Routine
	 * - Render all the scenes content here
	 * 
	 * @param delta
	 */
	public void draw(float delta){
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE); 
		batch.begin();

		// Render all the Flocking Boids
		for(Boid s: boids){
			s.draw(batch);
		}
		
		// Draw the Average Frames Per Second
		font.draw(batch, Gdx.graphics.getFramesPerSecond() + " fps", 5, 20);
		
		batch.end();		
	}
	
	/**
	 * Debug Rendering
	 * @param delta
	 */
	public void debugDraw(float delta){
		shapeBatch.setProjectionMatrix(camera.combined);
		shapeBatch.setColor(Color.WHITE);
		shapeBatch.begin(ShapeType.Line);
		
		// Render Debug Lines
		int amtx = Gdx.graphics.getWidth() / GRID_SIZE;
		int amty = Gdx.graphics.getHeight() / GRID_SIZE;
		
		// Draw Grid Columns
		for(int x=0;x<amtx; x++){
			shapeBatch.line(x*GRID_SIZE, 0, x*GRID_SIZE, Gdx.graphics.getHeight());
		}
		
		// Draw Grid Rows
		for(int y=0; y<amty; y++){
			shapeBatch.line(0, y*GRID_SIZE, Gdx.graphics.getWidth(), y*GRID_SIZE);
		}
		
		shapeBatch.end();
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
	
	
	

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	Vector3 worldPos = new Vector3();
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		worldPos.set(screenX, screenY, 0);
		camera.unproject(worldPos);
		touch.position.set(worldPos.x, worldPos.y);
		
		if(button == Input.Buttons.LEFT){
			touchTarget = new TargetRule(touch, 150, 50);
			engine.addRule(touchTarget);	
		}else if(button == Input.Buttons.RIGHT){
			touchHazard = new HazardRule(touch, 150, 50);
			engine.addRule(touchHazard);
		}
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		// Remove Rule
		if(button == Input.Buttons.LEFT){
			engine.removeRule(touchTarget);
		}else if(button == Input.Buttons.RIGHT){
			engine.removeRule(touchHazard);
		}
		
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		worldPos.set(screenX, screenY, 0);
		camera.unproject(worldPos);
		touch.position.set(worldPos.x, worldPos.y);		
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	/**
	 * This class is used to track the input and input states
	 * to modify target/hazard rules withing the flocking engine
	 * 
	 * 
	 * @author r0adkll
	 *
	 */
	static class TouchTracker implements Flockable{
		public Vector2D position;
		
		/**
		 * Basic Constructor
		 */
		public TouchTracker(){
			position = new Vector2D();
		}
		
		/**
		 * This is the only 'Flockable' method delegate
		 * that we need to use for interfacing with a TargetRule
		 */
		@Override
		public Vector2D getPosition() {
			// TODO Auto-generated method stub
			return position;
		}
		
		/*******
		 * The rest of these methods are not needed for 'TargetRule'
		 */

		@Override
		public Vector2D getVelocity() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setRotation(float value) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public FRectangle getBounds() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
