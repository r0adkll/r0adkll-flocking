package net.r0adkll.flocking;

import java.util.ArrayList;
import java.util.List;

import net.r0adkll.flocking.rules.AbstractRule;
import net.r0adkll.flocking.rules.BoundRule;
import net.r0adkll.flocking.utils.FRectangle;
import net.r0adkll.flocking.utils.MathUtils;
import net.r0adkll.flocking.utils.Vector2D;


/**
 * From here the flocking algorithm is orchistrated upon
 * the passed entities into this controller
 * 
 *  This Flocking Engine uses Reynold's Three Rules
 *  	1) Cohesion		- the propensity for boids to stay together
 *  	2) Alignment	- the propensity for boids to fly in the same direction
 *  	3) Seperation	- the propensity for boids to avoid each other
 * 
 * @author r0adkll
 *
 */
public class FlockEngine{
	
	/**************************************************************************
	 * Constants
	 */
	
	/* Reynold's Rule Constants */
	// Cohesion: tendency to move towards the surrounding birds' average position
	private static final float cohesionMin = -0.1f;
	private static final float cohesionMax = 5f;

	// Alignment: tendency to move towards the surrounding birds' average velocity
	private static final float alignmentMin = -0.01f;
	private static final float alignmentMax = 0.05f;

	// Separation: tendency to move away from the surrounding birds' average position
	private static final float separationMin = 0.25f;
	private static final float separationMax = 7f; 
	
	
	

	/**************************************************************************
	 * Variables
	 */
	
	// Debug Flag
	private boolean DEBUG = true;
	
	// the algorithm control variables
	float radius = 20f;
	float sepRadius = 12f;
	float innerSepRadius = 7;
	float speedLimit = 80;	
	
	// Separation distance variables
	private float separationDistMinSq = innerSepRadius * innerSepRadius;
	private float separationDistMaxSq = sepRadius * sepRadius;
	
	// The Rule Factors
	float cohesion = cohesionMax;
	float alignment = alignmentMax;
	float separation = separationMax;
	float separationDistSq = separationDistMaxSq;
		
	// Grid Optimization Flag
	boolean isGridEnabled = false;
	
	/*
	 * The Grid Optimization object to improve the performance
	 * of the algorithm
	 */
	FlockGrid _boidgrid;
	
	/*
	 * The list of currently tracked birds being controlled
	 * by the engine
	 */
	List<Flockable> _boidflock;	
	
	/*
	 * The list of flocking rules to be applied by
	 * the algorithm
	 */
	List<AbstractRule> _rules;
	
	
	/**
	 * Constructor
	 */
	public FlockEngine(){
		_boidflock = new ArrayList<Flockable>();
		_rules = new ArrayList<AbstractRule>();
	}
	
	/**
	 * Enable the Grid optimization
	 * @param gridSize	the width&height of each grid space
	 */
	public void enableGrid(int gridSize, FRectangle bounds){
		_boidgrid = new FlockGrid(gridSize, (int)bounds.getWidth(), (int)bounds.getHeight());
		isGridEnabled = true;
		
		for(AbstractRule rule: _rules){
			if(rule instanceof BoundRule){
				rule = new BoundRule(bounds, 75);
				return;
			}
		}
		
		BoundRule brule = new BoundRule(bounds, 75);
		addRule(brule);
	}
	
	
	/**************************************************************************
	 * Main Methods and Functions
	 */
	
	
	/**
	 * Update the Flock Controller
	 * @param elapsed	the frame elapsed time
	 */
	private Vector2D ruleResult = null;
	public void update(float elapsed){
		for(int i=0; i<_boidflock.size(); i++){
			Flockable boid = _boidflock.get(i);
			
			// Apply Reynold's Rules
			Vector2D acceleration = applyReynoldRules(boid);
			
			// Apply all the user set rules
			for(int r=0; r<_rules.size(); r++){
				ruleResult = _rules.get(r).applyRule(boid);
				if(ruleResult != null){
					acceleration.add(ruleResult);
				}
			}
						
			// Apply Acceleration to the Boid velocity
			boid.getVelocity().add(acceleration);
									
			// Limit Boid Velocity to speedlimit
			limitSpeed(boid);			
			
			// Finally, add the boid velocity to its position after multiplying it by 
			// the time elapsed between frames
			boid.getPosition().add(Vector2D.mult(boid.getVelocity(), elapsed));
			
			// Update Rect
			boid.getBounds().setX(boid.getPosition().x);
			boid.getBounds().setY(boid.getPosition().y);
			
			// Update Grid
			if(isGridEnabled) _boidgrid.update(boid);
		}
	}
	
	
	/**************************************************************************
	 * Settings Methods
	 * - These methods apply and/or set flags in the flocking engine
	 *   that can enable certain rules (such as the Wind Rule) and 
	 *   other optimizations.
	 *   
	 * - These also contain the methods to set the degree of all the 3 Reynold's 
	 * 	 rules
	 * 
	 */
	
	
	/**
	 * Set the Cohesion Factor of the algorithm
	 * 
	 * @param percent	the level of cohesion 0.0 to 1.0
	 */
	public void setCohesion(float percent){
		percent = MathUtils.clamp(percent, 0.0f, 1.0f);
		float coh = (cohesionMax - cohesionMin) * percent;
		cohesion = (cohesionMin + coh);
	}
	
