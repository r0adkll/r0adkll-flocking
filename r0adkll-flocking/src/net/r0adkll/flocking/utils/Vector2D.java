package net.r0adkll.flocking.utils;

/**
 * Vector Helper class for Flocking Algorithm
 * 
 * 
 * @author r0adkll
 */
public class Vector2D {

	/**
	 * Variables
	 */
	public float x;
	public float y;
	
	
	/****************************************************************************
	 * Constructors
	 */
	
	
	/**
	 * Default Constructor
	 */
	public Vector2D(){
		x = 0;
		y = 0;
	}
	
	/**
	 * Parameter Constructor
	 * @param x
	 * @param y
	 */
	public Vector2D(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Recursive Contructor
	 * @param v		Boid Vector
	 */
	public Vector2D(Vector2D v){
		x = v.x;
		y = v.y;
	}

	/****************************************************************************
	 * Vector Methods
	 */
	
	/**
	 * Set the Vector's X and Y
	 * and return this vector for chaining
	 * @param x
	 * @param y
	 * @return
	 */
	public Vector2D set(float x, float y){
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vector2D set(Vector2D v){
		this.x = v.x;
		this.y = v.y;
		return this;
	}
	
	/**
	 * *CHAINING*
	 *  - add the supplied vector to this current 
	 *    vector.
	 * @param v		the vector to add	
	 * @return		this vector for chaining
	 */
	public Vector2D add(Vector2D v){
		return set(x + v.x, y + v.y);
	}
	
	/**
	 * *CHAINING*
	 *  - subtract the supplied vector to this current 
	 *    vector.
	 * @param v		the vector to subtract	
	 * @return		this vector for chaining
	 */
	public Vector2D sub(Vector2D v){
		return set(x - v.x, y - v.y);
	}
	
	/**
	 * *CHAINING*
	 *  - Multiply this vector by a scalar.
	 * @param scalar		the scale to multiply by
	 * @return				this vector for chaining
	 */
	public Vector2D mult(float scalar){
		return set(x*scalar, y*scalar);
	}
	
	/**
	 * *CHAINING*
	 *  - Divide this vector by a scalar.
	 * @param scalar		the scale to divide by
	 * @return				this vector for chaining
	 */
	public Vector2D div(float scalar){
		return set(x * (1/scalar), y * (1/scalar));
	}
	
	/**
	 * Get the Distance Squared between
	 * this vector and the supplied one.
	 * @param v		the vector to compute the distance with
	 * @return		the distance squared
	 */
	public float distSq(Vector2D v){
		float xDiff = x - v.x;
		float yDiff = y - v.y;
		float distSq = xDiff * xDiff + yDiff * yDiff;
		return distSq;
	}
	
	/**
	 * Get the Distance between this vector
	 * and another vector
	 * @param v
	 * @return
	 */
	public float dist(Vector2D v){
		float xDiff = x - v.x;
		float yDiff = y - v.y;
		float distSq = xDiff * xDiff + yDiff * yDiff;
		return (float) Math.sqrt(distSq);
	}
	
	/**
	 * Get the Magnitude of this vector
	 * 
	 * @return   |this Boid vector|
	 */
	public float magSq(){
		return ((x*x) + (y*y));
	}
	
	/**
	 * Normalize the Vector
	 * 
	 * CAVEAT: 
	 * 		This method uses Math.sqrt, which
	 * 		can be very CPU costly
	 * 
	 * @return
	 */
	public Vector2D normalize(){
		float mag = (float) Math.sqrt(magSq());
		return this.div(mag);
	}
	
	/**
	 * Compute the Rotation of this vector
	 * @return
	 */
	public double rotation(){
		double angle = Math.atan2(y, x);
		return Math.toDegrees(angle);
	}
	
	/**
	 * Print this vector
	 */
	@Override
	public String toString(){
		return "[" + x + "," + y + "]";
	}
	
	/****************************************************************************
	 * Static Vector Methods
	 */
	
	public static Vector2D add(Vector2D v1, Vector2D v2){
		float x = v1.x + v2.x;
		float y = v1.y + v2.y;
		return new Vector2D(x,y);
	}
	
	/**
	 * Subtract 'v2' Vector from the 'v1' Vector
	 * @param v1		the Vector to subtract from
	 * @param v2		the Vector to subtract with
	 * @return			the difference
	 */
	public static Vector2D sub(Vector2D v1, Vector2D v2){
		float x = v1.x - v2.x;
		float y = v1.y - v2.y;
		return new Vector2D(x,y);
	}
	
	public static Vector2D mult(Vector2D v, float scalar){
		float x = v.x * scalar;
		float y = v.y * scalar;
		return new Vector2D(x,y);
	}
	
	public static Vector2D div(Vector2D v, float scalar){
		float nx = v.x * (1/scalar);
		float ny = v.y * (1/scalar);
		return new Vector2D(nx, ny);
	}
	
	
}
