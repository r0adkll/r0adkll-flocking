package net.r0adkll.flocking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.r0adkll.flocking.utils.Vector2D;

public class FlockGrid {
	// The cells map keeps track of where each object is
	// The grid list keeps track of which objects are in each cell

	Map<Flockable, Vector2D> cells = new HashMap<Flockable, Vector2D>(); // Key: Object object, Value: grid position... (x/scale,y/scale)
	// holds the position of the object in the grid. This makes sure that the object gets removed and replaced each time
	// it changes position in the grid.

	List<Flockable> [][] grid; // grid[x/scale][y/scale] = List of objects
	public int cellSize;
	
	public int cell_width = 0;
	public int cell_height = 0;
	
	/**
	 * Constructor
	 * 
	 * @param cellSize		the size of the grid cells
	 * @param objects		the array list of objects
	 * @param width			the width of the bounds
	 * @param height		the height of the bounds
	 */
	FlockGrid (int cellSize, int width, int height) {
		this.cellSize = cellSize;
		this.cell_width = (width/cellSize)+1;
		this.cell_height = (height/cellSize)+1;
		
		System.out.println("Cell Size[" + cell_width + "," + cell_height + "]");
		
		grid = new ArrayList[cell_width][cell_height];

		// construct a list for each cell
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[x].length; y++) {
				grid[x][y] = new ArrayList<Flockable>(); // a list of objects for every cell
			}
		}
	}
	
	/**
	 * Add a boid to the grid system
	 * @param boid		the boid to add
	 */
	public void addBoid(Flockable boid){
	
		Vector2D cellPos = new Vector2D((int)(boid.getPosition().x / cellSize), (int)(boid.getPosition().y / cellSize));
		cells.put(boid, cellPos);
		grid[(int)cellPos.x][(int)cellPos.y].add(boid);
		
	}
	
	/**
	 * Add a collection of boids
	 * @param list
	 */
	public void addBoid(List<Flockable> list){
		for (Flockable object : list) {
			addBoid(object);
		}
	}
	
	/**
	 * Remove Boid from Grid
	 * @param boid
	 */
	public void removeBoid(Flockable boid){
		
		// Remove from Hashmap
		Vector2D pos = cells.remove(boid);
		int oldCellX = (int)pos.x;
		int oldCellY = (int)pos.y;
		Vector2D cPos = new Vector2D((int)(boid.getPosition().x/cellSize), (int)(boid.getPosition().y/cellSize));
		int cellX = (int)cPos.x;
		int cellY = (int)cPos.y;
		
		// safety check
		if (cellX >= 0 && cellX < grid.length && cellY >= 0 && cellY < grid[cellX].length) {
			
			// Remove boid from current cell if exists
			if(grid[cellX][cellY].contains(boid)){
				grid[cellX][cellY].remove(boid);
			}
			
			// Remove boid from cached cell if exists
			if(grid[oldCellX][oldCellY].contains(boid)){
				grid[oldCellX][oldCellY].remove(boid);
			}
			
		}
	}
	
	
	
	/** 
	 * Update needs to be called on by each object after they update their position
	 * this makes sure that the object is still in the correct cell
	 */
	public void update(Flockable object) {
		// first check if it needs to be moved

		// Find cell we stored it in
		Vector2D oldCellPos = (Vector2D) cells.get(object);
		int oldCellX = (int)oldCellPos.x;
		int oldCellY = (int)oldCellPos.y;

		// find cell it is in currently
		int cellX = (int)(object.getPosition().x / cellSize);
		int cellY = (int)(object.getPosition().y / cellSize);

		// if they match, stop there
		if (cellX == oldCellX && cellY == oldCellY)
			return;

		// safety check
		if (cellX >= 0 && cellX < grid.length && cellY >= 0 && cellY < grid[cellX].length) {
			// if the grid the object is in now doesn't contain that object...
			if (!grid[cellX][cellY].contains(object)) {
				// add the object and remove the object from its former cell
				grid[cellX][cellY].add(object);
				grid[oldCellX][oldCellY].remove(object);
				
				// memorize which cell the object is in
				Vector2D oldVal = cells.get(object);
				if(oldVal != null){
					oldVal.set(cellX, cellY);
					cells.put(object, oldVal);
				}else{
					cells.put(object, new Vector2D(cellX, cellY));
				}
				//System.out.println("Object Changed Cells[" + oldCellX + "," + oldCellY + "] -to- [" + cellX + "," + cellY + "]");
				
			}
		}
	}

	/**
	 * Find a list of nearby objects
	 * goes through each cell adjacent to the current cell, and the current cell,
	 * and generates a list of objects contained in those cells
	 */
	private List<Flockable> nearBy = new ArrayList<Flockable>();
	private int _size = 0;
	public List<Flockable> nearByObjects (float x, float y) {
		nearBy.clear();
		
		int cellX = (int)(x / cellSize);
		int cellY = (int)(y / cellSize);

		if (cellX >= 0 && cellX < grid.length) {
			// center column
			if (cellY >= 0 && cellY < grid[cellX].length) {
				// middle
				_size = grid[cellX][cellY].size();
				for (int i=0; i<_size; i++){
					nearBy.add(grid[cellX][cellY].get(i));
					//System.out.println("Bird middle[" + cellX + "," + cellY + "]");
				}
				// top
				// cellY+1 >= 0 can be assumed since cellY >= 0 is checked in this block
				if (cellY+1 < grid[cellX].length) {
					_size = grid[cellX][cellY + 1].size();
					for (int i=0; i<_size; i++){
						nearBy.add(grid[cellX][cellY + 1].get(i));
						//System.out.println("Bird top[" + cellX + "," + (cellY+1) + "]");
					}
				}
			}
			// bottom
			if (cellY-1 >= 0 && cellY-1 < grid[cellX].length) {
				_size = grid[cellX][cellY - 1].size();
				for (int i=0; i<_size; i++){
					nearBy.add(grid[cellX][cellY - 1].get(i));
					//System.out.println("Bird bottom[" + cellX + "," + (cellY-1) + "]");
				}
			}

			// right column
			if (cellX+1 < grid.length) {
				if (cellY >= 0 && cellY < grid[cellX + 1].length) {
					// middle right
					_size = grid[cellX + 1][cellY].size();
					for (int i=0; i<_size; i++){
						nearBy.add(grid[cellX + 1][cellY].get(i));
						//System.out.println("Bird middleright[" + (cellX + 1) + "," + cellY + "]");
					}
					if (cellY+1 < grid[cellX + 1].length) {
						// top right
						_size = grid[cellX + 1][cellY + 1].size();
						for (int i=0; i<_size; i++){
							nearBy.add(grid[cellX + 1][cellY + 1].get(i));
							//System.out.println("Bird topright[" + (cellX + 1) + "," + (cellY+1) + "]");
						}
					}
				}
				if (cellY-1 >= 0 && cellY-1 < grid[cellX + 1].length) {
					// bottom right
					_size = grid[cellX + 1][cellY - 1].size();
					for (int i=0; i<_size; i++){
						nearBy.add(grid[cellX + 1][cellY - 1].get(i));
						//System.out.println("Bird bottomright[" + (cellX + 1) + "," + (cellY-1) + "]");
					}
				}
			}
		}

		// left column
		if (cellX-1 >= 0 && cellX-1 < grid.length) {
			if (cellY >= 0 && cellY < grid[cellX - 1].length) {
				// center left
				_size = grid[cellX - 1][cellY].size();
				for (int i=0; i<_size; i++){
					nearBy.add(grid[cellX - 1][cellY].get(i));
					//System.out.println("Bird centerleft[" + (cellX - 1) + "," + (cellY) + "]");
				}
				
				// top left
				if (cellY+1 < grid[cellX - 1].length) {
					_size = grid[cellX - 1][cellY + 1].size();
					for (int i=0; i<_size; i++){
						nearBy.add(grid[cellX - 1][cellY + 1].get(i));
						//System.out.println("Bird topleft[" + (cellX - 1) + "," + (cellY + 1) + "]");
					}
				}
			}
			if (cellY-1 >= 0 && cellY-1 < grid[cellX - 1].length) {
				// bottom left
				_size = grid[cellX - 1][cellY - 1].size();
				for (int i=0; i<_size; i++){
					nearBy.add(grid[cellX - 1][cellY - 1].get(i));
					//System.out.println("Bird bottomleft[" + (cellX - 1) + "," + (cellY - 1) + "]");
				}
			}
		}
		
		return nearBy;
	}

}
