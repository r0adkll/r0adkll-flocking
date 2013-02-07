package net.r0adkll.flocking.rules;

import net.r0adkll.flocking.Flockable;
import net.r0adkll.flocking.utils.Vector2D;

public class HazardRule extends AbstractRule{

	/**
	 * Variables
	 */
	private Flockable _entity;
	private float _radius;
	private int _factor; 
	
	/**
	 * Constructor
	 * @param hazard	the flockable object that must be avoided
	 * @param radius	the radius of boid detection
	 * @param factor	the factor of avoidance, how fast do they 'avoid'
	 */
	public HazardRule(Flockable hazard, float radius, int factor){
		_entity = hazard;
		_radius = radius;
		_factor = factor;
	}
	
	
	@Override
	public Vector2D applyRule(Flockable boid) {
		// first make sure the bird is close enough
		float mDiffX = _entity.getPosition().x - boid.getPosition().x;
		float mDiffY = _entity.getPosition().y - boid.getPosition().y;
		float mDistSq = mDiffX * mDiffX + mDiffY * mDiffY;
		if (mDistSq < getRadiusSq()) {
			float mDist = (float) Math.sqrt(mDistSq);
			// push it away by normalizing the vector from mouse to position, and changing the vector length to factor
			return new Vector2D((mDiffX/mDist) * _factor, (mDiffY/mDist) * _factor);
		}


		return new Vector2D();
	}
	
	/**
	 * Get the Radius distance squared for 
	 * more effiecient calculations
	 * @return
	 */
	private float getRadiusSq(){
		return _radius * _radius;
	}

}
