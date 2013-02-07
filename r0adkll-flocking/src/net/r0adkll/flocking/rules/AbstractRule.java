package net.r0adkll.flocking.rules;

import net.r0adkll.flocking.Flockable;
import net.r0adkll.flocking.utils.Vector2D;

/**
 * This is the base model class for creating
 * Flocking rules to be applyed by the algorithms
 * 
 * @author r0adkll
 *
 */
public abstract class AbstractRule {

	
	/**
	 * This is the method in which overriding will apply
	 * it's custom rules
	 * @param boid		the flockable entity to apply rules
	 * @return			return the resulting 2D vector
	 */
	public abstract Vector2D applyRule(Flockable boid);
	
}
