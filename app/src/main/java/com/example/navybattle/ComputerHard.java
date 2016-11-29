package com.example.navybattle;

import java.util.ArrayList;
import java.util.Random;

import android.util.Log;

public class ComputerHard extends Computer {
	ArrayList<Integer> hunt4;					//coordinates for aircarrier seeking
	ArrayList<Integer> hunt23;					//coordinates for other NON-patrol boat seeking
	private int fourCell, others;				//counters for sunk big ships

	public ComputerHard ()	{
		super();
		fourCell = 0;
		others = 0;
		hunt4 = new ArrayList<Integer>();
		hunt23 = new ArrayList<Integer>();
		fill4(hunt4);
		fill23(hunt23);
		}
	
	public void newGame()	{
		for (int i=0; i<100;i++)	gBoard[i] = 0;
		fourCell = 0;
		others = 0;
		hunt4 = new ArrayList<Integer>();
		hunt23 = new ArrayList<Integer>();
		fill4(hunt4);
		fill23(hunt23);
	}
	
	public int getMove()	{
		Log.i("Others: ", Integer.toString(others));
		Random rand = new Random();
		int ind=-1,ans;
		if (vessel.size() != 0)	others++;
		if (vessel.size() != 3 && fourCell == 0)			//if hits < 4 --> aircarrier is alive	
			{
			vessel.clear();
			ind = rand.nextInt(hunt4.size());
			while (gBoard[hunt4.get(ind)] != 0)	ind = rand.nextInt(hunt4.size());
			gBoard[hunt4.get(ind)] = 1;
			ans = hunt4.get(ind);
			hunt4.remove(ind);
			}	
		else if (vessel.size() == 3)	{					//if aircarrier was sunk, look for cruisers and corvettes
			fourCell = 1;	
			Log.i("FourCell: ", Integer.toString(fourCell));
			hunt23.addAll(hunt4);
			ans = get23Move(rand);
		}
		
		else if (vessel.size() == 1 || vessel.size() == 2)	{	//one of the cruisers or corvettes was sunk
			ans = get23Move(rand);
		}
		
		else if (others < 6)	ans = get23Move(rand);			//one of the cruisers or corvettes is alive
		
		else	{												//only patrol boats left
			ind = rand.nextInt(100);
			while (gBoard[ind] != 0)	ind = rand.nextInt(100);
			gBoard[ind] = 1;
			ans = ind;
		}	
		return ans;
	}
	
	
	public int get23Move(Random rand)	{
		int ind=-1,ans;
		vessel.clear();
		if (hunt23.size() > 0)	{
			ind = rand.nextInt(hunt23.size());
			while (gBoard[hunt23.get(ind)] != 0)	ind = rand.nextInt(hunt23.size());
			gBoard[hunt23.get(ind)] = 1;
			ans = hunt23.get(ind);
			hunt23.remove(ind);
		}
		else	{
			ind = rand.nextInt(100);
			while (gBoard[ind] != 0)	ind = rand.nextInt(100);
			gBoard[ind] = 1;
			ans = ind;
		}
		return ans;
	}
	
	private void fill4 (ArrayList<Integer> al)	{
		al.add(3);	al.add(7); al.add(12); al.add(16); al.add(21); al.add(25); al.add(29); al.add(30); al.add(34); al.add(38); al.add(43);
		al.add(47); al.add(52); al.add(56); al.add(61); al.add(65); al.add(69); al.add(70); al.add(74); al.add(78); al.add(83); al.add(87);
		al.add(92); al.add(96);
	}
	
	private void fill23 (ArrayList<Integer> al)	{
		al.add(1);	al.add(5); al.add(9); al.add(10); al.add(14); al.add(18); al.add(23); al.add(27); al.add(32); al.add(36); al.add(41);
		al.add(45); al.add(49); al.add(50); al.add(54); al.add(58); al.add(63); al.add(67); al.add(72); al.add(76); al.add(81); al.add(85);
		al.add(89); al.add(90); al.add(94); al.add(98); 
	}
}
