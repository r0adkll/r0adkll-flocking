package net.r0adkll.flocking;

import net.r0adkll.flocking.utils.FRectangle;
import net.r0adkll.flocking.utils.Vector2D;

public interface Flockable {
	public Vector2D getPosition();
	public Vector2D getVelocity();
	public void setRotation(float value);
	public FRectangle getBounds();
}