	/**
	 * Set the Alignment Factor of the algorithm
	 * 
	 * @param percent	the level of alignment 0.0 to 1.0
	 */
	public void setAlignment(float percent){
		percent = MathUtils.clamp(percent, 0.0f, 1.0f);
		float ali = (alignmentMax - alignmentMin) * percent;
		alignment = (alignmentMin + ali);
	}
	
	/**
	 * Set the Separation Factor of the algorithm
	 * 
	 * @param percent	the level of separation 0.0 to 1.0
	 */
	public void setSeparation(float percent){
		percent = MathUtils.clamp(percent, 0.0f, 1.0f);
		float sep = (separationMax - separationMin) * percent;
		separation = (separationMin + sep);
	}
	
	/**
	 * Set the radius at which the boids start interacting with each other
	 * 
	 * @param radii		the radius of interaction
	 */
	public void setInteractionRadius(float radii){
		radius = radii;
	}
	
	public void setInnerSeperationRadius(float radii){
		innerSepRadius = radii;
		separationDistMinSq = innerSepRadius * innerSepRadius;
	}
	
	/**
	 * Set the radius at which the boids start separating from each other
	 * 
	 * @param radii		the radius of separation
	 */
	public void setSeparationRadius(float outer){
		sepRadius = outer;										// Set the Outer Radius
		separationDistMaxSq = sepRadius * sepRadius;			// Set the Outer Sq Value
		separationDistSq = separationDistMaxSq;					// Set the Separation dist Variable
	}
		
	/**
	 * Set the Flock Speed Limit
	 * @param limit		the rate (pixels/tick) to limit the boid 
	 * 					speed
	 */
	public void setSpeedLimit(float limit){
		speedLimit = limit;
	}
	
	
	
	/**************************************************************************
	 * Container Methods
	 * - These methods add/remove/etc boids from the flocking engine
	 * 
	 */
		
	
	/**
	 * Add a boid to the flock
	 * @param entity
	 */
	public void addBoid(Flockable entity){
		// Add to array list
		_boidflock.add(entity);
		
		// Add to grid
		if(isGridEnabled)
			_boidgrid.addBoid(entity);
	}
	
	/**
	 * Add a list of boid objects
	 * @param boids
	 */
	public void addBoids(List<Flockable> boids){
		_boidflock.addAll(boids);
		if(isGridEnabled)
			_boidgrid.addBoid(boids);
	}

	/**
	 * Remove a boid from the flock
	 * @param entity
	 */
	public void removeBoid(Flockable entity){
		// Remove to List
		_boidflock.remove(entity);
		
		// Remove from grid
		if(isGridEnabled)
			_boidgrid.removeBoid(entity);
	}
	
	/**
	 * Add a Flocking Rule to the engine
	 * 
	 * @param rule		the rule to add
	 */
	public void addRule(AbstractRule rule){
		// Safety check to only allow ONE Bound rule at a time
		if(rule instanceof BoundRule){
			for(AbstractRule r: _rules){
				if(rule instanceof BoundRule){
					rule = new BoundRule(((BoundRule)rule).getBounds(), 75);
					return;
				}
			}
		}
		
		_rules.add(rule);
	}
	
	/**
	 * Remove a Flocking Rule from the engine
	 * 
	 * @param rule		the rule to remove
	 */
	public void removeRule(AbstractRule rule){
		_rules.remove(rule);
	}
	
	
	/**************************************************************************
	 * Flocking Rule Methods
	 */
	
	/*******
	 * Rule Variables
	 */
	// TODO: Convert these to 'cache' variables
	private Vector2D cohesionSum = new Vector2D();		// Alignment 
	private Vector2D alignmentSum = new Vector2D();		// Seperation
	private Vector2D separationSum = new Vector2D();		// Cohesion
	private Vector2D acceleration = new Vector2D(); // Acceleration

