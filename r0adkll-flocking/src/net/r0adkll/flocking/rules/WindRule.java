package net.r0adkll.flocking.rules;

import net.r0adkll.flocking.Flockable;
import net.r0adkll.flocking.utils.Vector2D;

/**
 * This is a very simple Wind Rule that simply returns the 
 * previously provided wind vector
 * 
 * @author r0adkll
 */
public class WindRule extends AbstractRule{

	/**
	 * Variables
	 */
	private Vector2D _wind;
	
	/**
	 * Constructor
	 * @param wind	the wind vector
	 */
	public WindRule(Vector2D wind){
		_wind = wind;
	}
	
	@Override
	public Vector2D applyRule(Flockable boid) {
		// TODO Auto-generated method stub
		return _wind;
	}

}
