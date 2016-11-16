package com.example.navybattle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import android.annotation.SuppressLint;
import android.util.Log;

public class Ship implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int coordinates[];
	public ArrayList<Integer> neighbors = new ArrayList<Integer>();
	public int size, shape, cell;				//size: 1-4 (cells), shape: 0-1 (0=vertical, 1=horizontal)
	
	
	//Constructor
	public Ship (int shape, int size)	{
		this.size = size;
		this.shape = shape;
		coordinates = new int[size];
		cell = 0;
	}
	
	
	//Methods
	
	
	public int shipType ()	{	return shape;	}
	
	public int shipSize()	{	return size;	}
	
	public int[] getCoordinates ()	{		return coordinates;		}
	
	
	public void setCoordinates (int index)	{
		coordinates[cell] = index;		
		cell++;
		if (cell == size)	{
			createNeighborsList();
			
		}
		}
	
	
	@SuppressLint("UseValueOf")
	private void createNeighborsList()	{
		if (size == 1)	addNeighbors(coordinates[0]);
		else if (size == 2)	{
			addNeighbors(coordinates[0]);
			addNeighbors(coordinates[1]);
			neighbors.remove(new Integer(coordinates[0]));
			neighbors.remove(new Integer(coordinates[1]));
		}
		else if (size == 3)	{
			addNeighbors(coordinates[0]);
			addNeighbors(coordinates[2]);
			neighbors.remove(new Integer(coordinates[1]));
			neighbors.remove(new Integer(coordinates[1]));
		}
		else if (size == 4)	{
			addNeighbors(coordinates[0]);
			addNeighbors(coordinates[3]);
			neighbors.remove(new Integer(coordinates[1]));
			neighbors.remove(new Integer(coordinates[2]));
		}		
		
		//removing duplicates from a list by using HashSet which doesn't allow duplicates
		HashSet<Integer> hs = new HashSet<Integer>();
		hs.addAll(neighbors);
		neighbors.clear();
		neighbors.addAll(hs);	
		
	}
	
	
	private void addNeighbors (int index)	{
		if (index == 0)	{		//top left (0)
			neighbors.add(index+1);	neighbors.add(index+10);	neighbors.add(index+11);
		}		
		else if (index == 9)	{	//top right (9)
			neighbors.add(index-1);	neighbors.add(index+10);	neighbors.add(index+9);
		}
		else if (index == 90)	{	//bottom left (90)
			neighbors.add(index+1);	neighbors.add(index-10);	neighbors.add(index-9);
		}
		else if (index == 99)	{	//bottom right (99)
			neighbors.add(index-10);	neighbors.add(index-11);	neighbors.add(index-1);
		}
		
		//cells with 5neighbors
		else if (index > 0 && index < 9)	{		//top row of cells (1-8)
			neighbors.add(index+1);	neighbors.add(index-1);	neighbors.add(index+9); neighbors.add(index+10);	neighbors.add(index+11);	
		}
		else if (index >90 && index < 99)	{	//bottom row of cells (91-98)
			neighbors.add(index+1);	neighbors.add(index-1);	neighbors.add(index-9); neighbors.add(index-10);	neighbors.add(index-11);	
		}
		else if (index > 0 && index < 90 && (index%10 == 0))	{	//left column of cells (10,20,..,80)
			neighbors.add(index+1);	neighbors.add(index-10);	neighbors.add(index-9); neighbors.add(index+10);	neighbors.add(index+11);	
		}
		else if (index > 9 && index < 99 && (index%10 == 9))	{	//right column of cells (19,29,...,89)
			neighbors.add(index-1);	neighbors.add(index-10);	neighbors.add(index-11); neighbors.add(index+10);	neighbors.add(index+9);	
		}
		
		//cells with 8neighbors
		else	{
			neighbors.add(index+1);	neighbors.add(index-1);	neighbors.add(index+9); neighbors.add(index+10);	neighbors.add(index+11);	neighbors.add(index-9);	
			neighbors.add(index-10);	neighbors.add(index-11);
		}
	}
	
	
	public void shipHit (int index)	{
		boolean hit = false;
		for (int i=0; !hit && i<size;i++)	{
			if (coordinates[i] == index)	{
				coordinates[i] = coordinates[i]+110;			//index becomes x+110 which means it was hit	
				Log.e("New Coordinate", Integer.toString(coordinates[i]));
				hit = true;
			}
		}
		
	}
	
	/**
	 * 	
	 * @return	0 = ship is alive, 1 = ship was sunk
	 */
	public int shipStatus ()	{
		for (int i=0; i<size;i++)	{
			if (coordinates[i] < 100)	return 0;
		}
		return 1;
	}
	
	public ArrayList<Integer> getNeighbors ()	{
		return neighbors;
	}

	
	
	
	
	
	
}
