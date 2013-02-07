package net.r0adkll.flocking.rules;

import net.r0adkll.flocking.Flockable;
import net.r0adkll.flocking.utils.FRectangle;
import net.r0adkll.flocking.utils.Vector2D;

/**
 * This is the Rectangle collision avoidance rule, causing the flock
 * to 'collide' with set rectangles and be forced to move around them
 * @author r0adkll
 *
 */
public class RectCollisionRule extends AbstractRule{

	/**
	 * Variables
	 */
	private FRectangle _rect;
	private Collision _listener;
	
	private float bLeft, bRight, bTop, bBottom;
	private float rLeft, rRight, rTop, rBottom;
	private float oLeft, oRight, oTop, oBottom;
	
	/**
	 * Constructor
	 * @param rect		the rect to avoid
	 */
	public RectCollisionRule(FRectangle rect){
		_rect = rect;
	}
	
	/**
	 * Set the collision listener that get's called when there 
	 * is a collision due to this rule
	 * 
	 * @param listener		the collision listener reference
	 */
	public void setOnCollisionListener(Collision listener){
		_listener = listener;
	}
	
	@Override
	public Vector2D applyRule(Flockable boid) {
		// Trivial Rejections
		if(boid.getBounds().x > (_rect.x + _rect.width)) return null;
		if(boid.getBounds().y > (_rect.y + _rect.height)) return null;
		if((boid.getBounds().x + boid.getBounds().width) < _rect.x) return null;
		if((boid.getBounds().y + boid.getBounds().height) < _rect.y) return null;

		// Coarse check the rect
		if(_rect.overlaps(boid.getBounds())){

			bLeft = boid.getBounds().x;
			bTop = boid.getBounds().y;
			bRight = bLeft + boid.getBounds().width;
			bBottom = bTop + boid.getBounds().height;
			rLeft = _rect.x;
			rTop = _rect.y;
			rRight = rLeft + _rect.width;
			rBottom = rTop + _rect.height;

			// Compute overlap
			if(rBottom > bBottom) oBottom = bBottom;
			else oBottom = rBottom;

			if(rTop < bTop) oTop = bTop;
			else oTop = rTop; 

			if(rLeft < bLeft) oLeft = bLeft;
			else oLeft = rLeft;

			if(rRight > bRight) oRight = bRight;
			else oRight = rRight;


			float xDepth = oRight - oLeft;
			float yDepth = oTop - oBottom;
			float absXDepth = (xDepth < 0) ? -xDepth: xDepth;
			float absYDepth = (yDepth < 0) ? -yDepth: yDepth;

			if(absXDepth < absYDepth){
				if(boid.getBounds().x < _rect.x){
					boid.getPosition().x -= xDepth;
				}else{
					boid.getPosition().x += xDepth;
				}

			}else{

				if(boid.getBounds().y < _rect.y){
					boid.getPosition().y += yDepth;
				}else{
					boid.getPosition().y -= yDepth;
				}

			}
			if(_listener != null) _listener.onCollision(boid, xDepth, yDepth);
			return null;
		}
		return null;
	}

	/**
	 * This is the interface that get's called when there is a collision due to this rule
	 * @author r0adkll
	 *
	 */
	public static interface Collision{
		public void onCollision(Flockable boid, float xDepth, float yDepth);
	}
	
}
