#r0adkll-flocking

Universal Flocking engine written in java.

This engine was built and tested on the Libgdx framework. The algorithm works using Reynold's Three Rules
  1.  Cohesion
  2.  Alignment
  3.  Seperation

This engine allows you set a custom value for each of the three rules allowing your flocking mass to take on any shape 
that it wants

## Usage
  
    private FlockEngine engine;
    
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
    engine.setSpeedLimit(80);`

After creating the engine, you need to populate it with entities that implement the `Flockable` interface, here is my
example from the sample project

    // Initialize all the boids for the example
    for(int i=0; i<BOID_AMOUNT; i++){
      // Create New Boid
      Boid bird = new Boid(image, new Vector2D(MathUtils.random(0, w), MathUtils.random(0, h)),
                      						new Vector2D(MathUtils.random(-20, 20), MathUtils.random(-20, 20)));
			
      // Add to the engine
      engine.addBoid(bird);	
			
      // Add to tracking list
      boids.add(bird);
    }

Once you have everything initialized (including rules) you must call the `FlockEngine.update(float delta)` for the engine
to operate properly. Something like this example:

	public void update(float delta){

		// update the flocking engine
		engine.update(delta);

	}

## Documentation
  More to come...

## License
Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0.html)

## Authors
 * Drew Heavner (VeeDubUSC@gmail.com)
