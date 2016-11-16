package com.example.navybattle;

import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.util.Log;


public abstract class Computer {
	int gBoard[];
	public ArrayList<Integer> vessel;
	int shape;		// 10=vertical, 1=horizontal
	
	public Computer () {
		gBoard = new int[100];
		for (int i=0; i<100; i++)	{
			gBoard[i] = 0;
		}
	vessel = new ArrayList<Integer>();	
	}
	
	public void newGame()	{
		for (int i=0; i<100;i++)	gBoard[i] = 0;
	}
	
	/**
	 * Computer gets neighbors list of sunk ship 
	 * @param list
	 */
	public void getDeadCells(ArrayList<Integer> list)	{
		for (int i=0; i<list.size();i++)	{
			gBoard[list.get(i)] = 9;
		}
	}
	
	/**
	 * Method to get random move while no partially hit ships
	 * @return
	 */
	public int getMove()	{
		Random rand = new Random();
		int aim = rand.nextInt(100); 
		while (gBoard[aim] != 0)		aim = rand.nextInt(100);
		gBoard[aim] = 1;
		return aim;
	}
	
	/**
	 * Method to get move if some ship was partially hit
	 * @param hit
	 * @return
	 */
	@SuppressLint("UseValueOf")
	public int getMove(int hit)	{
		int low, high;
		Random rand = new Random();
		int side;		//randomly pick a side to search from a hit vessel: 0 - left, 1 - top, 2 - right, 3 - down
		boolean found=false;
		
		if(!vessel.contains(new Integer(hit)))	{
			gBoard[hit] = 1;
			vessel.add(hit);
		}

		
		if (vessel.size() > 1)	{
			shape = (int) Math.sqrt((vessel.get(0)-vessel.get(1))*(vessel.get(0)-vessel.get(1)));
			low = findEdge(0,vessel);
			high = findEdge(1,vessel);
			
			if (shape == 1)		{
				if (checkAvailability(0,low-1))		{					
					gBoard[low-1]=1;
					return low-1;
				}
				else	{
					gBoard[high+1]=1;
					return high+1;
				}
			}
			else	{			
				if (checkAvailability(0,low-10))		{
					gBoard[low-10]=1;
					return low-10;
				}
				else	{
					gBoard[high+10]=1;
					return high+10;
				}
			}
		}
		
		else	{
			while (!found)	{
				side = getSide(rand);				
				if (side == 0)	{
					if (checkAvailability(0,hit-1))	{	gBoard[hit-1] = 1;	Log.d("hit-1 ", Integer.toString(hit-1));	found=true;     return hit-1;		}
					
				}
				else if (side == 1)	{
					if (checkAvailability(1,hit-10))	{	gBoard[hit-10] = 1; Log.d("hit-10 ", Integer.toString(hit-10));	found=true;	return hit-10;	}
				}	
				else if (side == 2)	{
					if (checkAvailability(2,hit+1))	{	gBoard[hit+1] = 1;	Log.d("hit+1 ", Integer.toString(hit+1)); found=true;		return hit+1;	}
				}
				else if (side == 3)	{
					if (checkAvailability(3,hit+10))	{	gBoard[hit+10] = 1;	Log.d("hit+10 ", Integer.toString(hit+10)); found=true;	return hit+10;	}
				}	
				}
		}
		return -1;			
		}
	
	public int getSide (Random rand)	{
		return rand.nextInt(4);
	}
	
	public boolean checkAvailability (int side, int index)	{  //проверка возможности выстрела по данным каординатам
		if (index < 0 || index > 99)	return false;
		if (gBoard[index] != 0)		return false;
		if (side == 0 && (index % 10 == 9))	return false;	
		else if ((side == 2)	&& ((index) % 10 == 0))	return false;	
		return true;
		}
	
	@SuppressLint("UseValueOf")
	public int findEdge (int side, ArrayList<Integer> list)	{
		int min=new Integer(list.get(0)), max=new Integer(list.get(0));
		for (int i=0; i<list.size();i++)	{
			if (list.get(i) < min)	min = new Integer(list.get(i)); 
			if (list.get(i) > max)	max = new Integer(list.get(i));
			}

		if (side == 0)	return min;
		else	return max;
	}
	
	
}
