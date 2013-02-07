package net.r0adkll.flocking.rules;

import net.r0adkll.flocking.Flockable;
import net.r0adkll.flocking.utils.FRectangle;
import net.r0adkll.flocking.utils.Vector2D;

/**
 * This rule is for bounding all the boids in one giant rectangle
 * so that they don't go flying off into limbo.
 * 
 * @author r0adkll
 */
public class BoundRule extends AbstractRule{

	/**
	 * Variables
	 */
	private FRectangle _rect;
	private float _factor;
	
	private float XMIN;
	private float XMAX;
	private float YMIN;
	private float YMAX;
	
	/**
	 * Constructor
	 * @param rect	the rectangle to bound all the boids in
	 */
	public BoundRule(FRectangle rect, float factor){
		_rect = rect;
		_factor = factor;
	}
	
	@Override
	public Vector2D applyRule(Flockable boid) {
		XMIN = _rect.getX();
		XMAX = _rect.getX() + _rect.getWidth();
		YMIN = _rect.getY();
		YMAX = _rect.getY() + _rect.getHeight();
		Vector2D v = new Vector2D();
		if(boid.getPosition().x < XMIN){
			v.x = _factor;
		}else if(boid.getPosition().x > XMAX){
			v.x = -_factor;
		}
		if(boid.getPosition().y < YMIN){
			v.y = _factor;
		}else if(boid.getPosition().y > YMAX){
			v.y = -_factor;
		}

		return v;
		
	}

}
