package com.example.navybattle;

import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.util.Log;

public class ComputerNormal extends Computer {
	
	public ComputerNormal ()	{
		super();
	}
	
	
	/**
	 * Override getMove method by clearing vessel coordinates after sinking ship
	 */
	public int getMove()	{
		vessel.clear();
		Random rand = new Random();
		int aim = rand.nextInt(100); 
		while (gBoard[aim] != 0)		aim = rand.nextInt(100);
		gBoard[aim] = 1;
		return aim;
	}
		

	
	

		
		
}

