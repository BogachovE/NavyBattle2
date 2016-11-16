package com.example.navybattle;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.orhanobut.hawk.Hawk;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ComputerVeryHard extends Computer {
	ArrayList<Integer> hunt4;					//coordinates for aircarrier seeking
	ArrayList<Integer> hunt23;					//coordinates for other NON-patrol boat seeking
	private int fourCell, others;				//counters for sunk big ships


	public ComputerVeryHard(ArrayList fromDB)	{
		super();
		fourCell = 0;
		others = 0;
		hunt4 = new ArrayList<Integer>();
		hunt23 = new ArrayList<Integer>();
		fill4(fromDB,hunt4);
		fill23(fromDB,hunt23);
		}
	
	public void newGame(ArrayList fromDB)	{
		for (int i=0; i<100;i++)	gBoard[i] = 0;
		fourCell = 0;
		others = 0;
		hunt4 = new ArrayList<Integer>();
		hunt23 = new ArrayList<Integer>();
		fill4(fromDB,hunt4);
		fill23(fromDB,hunt23);
	}
	
	public int getMove()	{



		Log.i("Others: ", Integer.toString(others));
		Random rand = new Random();
		int ind=-1,ans;
		if (vessel.size() != 0)	others++;
		if (vessel.size() != 3 && fourCell == 0)			//if hits < 4 --> aircarrier is alive	
			{
			vessel.clear();
			ind = hunt4.get(hunt4.size()-1);
			while (gBoard[ind] != 0)	ind = hunt4.get(hunt4.size()-1);
			gBoard[hunt4.get(hunt4.size()-1)] = 1;
			ans = ind;
			hunt4.remove(hunt4.get(hunt4.size()-1));
			}	
		else if (vessel.size() == 3)	{					//if aircarrier was sunk, look for cruisers and corvettes
			fourCell = 1;	
			Log.i("FourCell: ", Integer.toString(fourCell));
			if(hunt4!=null){
				hunt23.addAll(0,hunt4);
				hunt4.clear();
			}
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
			ind = hunt23.get(hunt23.size()-1);
			while (gBoard[ind] != 0){
				hunt23.remove(hunt23.size()-1);
				ind = hunt23.get(hunt23.size()-1);
			}
			gBoard[ind] = 1;
			ans = ind;
			hunt23.remove(hunt23.get(hunt23.size()-1));
		}
		else	{
			ind = rand.nextInt(100);
			while (gBoard[ind] != 0)	ind = rand.nextInt(100);
			gBoard[ind] = 1;
			ans = ind;
		}
		return ans;
	}
	
	private void fill4 (ArrayList<Integer> fromBD,ArrayList<Integer> al)	{
		HashMap<Integer,Integer> act = new HashMap<Integer,Integer>();
		act.put(3,fromBD.get(3));
		act.put(7,fromBD.get(7));
		act.put(12,fromBD.get(12));
		act.put(16,fromBD.get(16));
		act.put(21,fromBD.get(21));
		act.put(25,fromBD.get(25));
		act.put(29,fromBD.get(29));
		act.put(30,fromBD.get(30));
		act.put(34,fromBD.get(34));
		act.put(38,fromBD.get(38));
		act.put(43,fromBD.get(43));
		act.put(47,fromBD.get(47));
		act.put(52,fromBD.get(52));
		act.put(56,fromBD.get(56));
		act.put(61,fromBD.get(61));
		act.put(65,fromBD.get(65));
		act.put(69,fromBD.get(69));
		act.put(70,fromBD.get(70));
		act.put(74,fromBD.get(74));
		act.put(78,fromBD.get(78));
		act.put(83,fromBD.get(83));
		act.put(87,fromBD.get(87));
		act.put(92,fromBD.get(92));
		act.put(96,fromBD.get(96));

		HashMap<Integer,Integer> sorted = new HashMap<Integer,Integer>();
		sorted = (HashMap<Integer, Integer>) sortByValue(act);

		for ( Integer key : sorted.keySet() ) {
			al.add(key);
		}



	}
	
	private void fill23withRest (ArrayList<Integer> fromBD,ArrayList<Integer> al,ArrayList<Integer> rest)	{
		al.clear();
		HashMap<Integer,Integer> act = new HashMap<Integer,Integer>();
		act.put(1,fromBD.get(1));
		act.put(5,fromBD.get(5));
		act.put(9,fromBD.get(9));
		act.put(10,fromBD.get(10));
		act.put(14,fromBD.get(14));
		act.put(18,fromBD.get(18));
		act.put(23,fromBD.get(23));
		act.put(27,fromBD.get(27));
		act.put(32,fromBD.get(32));
		act.put(36,fromBD.get(36));
		act.put(41,fromBD.get(41));
		act.put(45,fromBD.get(45));
		act.put(49,fromBD.get(49));
		act.put(50,fromBD.get(50));
		act.put(54,fromBD.get(54));
		act.put(58,fromBD.get(58));
		act.put(63,fromBD.get(63));
		act.put(67,fromBD.get(67));
		act.put(72,fromBD.get(72));
		act.put(76,fromBD.get(76));
		act.put(81,fromBD.get(81));
		act.put(85,fromBD.get(85));
		act.put(89,fromBD.get(89));
		act.put(90,fromBD.get(90));
		act.put(94,fromBD.get(94));
		act.put(98,fromBD.get(98));

		for (int i=0;i<rest.size();i++){
			act.put(rest.get(i),fromBD.get(rest.get(i)));
		}

		HashMap<Integer,Integer> sorted = new HashMap<Integer,Integer>();
		sorted = (HashMap<Integer, Integer>) sortByValue(act);

		for ( Integer key : sorted.keySet() ) {
			al.add(key);
		}
	}

	private void fill23 (ArrayList<Integer> fromBD,ArrayList<Integer> al)	{
		HashMap<Integer,Integer> act = new HashMap<Integer,Integer>();
		act.put(1,fromBD.get(1));
		act.put(5,fromBD.get(5));
		act.put(9,fromBD.get(9));
		act.put(10,fromBD.get(10));
		act.put(14,fromBD.get(14));
		act.put(18,fromBD.get(18));
		act.put(23,fromBD.get(23));
		act.put(27,fromBD.get(27));
		act.put(32,fromBD.get(32));
		act.put(36,fromBD.get(36));
		act.put(41,fromBD.get(41));
		act.put(45,fromBD.get(45));
		act.put(49,fromBD.get(49));
		act.put(50,fromBD.get(50));
		act.put(54,fromBD.get(54));
		act.put(58,fromBD.get(58));
		act.put(63,fromBD.get(63));
		act.put(67,fromBD.get(67));
		act.put(72,fromBD.get(72));
		act.put(76,fromBD.get(76));
		act.put(81,fromBD.get(81));
		act.put(85,fromBD.get(85));
		act.put(89,fromBD.get(89));
		act.put(90,fromBD.get(90));
		act.put(94,fromBD.get(94));
		act.put(98,fromBD.get(98));



		HashMap<Integer,Integer> sorted = new HashMap<Integer,Integer>();
		sorted = (HashMap<Integer, Integer>) sortByValue(act);

		for ( Integer key : sorted.keySet() ) {
			al.add(key);
		}
	}



	public  <K, V extends Comparable<? super V>> Map<K, V>
		sortByValue( Map<K, V> map )
		{
			List<Map.Entry<K, V>> list =
					new LinkedList<Map.Entry<K, V>>( map.entrySet() );
			Collections.sort( list, new Comparator<Map.Entry<K, V>>()
			{
				public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
				{
					return (o1.getValue()).compareTo( o2.getValue() );
				}
			} );

			Map<K, V> result = new LinkedHashMap<K, V>();
			for (Map.Entry<K, V> entry : list)
			{
				result.put( entry.getKey(), entry.getValue() );
			}
			return result;
		}
	}



