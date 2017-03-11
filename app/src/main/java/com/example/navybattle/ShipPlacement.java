package com.example.navybattle;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;

public class ShipPlacement extends Activity {
	
	private ImageButton aBoardButtons[];
	private TableLayout boardPlayer;
//	private TableRow boardPlayerRow;
	private ImageView aircarrier, reverse; 				  //четерехпалубный и стрелка?
	private ImageView [] cruiser = new ImageView[2];      //трехпалубный
	private ImageView [] corvette = new ImageView[3];    //двухпалубный
	private ImageView [] patrol_boat = new ImageView[4]; //однопалубный
	private ImageButton start;
	private ArrayList <Integer> neighbors;
	private ArrayList <Integer> ships;
	public int shipSize, visibility, shape, num;
	public GameBoard playerBoard;
	public int counter;
	public MediaPlayer mpPlacement;
	public String sound;
	private GlobalSettingsVariables settings;
	String cUser;
	TextView easywin,easyloose,normalwin,normalloose,hardwin,hardloose,veryhardwin,veryhardloose;
	LinearLayout statistc_lay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ship_placement);



		playerBoard = new GameBoard(100);
		int k = 0;
		
		mpPlacement = MediaPlayer.create(this, R.raw.shipbell);
		settings = ((GlobalSettingsVariables)(getApplicationContext()));
		sound = settings.getSound();
		Log.i("Sound", sound);
		counter = 0;
		visibility = 0;
		shape = 1;
		num = 0;
		neighbors = new ArrayList<Integer>();
		ships = new ArrayList<Integer>();
		aBoardButtons = new ImageButton[100];
		boardPlayer = (TableLayout)findViewById(R.id.tableLayout1);
		aircarrier = (ImageView)findViewById(R.id.imageAircarrier);
		cruiser[0] = (ImageView)findViewById(R.id.imageCruiser1);
		cruiser[1] = (ImageView)findViewById(R.id.ImageCruiser2);
		corvette[0] = (ImageView)findViewById(R.id.imageCorvette1);
		corvette[1] = (ImageView)findViewById(R.id.imageCorvette2);
		corvette[2] = (ImageView)findViewById(R.id.imageCorvette3);
		patrol_boat[0] = (ImageView)findViewById(R.id.imagePatrol1);
		patrol_boat[1] = (ImageView)findViewById(R.id.imagePatrol2);
		patrol_boat[2] = (ImageView)findViewById(R.id.imagePatrol3);
		patrol_boat[3] = (ImageView)findViewById(R.id.imagePatrol4);
		reverse = (ImageView)findViewById(R.id.imageReverse);
		start = (ImageButton)findViewById(R.id.imageButtonStart);
		easywin = (TextView)findViewById(R.id.easywin);
		easyloose = (TextView)findViewById(R.id.easyloose);
		normalwin = (TextView)findViewById(R.id.normalwin);
		normalloose = (TextView)findViewById(R.id.normalloose);
		hardwin = (TextView)findViewById(R.id.hardwin);
		hardloose = (TextView)findViewById(R.id.hardloose);
		veryhardwin = (TextView)findViewById(R.id.veryhardwin);
		veryhardloose = (TextView)findViewById(R.id.veryhardloose);
		statistc_lay = (LinearLayout)findViewById(R.id.statistc_lay);

		//create dynamic boards
		for (int i=0; i<10; i++)	{
			TableRow tR = new TableRow(this);
			for (int j=0; j<10; j++)	{			
				aBoardButtons[k] = new ImageButton(this);
				aBoardButtons[k].setImageResource(R.drawable.celll);
				aBoardButtons[k].setBackgroundDrawable(null);
				aBoardButtons[k].setPadding(0,0,0,0);
				aBoardButtons[k].setEnabled(false);
			
					
				tR.addView(aBoardButtons[k]);
				k++;

			}
			boardPlayer.addView(tR);
			}
				
		boardCreation();

	}
	
	
	private void boardCreation()	{
		aircarrier.setOnClickListener(new ShipOnClickListener(4,0));
		cruiser[0].setOnClickListener(new ShipOnClickListener(3,0));
		cruiser[1].setOnClickListener(new ShipOnClickListener(3,1));
		corvette[0].setOnClickListener(new ShipOnClickListener(2,0));
		corvette[1].setOnClickListener(new ShipOnClickListener(2,1));
		corvette[2].setOnClickListener(new ShipOnClickListener(2,2));
		for (int i=0; i<4; i++)		patrol_boat[i].setOnClickListener(new ShipOnClickListener(1,i)); 
	}
	
	
	@SuppressLint("UseValueOf")
	private void displayAvailableCells (){
		//horizontal ships
		if (shape == 1)	{   //проверка можно ли поместить корабыль в эту клетку
				for (int i=0; i<11-shipSize; i++)	{
					for (int j=0; j<10; j++)	{
						if ((playerBoard.checkAvailability(i+j*10)) && (playerBoard.checkAvailability(i+j*10+(shipSize-1))))	{
							aBoardButtons[i+j*10].setImageResource(R.drawable.cell_available);
							aBoardButtons[i+j*10].setEnabled(true);
							aBoardButtons[i+j*10].setOnClickListener(new BoardOnClickListener(i+j*10));
						}
						else	{
							if (!ships.contains(new Integer(i+j*10)))	{
							aBoardButtons[i+j*10].setImageResource(R.drawable.cell_not_available);
							aBoardButtons[i+j*10].setEnabled(false);
							}
						}
					}
				}
				for (int i=11-shipSize; i<10; i++)	{
					for (int j=0; j<10; j++)	{
						if (!ships.contains(new Integer(i+j*10)))	{
							aBoardButtons[i+j*10].setImageResource(R.drawable.cell_not_available);
							aBoardButtons[i+j*10].setEnabled(false);
							aBoardButtons[i+j*10].setOnClickListener(new BoardOnClickListener(i+j*10));
						}
					}
				}
			}
		else	{
				for (int i=0; i <10; i++)	{
					for (int j=0; j<11-shipSize; j++)	{
						if ((playerBoard.checkAvailability(i+j*10)) && (playerBoard.checkAvailability(i+j*10+(shipSize*10-10))))	{
							aBoardButtons[i+j*10].setImageResource(R.drawable.cell_available);
							aBoardButtons[i+j*10].setEnabled(true);
							aBoardButtons[i+j*10].setOnClickListener(new BoardOnClickListener(i+j*10));
						}
						else	{
							if (!ships.contains(new Integer(i+j*10)))	{
								aBoardButtons[i+j*10].setImageResource(R.drawable.cell_not_available);
								aBoardButtons[i+j*10].setEnabled(false);
								aBoardButtons[i+j*10].setOnClickListener(new BoardOnClickListener(i+j*10));
							}
						}
					}
				}
				for (int i=0; i<10; i++)	{
					for (int j=11-shipSize; j<10; j++)	{
						if (!ships.contains(new Integer(i+j*10)))	{
							aBoardButtons[i+j*10].setImageResource(R.drawable.cell_not_available);
							aBoardButtons[i+j*10].setEnabled(false);
							aBoardButtons[i+j*10].setOnClickListener(new BoardOnClickListener(i+j*10));
						}
					}
				}
			}		
		}	




	private class ShipOnClickListener implements View.OnClickListener	{
		int size, index;
		
		public ShipOnClickListener(int size, int n)	{
			this.size = size;
			index = n;
		}
				
		@Override
		public void onClick(View v) {   //при нажатии на корабыль

			reverse.setVisibility(View.VISIBLE); //показать стрелку
			shipSize = size;  //присвоить размер коробля
			num = index;  //индекс коробля этого типа
			visibility=1;
			displayAvailableCells(); //обозначить свободные клетки
			
	}
	}
		
	
	
	class BoardOnClickListener implements View.OnClickListener	{
		private int location;
		
		
		public BoardOnClickListener(int location)	{
			this.location = location;
		}

		@SuppressLint("UseValueOf")
		@Override
		public void onClick(View v) {
			if (aBoardButtons[location].isEnabled())	{  //если нажата эта клетка
				if (shipSize == 4 && shape == 1)	{  //если корабыль занимает 4 клетки и
					playerBoard.addShip(shipSize, 1, location);  //поставить на поле корабыль этого размера  shape 1  на ту клетку
					aBoardButtons[location].setImageResource(R.drawable.aircarrier_1_1);   //
					aBoardButtons[location+1].setImageResource(R.drawable.aircarrier_1_2); // нарисовать на 4 клетках корабыль
					aBoardButtons[location+2].setImageResource(R.drawable.aircarrier_1_3); //
					aBoardButtons[location+3].setImageResource(R.drawable.aircarrier_1_4); //
					ships.add(location); ships.add(location+1); ships.add(location+2); ships.add(location+3);
					neighbors.addAll(playerBoard.aircarrier.getNeighbors());  //заполнить клетки вокруг коробля
					lockBoard();
					aircarrier.setVisibility(View.INVISIBLE);
					shape = 1;
					counter++;
					reverse.setVisibility(View.INVISIBLE);
				}
				else if (shipSize == 4 && shape == 0)	{
					playerBoard.addShip(shipSize, 0, location);
					aBoardButtons[location].setImageResource(R.drawable.aircarrier_0_1);
					aBoardButtons[location+10].setImageResource(R.drawable.aircarrier_0_2);
					aBoardButtons[location+20].setImageResource(R.drawable.aircarrier_0_3);
					aBoardButtons[location+30].setImageResource(R.drawable.aircarrier_0_4);
					ships.add(location); ships.add(location+10); ships.add(location+20); ships.add(location+30);
					neighbors.addAll(playerBoard.aircarrier.getNeighbors());
					lockBoard();
					counter++;
					shape = 1;
					aircarrier.setVisibility(View.INVISIBLE);
					reverse.setVisibility(View.INVISIBLE);
				}
				else if (shipSize == 3 && shape == 0)	{
					playerBoard.addShip(shipSize, 0, location);
					aBoardButtons[location].setImageResource(R.drawable.cruiser_split_0_1);
					aBoardButtons[location+10].setImageResource(R.drawable.cruiser_split_0_2);
					aBoardButtons[location+20].setImageResource(R.drawable.cruiser_split_0_3);
					ships.add(location); ships.add(location+10); ships.add(location+20);
					neighbors.addAll(playerBoard.getLatestAdded(3).getNeighbors());
					shape = 1;
					lockBoard();
					counter++;
					cruiser[num].setVisibility(View.INVISIBLE);
					reverse.setVisibility(View.INVISIBLE);					
				}
				else if (shipSize == 3 && shape == 1)	{
					playerBoard.addShip(shipSize, 1, location);
					aBoardButtons[location].setImageResource(R.drawable.cruiser_split_1_1);
					aBoardButtons[location+1].setImageResource(R.drawable.cruiser_split_1_2);
					aBoardButtons[location+2].setImageResource(R.drawable.cruiser_split_1_3);
					ships.add(location); ships.add(location+1); ships.add(location+2);
					neighbors.addAll(playerBoard.getLatestAdded(3).getNeighbors());
					shape = 1;
					lockBoard();
					counter++;
					cruiser[num].setVisibility(View.INVISIBLE);
					reverse.setVisibility(View.INVISIBLE);
				}
				else if (shipSize == 2 && shape == 0)	{
					playerBoard.addShip(shipSize, 0, location);
					aBoardButtons[location].setImageResource(R.drawable.corvette_split_0_1);
					aBoardButtons[location+10].setImageResource(R.drawable.corvette_split_0_2);
					ships.add(location); ships.add(location+10);
					neighbors.addAll(playerBoard.getLatestAdded(2).getNeighbors());
					shape = 1;
					lockBoard();
					counter++;
					corvette[num].setVisibility(View.INVISIBLE);
					reverse.setVisibility(View.INVISIBLE);
				}
				else if (shipSize == 2 && shape == 1)	{
					playerBoard.addShip(shipSize, 1, location);
					aBoardButtons[location].setImageResource(R.drawable.corvette_split_1_1);
					aBoardButtons[location+1].setImageResource(R.drawable.corvette_split_1_2);
					ships.add(location); ships.add(location+1);
					neighbors.addAll(playerBoard.getLatestAdded(2).getNeighbors());
					shape = 1;
					lockBoard();
					counter++;
					corvette[num].setVisibility(View.INVISIBLE);
					reverse.setVisibility(View.INVISIBLE);
				}
				else	{
					playerBoard.addShip(shipSize, 1, location);
					aBoardButtons[location].setImageResource(R.drawable.patrol);
					ships.add(location);
					neighbors.addAll(playerBoard.getLatestAdded(1).getNeighbors());
					shape = 1;
					lockBoard();
					counter++;
					patrol_boat[num].setVisibility(View.INVISIBLE);
					reverse.setVisibility(View.INVISIBLE);
				}
			}
			else	{
				Toast.makeText(getApplicationContext(), "You can't place this ship there!", Toast.LENGTH_LONG).show();			}
		}
	}
	
	
	
	
	@SuppressLint("UseValueOf")
	public void resetAvailability (boolean stage)	{
		for (int i=0; i<100; i++)	{
			if (neighbors.contains(new Integer(i)))	{
				if (ships.contains(new Integer(i)))	aBoardButtons[i].setEnabled(false);
				aBoardButtons[i].setImageResource(R.drawable.cell_not_available);
				aBoardButtons[i].setEnabled(false);
			}
			else if (ships.contains(new Integer(i)))	aBoardButtons[i].setEnabled(false);
			else	{
				aBoardButtons[i].setImageResource(R.drawable.cell_available);
				aBoardButtons[i].setEnabled(true);
			}
			
		}
	}
	
	public void reverseShip (View view)	{
		if (visibility == 1)	{
			if (shape == 1)	{	
				shape = 0;
				displayAvailableCells();
			}
			else	{
				shape = 1;
				displayAvailableCells();
			}
		}
	}
	
	
	@SuppressLint("UseValueOf")
	public void lockBoard()	{
		if (counter == 9)	{
			start.setVisibility(View.VISIBLE);
			if (sound == "On")	mpPlacement.start();
		}
		for (int i=0; i<100; i++)	{
			aBoardButtons[i].setEnabled(false);
			 if (!ships.contains(new Integer(i)))	{
				 if (neighbors.contains(new Integer(i)))	aBoardButtons[i].setImageResource(R.drawable.cell_not_available);
				 else										 aBoardButtons[i].setImageResource(R.drawable.cell_available); 
			 }
		}
	}
	
	
	public void startGame (View View)	{
		Intent newGame = new Intent(this, AgainstComputer.class);
		newGame.putExtra("Board", playerBoard);
		Hawk.put("ships",ships);
		startActivity(newGame);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ship_placement, menu);
		return true;
	}




}
