package com.example.navybattle;

import java.io.Serializable;
import java.util.Random;

import android.util.Log;

public class GameBoard implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Ship boardShips[];
	public int hits, size, ships;
	public Ship aircarrier;
	public Ship cruiser[];	
	public Ship corvette[];
	public Ship patrol_boat[];
	public int cruiserIndex, corvetteIndex, patrolIndex;
	
	
	
	
	//constructor
	public GameBoard(int size)	{
		cruiserIndex=0; corvetteIndex=0; patrolIndex=0;
		boardShips = new Ship [size];
		hits = 0;
		this.size = size;
		
		for (int i=0;i<size;i++)	{
			boardShips[i] = null;
		}
		cruiser = new Ship[2];
		corvette = new Ship[3];
		patrol_boat = new Ship[4];
	}
	
	public GameBoard()	{
		cruiserIndex=0; corvetteIndex=0; patrolIndex=0;
		for (int i=0;i<size;i++)	{
			boardShips[i] = null;
		}
		cruiser = new Ship[2];
		corvette = new Ship[3];
		patrol_boat = new Ship[4];
	}
	
	//methods
	
	public void clearBoards()	{
		cruiserIndex=0; corvetteIndex=0; patrolIndex=0;
		for (int i=0;i<size;i++)	{
			boardShips[i] = null;
		}
		hits=0;
	}
	
	public int getHits()	{	return hits;	}
	
	public void createRandomBoard()	{
		Random rand = new Random();
		int shape;		//1-horizontal,0-vertical
		int start;
		
		//*************4cells x 1 ship**********************
		shape  = VerOrHor(rand);
		if (shape == 0)	{					//range for vertical 4x ship is 0-69
			start=rand.nextInt(70);
			while (!checkAvailability(start) || !checkAvailability(start+30))	{		//checking if ship can be placed: 
				start=rand.nextInt(70);													//check availability for 1st and last piece --
			}																			//get another number if some of the pieces or neighbor cells are busy
			
			aircarrier = new Ship (0,4);
			for (int i=start;i<start+31;i+=10)	{										
				boardShips[i] = aircarrier;				//ship index --> 4 (ship size) + 0 (vertical) + 0,1,2,3 (celll num)
				aircarrier.setCoordinates(i);
			}
		}
		else	{
			int x=rand.nextInt(7);
			int y=rand.nextInt(10);
			start = x+y*10;					//range for horizontal 4x ship is 0-6 for x, 10-90 for y
			while (!checkAvailability(start) || !checkAvailability(start+3))	{		//check if ship can be placed
				x = rand.nextInt(7);
				y = rand.nextInt(10);
				start = x+y*10;
			}	
			aircarrier = new Ship (1,4);
			for (int i=start; i<start+4;i++)	{
				boardShips[i] = aircarrier;			//ship index --> 4 (ship size) + 1 (horizontal) + 0,1,2,3 (celll num)
				aircarrier.setCoordinates(i);
			}
		}	
		
		//*************3cells x 2 ships**********************
		for (int i=0; i<2; i++)	{			
			shape  = VerOrHor(rand);
			if (shape == 0)	{
				start=rand.nextInt(80);		//range for vertical 3x ship is 0-79
				while (!checkAvailability(start) || !checkAvailability(start+20))	{	//check if ship can be placed
					start=rand.nextInt(80);	
				}
				
				cruiser[i] = new Ship(0,3);
				for (int j=start;j<start+21;j+=10)	{
					boardShips[j] = cruiser[i];		//ship index --> 3 (ship size) + 0 (vertical) + 0,1,2(celll num)
					cruiser[i].setCoordinates(j);
				}	
			}
			else	{
				int x=rand.nextInt(8);
				int y=rand.nextInt(10);
				start = x+y*10;					//range for horizontal 3x ship is 0-7 for x, 10-90 for y				
				while (!checkAvailability(start) || !checkAvailability(start+2))	{		//check if ship can be placed
					x = rand.nextInt(8);
					y = rand.nextInt(10);
					start = x+y*10;
				}	
				
				cruiser[i] = new Ship(1,3);
				for (int j=start; j<start+3;j++)	{
					boardShips[j] = cruiser[i];			//ship index --> 3 (ship size) + 1 (horizontal) + 0,1,2 (celll num)
					cruiser[i].setCoordinates(j);
				}
			}
		}
		
		//*************2cells x 3 ships**********************
		for (int i=0; i<3; i++)	{			
			shape  = VerOrHor(rand);
			if (shape == 0)	{
				start=rand.nextInt(90);		//range for vertical 3x ship is 0-79
				while (!checkAvailability(start) || !checkAvailability(start+10))	{	//check if ship can be placed
					start=rand.nextInt(90);	
				}
				
				corvette[i] = new Ship(0,2);
				for (int j=start;j<start+11;j+=10)	{
					boardShips[j] = corvette[i];		//ship index --> 3 (ship size) + 0 (vertical) + 0,1,2(celll num)
					corvette[i].setCoordinates(j);
				}	
			}
			else	{
				int x=rand.nextInt(9);
				int y=rand.nextInt(10);
				start = x+y*10;					//range for horizontal 3x ship is 0-7 for x, 10-90 for y				
				while (!checkAvailability(start) || !checkAvailability(start+1))	{		//check if ship can be placed
					x = rand.nextInt(9);
					y = rand.nextInt(10);
					start = x+y*10;
				}	
				
				corvette[i] = new Ship(1,2);
				for (int j=start; j<start+2;j++)	{
					boardShips[j] = corvette[i];			//ship index --> 3 (ship size) + 1 (horizontal) + 0,1,2 (celll num)
					corvette[i].setCoordinates(j);
				}
			}
		}
		
		//*************1x4 ships**********************
				for (int i=0; i<4; i++)	{
					patrol_boat[i] = new Ship(1,1);
					start = rand.nextInt(99);
					while(!checkAvailability(start))	{	start = rand.nextInt(99);	}
					boardShips[start]=patrol_boat[i];					//ship index --> single square
					patrol_boat[i].setCoordinates(start);
				}
		
				
		
		
	}
	
	public void createFixedTestingBoard()	{
		
		hits = 0;
		aircarrier = new Ship (0,4);
		for (int i=15; i<46;i+=10)	{
			boardShips[i] = aircarrier;
			aircarrier.setCoordinates(i);
		}
		cruiser[0] = new Ship(0,3);
		cruiser[1] = new Ship(1,3);
		boardShips[65] = cruiser[0];
		cruiser[0].setCoordinates(65);
		boardShips[75] = cruiser[0];
		cruiser[0].setCoordinates(75);
		boardShips[85] = cruiser[0];
		cruiser[0].setCoordinates(85);
		
		boardShips[31] = cruiser[1];
		cruiser[1].setCoordinates(31);
		boardShips[32] = cruiser[1];
		cruiser[1].setCoordinates(32);
		boardShips[33] = cruiser[1];
		cruiser[1].setCoordinates(33);
		
		corvette[0] = new Ship(0,2);
		corvette[1] = new Ship(1,2);
		corvette[2] = new Ship(0,2);
		
		boardShips[0] = corvette[0];
		corvette[0].setCoordinates(0);
		boardShips[10] = corvette[0];	
		corvette[0].setCoordinates(10);
		
		boardShips[8] = corvette[1];
		corvette[1].setCoordinates(8);
		boardShips[9] = corvette[1];
		corvette[1].setCoordinates(9);
		
		boardShips[89] = corvette[2];
		corvette[2].setCoordinates(89);
		boardShips[99] = corvette[2];
		corvette[2].setCoordinates(99);
		
		
		patrol_boat[0] = new Ship(1,1);
		patrol_boat[1] = new Ship(1,1);
		patrol_boat[2] = new Ship(1,1);
		patrol_boat[3] = new Ship(1,1);
		
		
		boardShips[69] = patrol_boat[0];
		patrol_boat[0].setCoordinates(69);
		boardShips[38] = patrol_boat[1];
		patrol_boat[1].setCoordinates(38);
		boardShips[53] = patrol_boat[2];
		patrol_boat[2].setCoordinates(53);
		boardShips[71] = patrol_boat[3];	
		patrol_boat[3].setCoordinates(71);
		
	}
	
	public void addShip (int shipSize, int shipShape, int location)	{
		if (shipSize == 4)	{
			if (shipShape == 1)	{
				aircarrier = new Ship (1,4);
				for (int i=location; i<location+4; i++)	{
					boardShips[i] = aircarrier;
					aircarrier.setCoordinates(i);
				}
			}
			else	{
				aircarrier = new Ship (0,4);
				for (int i=location; i<location+40; i+=10)	{
					boardShips[i] = aircarrier;
					aircarrier.setCoordinates(i);
				}
			}			
		}
		
		if (shipSize == 3)	{
			if (shipShape == 1)	{
				cruiser[cruiserIndex] = new Ship (1,3);
				for (int i=location; i<location+3; i++)	{
					boardShips[i] = cruiser[cruiserIndex];
					cruiser[cruiserIndex].setCoordinates(i);
				}
			}
			else	{
				cruiser[cruiserIndex] = new Ship (0,3);
				for (int i=location; i<location+30; i+=10)	{
					boardShips[i] = cruiser[cruiserIndex];
					cruiser[cruiserIndex].setCoordinates(i);
				}
			}
			cruiserIndex++;
		}
		
		if (shipSize == 2)	{
			if (shipShape == 1)	{
				corvette[corvetteIndex] = new Ship (1,2);
				for (int i=location; i<location+2; i++)	{
					boardShips[i] = corvette[corvetteIndex];
					corvette[corvetteIndex].setCoordinates(i);
				}
			}
			else	{
				corvette[corvetteIndex] = new Ship (0,2);
				for (int i=location; i<location+20; i+=10)	{
					boardShips[i] = corvette[corvetteIndex];
					corvette[corvetteIndex].setCoordinates(i);
				}
			}	
			corvetteIndex++;
		}
		
		if (shipSize == 1)	{
				patrol_boat[patrolIndex] = new Ship (1,1);
				boardShips[location] = patrol_boat[patrolIndex];
				patrol_boat[patrolIndex].setCoordinates(location);
				patrolIndex++;
				}	
	}
	
	/**
	 * 
	 * returns 1 or 0... 1 for horizontal ship, 0 for vertical
	 */
	private int VerOrHor (Random figure)	{
		Random rand = figure;
		return rand.nextInt(2);
	}
	
	public boolean checkAvailability (int index)	{
		if (boardShips[index] != null)	return false;		
		
		//checking neighboring cells of potential ship piece
		//cells with 3neighbours
		if (index == 0)	{		//top left (0)
			if ((boardShips[index+1] != null) ||(boardShips[index+10] != null) || (boardShips[index+11] != null))	return false;
			else	return true;
		}
		if (index == 9)	{	//top right (9)
			if ((boardShips[index-1] != null) || (boardShips[index+10] != null) || (boardShips[index+9] != null))		return false;
			else	return true;
		}
		if (index == 90)	{	//bottom left (90)
			if ((boardShips[index-10] != null) || (boardShips[index+1] != null) || (boardShips[index-9] != null))		return false;
			else	return true;
		}
		if (index == 99)	{	//bottom right (99)
			if ((boardShips[index-10] != null) || (boardShips[index-11] != null) || (boardShips[index-1] != null))		return false;
			else	return true;
		}
		
		//cells with 5neighbors
		if (index > 0 && index < 9)	{		//top row of cells (1-8)
			if ((boardShips[index+1] != null) ||(boardShips[index-1] != null) || (boardShips[index+9]!= null) || 
					(boardShips[index+10]!= null) || (boardShips[index+11]!= null))
				return false;
			else	return true;
		}
		if (index >90 && index < 99)	{	//bottom row of cells (91-98)
			if ((boardShips[index+1] != null) ||(boardShips[index-1]!= null) || (boardShips[index-9]!= null) || (boardShips[index-10]!= null) 
					|| (boardShips[index-11]!= null))
				return false;
			else	return true;
		}
		if (index > 0 && index < 90 && (index%10 == 0))	{	//left column of cells (10,20,..,80)
			if ((boardShips[index+1] != null) ||(boardShips[index-10] != null) || (boardShips[index-9]!= null) ||
					(boardShips[index+10]!= null) || (boardShips[index+11]!= null))
				return false;	
			else	return true;
		}
		if (index > 9 && index < 99 && (index%10 == 9))	{	//right column of cells (19,29,...,89)
			if ((boardShips[index-1] != null) ||(boardShips[index-10]!= null) || (boardShips[index-11]!= null) || 
					(boardShips[index+10]!= null) || (boardShips[index+9]!= null))
				return false;
			else	return true;
		}
		
		//cells with 8neighbors
		else	{
			if ((boardShips[index+1] != null) || (boardShips[index-1]!= null) || (boardShips[index+9]!= null) || (boardShips[index+10]!= null) ||
					(boardShips[index+11]!= null) || (boardShips[index-9]!= null) || (boardShips[index-10]!= null) 
					|| (boardShips[index-11]!= null))	return false;		}
		return true;
	}
	
	
	public int setMove (int location)	{
		//if hit
		if (boardShips[location] != null)	{
			hits++;
			Log.e("ComputerHits", Integer.toString(1));
			boardShips[location].shipHit(location);
			return 1;
		}
		else	return 0;
	}
	
	
	public int checkForWinner()	{
		if (aircarrier.shipStatus() == 1 && cruiser[0].shipStatus() == 1 && cruiser[1].shipStatus() == 1 && corvette[0].shipStatus() == 1
				&& corvette[1].shipStatus() == 1 && corvette[2].shipStatus() == 1 && patrol_boat[0].shipStatus() == 1
				&& patrol_boat[1].shipStatus() == 1 && patrol_boat[2].shipStatus() == 1 && patrol_boat[3].shipStatus() == 1 )
			return 1;
		else	return 0;
	}
	
	public Ship getState(int index)	{	return	boardShips[index];	} 
	
	public int giveIndex ()	{
		return patrolIndex;
	}
	
	public Ship getLatestAdded (int size)	{
		if (size==4)	return aircarrier;
		else if (size == 3)	return cruiser[cruiserIndex-1];
		else if (size == 2)	return corvette[corvetteIndex-1];
		else	return patrol_boat[patrolIndex-1];
	}
	
	public Ship getShip (int index)		{		return boardShips[index];	}
	
}
