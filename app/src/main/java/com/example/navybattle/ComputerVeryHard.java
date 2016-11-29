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
	ArrayList<Integer> huntOther;
	private int fourCell, others;				//counters for sunk big ships


	public ComputerVeryHard(ArrayList fromDB)	{
		super();
		fourCell = 0;
		others = 0;
		hunt4 = new ArrayList<Integer>();
		hunt23 = new ArrayList<Integer>();
		huntOther = new ArrayList<Integer>();
		fill4(fromDB,hunt4);
		fill23(fromDB,hunt23);
		fillOther(fromDB,huntOther);
		}
	
	public void newGame(ArrayList fromDB)	{
		for (int i=0; i<100;i++)	gBoard[i] = 0;
		fourCell = 0;
		others = 0;
		hunt4 = new ArrayList<Integer>();
		hunt23 = new ArrayList<Integer>();
		fill4(fromDB,hunt4);
		fill23(fromDB,hunt23);
		fillOther(fromDB,huntOther);
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
		else if (vessel.size() == 3)	{
			Log.i("s","3");
			Log.i("v",Integer.toString(vessel.size()));//if aircarrier was sunk, look for cruisers and corvettes
			fourCell = 1;	
			Log.i("FourCell: ", Integer.toString(fourCell));
			if(hunt4!=null){
				hunt23.addAll(0,hunt4);
				ArrayList<Integer> fromBD = new ArrayList<>();
				fromBD = Hawk.get("fromBD");
				sortAgain(fromBD,hunt23);

			}
			ans = get23Move();
		}
		
		else if (vessel.size() == 1 && others<5 || vessel.size() == 2 && others<5)	{
			Log.i("s","2");
			Log.i("v",Integer.toString(vessel.size()));
			//one of the cruisers or corvettes was sunk
			ans = get23Move();
		}
		
		else if (others < 6 && vessel.size()==0){	ans = get23Move();
			Log.i("s","2b");
			Log.i("v",Integer.toString(vessel.size()));}//one of the cruisers or corvettes is alive
		
		else	{
			if(vessel.size()!=0)vessel.clear();
			Log.i("s","1");
			Log.i("v",Integer.toString(vessel.size()));//only patrol boats left
			ind = huntOther.get(huntOther.size()-1);
			while (gBoard[ind] != 0)	{
				huntOther.remove(huntOther.size()-1);
				ind = huntOther.get(huntOther.size()-1);
			}
			gBoard[huntOther.get(huntOther.size()-1)] = 1;
			ans = ind;
			huntOther.remove(huntOther.get(huntOther.size()-1));
		}	
		return ans;
	}
	
	
	public int get23Move()	{
		int ind=-1,ans;
		vessel.clear();
		if (hunt23.size() > 1)	{
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
			Log.i("s","1");
			Log.i("v",Integer.toString(vessel.size()));//only patrol boats left
			ind = huntOther.get(huntOther.size()-1);
			while (gBoard[ind] != 0)	{
				huntOther.remove(huntOther.size()-1);
				ind = huntOther.get(huntOther.size()-1);
			}
			gBoard[huntOther.get(huntOther.size()-1)] = 1;
			ans = ind;
			huntOther.remove(huntOther.get(huntOther.size()-1));
		}
		return ans;
	}
	
	private void fill4 (ArrayList<Integer> fromBD,ArrayList<Integer> al)	{
		ArrayList<Map<Integer,Integer>> act = new ArrayList<Map<Integer,Integer>>(24);
		Integer[] pul ={3,7,12,16,21,25,29,30,34,38,43,47,52,56,61,65,69,70,74,78,83,87,92,96};


		for(int i=0;i<pul.length;i++){
			Map m = new HashMap<Integer,Integer>();
			m.put(pul[i], fromBD.get(pul[i]));
			act.add(i, m);
		}


		Collections.sort(act, new Comparator<Map<Integer,Integer>>() {
			@Override
			public int compare(Map<Integer, Integer> o1, Map<Integer, Integer> o2) {
				int res = 0;

				for(int key:o1.keySet()){
					for(int key2:o2.keySet()) {
						res =  o1.get(key) - o2.get(key2);
					}
				}

				return res;
			}
		});



		for(int i=0;i<act.size();i++){
			for ( Integer key : act.get(i).keySet() ) {
				al.add(i,key);
			}

		}



	}
	private void fillOther (ArrayList<Integer> fromBD, ArrayList<Integer> al)	{
		huntOther.clear();
		ArrayList<Map<Integer,Integer>> act = new ArrayList<Map<Integer,Integer>>(24);


		for(int i=0;i<fromBD.size();i++){
			Map m = new HashMap<Integer,Integer>();
			m.put(i, fromBD.get(i));
			act.add(i, m);
		}


		Collections.sort(act, new Comparator<Map<Integer,Integer>>() {
			@Override
			public int compare(Map<Integer, Integer> o1, Map<Integer, Integer> o2) {
				int res = 0;

				for(int key:o1.keySet()){
					for(int key2:o2.keySet()) {
						res =  o1.get(key) - o2.get(key2);
					}
				}

				return res;
			}
		});



		for(int i=0;i<act.size();i++){
			for ( Integer key : act.get(i).keySet() ) {
				al.add(i,key);
			}

		}



	}

	private void sortAgain (ArrayList<Integer> fromBD, ArrayList<Integer> al)	{
		ArrayList<Map<Integer,Integer>> act = new ArrayList<Map<Integer,Integer>>();



		for(int i=0;i<al.size();i++){
			Map m = new HashMap<Integer,Integer>();
			m.put(al.get(i), fromBD.get(al.get(i)));
			act.add(i, m);
		}


		Collections.sort(act, new Comparator<Map<Integer,Integer>>() {
			@Override
			public int compare(Map<Integer, Integer> o1, Map<Integer, Integer> o2) {
				int res = 0;

				for(int key:o1.keySet()){
					for(int key2:o2.keySet()) {
						res =  o1.get(key) - o2.get(key2);
					}
				}

				return res;
			}
		});


		al.clear();

		for(int i=0;i<act.size();i++){
			for ( Integer key : act.get(i).keySet() ) {
				al.add(i,key);
			}

		}

		String s;
		s="dasd";



	}
	


	private void fill23 (ArrayList<Integer> fromBD,ArrayList<Integer> al)	{
		ArrayList<Map<Integer,Integer>> act = new ArrayList<Map<Integer,Integer>>();
		Integer[] pul = {1,5,9,10,14,18,23,27,32,36,41,45,49,50,54,58,63,67,72,76,81,85,89,90,94,98};
		for(int i=0;i<pul.length;i++) {
			Map m = new HashMap<Integer, Integer>();
			m.put(pul[i], fromBD.get(pul[i]));
			act.add(i, m);
		}


		Collections.sort(act, new Comparator<Map<Integer,Integer>>() {
				@Override
				public int compare(Map<Integer, Integer> o1, Map<Integer, Integer> o2) {
					int res = 0;

					for(int key:o1.keySet()){
						for(int key2:o2.keySet()) {
							res =  o1.get(key) - o2.get(key2);
						}
					}

					return res;
				}


		});



		for(int i=0;i<act.size();i++){
				for ( Integer key : act.get(i).keySet() ) {
					al.add(i,key);
				}

		}
			String s;
			s="s";





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