	// TODO: Convert these to 'cache' variables
	private int alignmentCount = 0;
	private int separationCount = 0;
	private int cohesionCount = 0;
	
	List<Flockable> nearby = null;
	
	/**
	 * Apply all the Reynold's Rules to the boid
	 * and return the vector change
	 * 
	 *   1) Cohesion
	 *   2) Alignment
	 *   3) Separation
	 * 
	 * @param boid		The boid to apply the rules too
	 * @return			the Acceleration Vector for the boid to add
	 */
	public Vector2D applyReynoldRules(Flockable boid){
		
		// Reset Sum Values
		cohesionSum.set(0, 0);
		alignmentSum.set(0, 0);
		separationSum.set(0, 0);
		acceleration.set(0, 0);
		
		// Reset Count Variables
		alignmentCount = 0;
		separationCount = 0;
		cohesionCount = 0;		
		
		
		if(!isGridEnabled){
			nearby = _boidflock;
		}else{
			nearby = _boidgrid.nearByObjects(boid.getPosition().x, boid.getPosition().y);
		}
		//System.out.println("Nearby Birds: " + nearby.size());
		for(int i=0; i<nearby.size(); i++){
			
			// TODO: Create a 'cache' variable for this
			Flockable ent = nearby.get(i);
			if(ent != boid){
				
				// TODO: Potentially create a 'cache' variable for this
				float distSq = boid.getPosition().distSq(ent.getPosition());
				
				// Main Distance Check
	    		if (distSq < (radius*radius)) {
	    			//System.out.println("Dist: " + distSq);
	    			// added their pos to cohesion
	    			cohesionSum.add(ent.getPosition());
	    			cohesionCount++;

	    			// add their velocity to alignment
	    			alignmentSum.add(ent.getVelocity());
	    			alignmentCount++;

	    			// the separation distance will be smaller than cohere distance
	    			// birds will try and maintain a particular distance from each other
	    			if (distSq < separationDistSq) {
	    				separationSum.add(ent.getPosition());
	    				separationCount++;
	    				
	    				// This causes unwanted results in the flocking algorithm, so it is excluded for now (possible deletion)
	    				if (distSq < separationDistMinSq) { // avoid being too close to any particular bird
	    					separationSum.add(Vector2D.mult(boid.getPosition(), 10)); // PVector.mult(other,12) 
	    					separationCount += 10;
	    				} // separation minimum	    				
	    				
	    			} // separation dist check
	    		} // dist check
			}
		}

		/* COHESION RULE */
		if(cohesionCount>0){
			// Average the position
			cohesionSum.div(cohesionCount);
			
			// TODO: Convert to 'cache' variable
			// find the vector between teh average position of this bird's position
			Vector2D desired = Vector2D.sub(boid.getPosition(),cohesionSum); 

			// Multiply by our cohesion
			desired.mult(cohesion);
			
			// Avoid Tightly Packed Groups
			if(cohesionCount > 10 && cohesion > 0){
				desired.mult(-0.25f);
			}
			
			// Add to acceleration
			acceleration.sub(desired);
		}
		
		/* ALIGNMENT RULE */
		if(alignmentCount>0){
			// Average out the alignment
			alignmentSum.div(alignmentCount);
			
			// Set Rotation Variable
			boid.setRotation((float)alignmentSum.rotation());
			
			// Multiply by our alignment property
			alignmentSum.mult(alignment);
			
			// Add to acceleration
			acceleration.add(alignmentSum);
		}
		
		/* SEPARATION RULE */
		if(separationCount>0){
			// Avg out the surrounding birds positions
			separationSum.div(separationCount);
			
			// TODO: Convert to 'cache' variable
			// Find the vector between this bird and the avg position
			Vector2D desired = Vector2D.sub(boid.getPosition(), separationSum);
			desired.mult(separation);
			
			// Add to Acceleration
			acceleration.add(desired);
		}
		
		
		return acceleration;
	}
	
	/**
	 * Limit the Velocity on the boid
	 * @param boid
	 */
	float velMagSq = 0;
	public void limitSpeed(Flockable boid){
		velMagSq = boid.getVelocity().magSq();
	    if (velMagSq > (speedLimit * speedLimit)) {
	    	float velMag = (float) Math.sqrt(velMagSq);
	    	boid.getVelocity().x = (boid.getVelocity().x / velMag) * speedLimit;
	    	boid.getVelocity().y = (boid.getVelocity().y / velMag) * speedLimit;
	    }
	}
		
	
	
	
}
