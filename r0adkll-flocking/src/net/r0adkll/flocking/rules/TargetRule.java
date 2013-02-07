package net.r0adkll.flocking.rules;

import net.r0adkll.flocking.Flockable;
import net.r0adkll.flocking.utils.Vector2D;

/**
 * This is a Target Rule, this means the flockable object
 * you pass it will become a target by the whole flock causing
 * them to flock AT you when they cross into your 'radius'
 * 
 * @author r0adkll
 *
 */
public class TargetRule extends AbstractRule{

	/**
	 * Variables
	 */
	private Flockable _entity;
	private float _radius;
	private int _factor; 
	
	/**
	 * Constructor
	 * @param target	the flockable target object
	 * @param radius	the radius of flocking detection
	 * @param factor	the factor of how fast they swarm you
	 */
	public TargetRule(Flockable target, float radius, int factor){
		_entity = target;
		_radius = radius;
		_factor = factor;
	}
	
	@Override
	public Vector2D applyRule(Flockable boid) {
		// first make sure the bird is close enough
		float mDiffX = boid.getPosition().x - _entity.getPosition().x;
		float mDiffY = boid.getPosition().y - _entity.getPosition().y;
		float mDistSq = mDiffX * mDiffX + mDiffY * mDiffY;
		if (mDistSq < getRadiusSq()) {
			float mDist = (float) Math.sqrt(mDistSq);
			// push it away by normalizing the vector from mouse to position, and changing the vector length to 100
			return new Vector2D( -((mDiffX/mDist) * _factor), -((mDiffY/mDist) * _factor));
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
