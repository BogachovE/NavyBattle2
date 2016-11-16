package com.example.navybattle;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;

public class AgainstComputer extends Activity {

	private GameBoard cpu_board, player_board;
	
	private ImageButton aBoardButtons[], playerBoardButtons[];
	private TextView aMsgArea, playerTurn, textLvl;
	private String p1_name = "Player1";
	private TableLayout boardPlayer, boardComputer;
	private TableRow boardPlayerRow, boardComputerRow;
	private ComputerEasy easy;
	private ComputerNormal normal;
	private ComputerHard hard;
	private ComputerVeryHard veryhard;
	private int k=0, turn=0, shipHitCoordinate;		//turn 0 - player turn, 1 - computer turn
	private GlobalSettingsVariables settings;
	private String difficulty, vibration, sound;
	private Vibrator v;
	private MediaPlayer mpHit, mpSunk, mpMiss, mpUserWin, mpCompWin;
	private ArrayList<Integer> fromBD;
	

	String player1;
	
	
	private boolean aGameOver = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_against_computer);		
		
		//nameInput();
		
		aBoardButtons = new ImageButton[100];
		boardComputer = (TableLayout)findViewById(R.id.boardComputer);
		boardComputerRow = (TableRow)findViewById(R.id.tableRow1);

		Hawk.init(this).build();
		fromBD=Hawk.get("fromBD");
		
		//create dynamic boards
		for (int i=0; i<10; i++)	{
			TableRow tR = new TableRow(this);
			for (int j=0; j<10; j++)	{			
			aBoardButtons[k] = new ImageButton(this);
			aBoardButtons[k].setImageResource(R.drawable.cell);
			aBoardButtons[k].setBackgroundDrawable(null);
			aBoardButtons[k].setPadding(0,0,0,0);
			
			tR.addView(aBoardButtons[k]);
			k++;

			}			
			boardComputer.addView(tR);
		}
		
		k=0;
		
		boardPlayer = (TableLayout)findViewById(R.id.boardPlayer);
		boardPlayerRow = (TableRow)findViewById(R.id.tableRow11);
		playerBoardButtons = new ImageButton[100];
		
		for (int i=0; i<10; i++)	{
			TableRow tR = new TableRow(this);
			for (int j=0; j<10; j++)	{			
			playerBoardButtons[k] = new ImageButton(this);
			playerBoardButtons[k].setImageResource(R.drawable.cell);
			playerBoardButtons[k].setBackgroundDrawable(null);
			playerBoardButtons[k].setPadding(0,0,0,0);
			playerBoardButtons[k].setEnabled(false);
			
			tR.addView(playerBoardButtons[k]);
			k++;
			}			
			boardPlayer.addView(tR);
		}
		
		k=0;
		
		aMsgArea = (TextView)findViewById(R.id.msgarea);
		textLvl = (TextView)findViewById(R.id.textLvl);
		
		
		cpu_board = new GameBoard(100);
		
		
		Intent intent = getIntent();
		
		v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		mpHit = MediaPlayer.create(this, R.raw.hit);
		mpMiss = MediaPlayer.create(this, R.raw.miss);
		mpSunk = MediaPlayer.create(this, R.raw.sunk);
		mpCompWin = MediaPlayer.create(this, R.raw.computer_win);
		mpUserWin = MediaPlayer.create(this, R.raw.user_win);
		player_board = new GameBoard();
		player_board = (GameBoard)intent.getSerializableExtra("Board");
		settings = ((GlobalSettingsVariables)(getApplicationContext()));
		sound = settings.getSound();
		vibration = settings.getVibration();
		difficulty = settings.getDifficulty();
		if (difficulty == "Easy")	easy = new ComputerEasy();
		else if (difficulty == "Normal")	normal = new ComputerNormal();
		else if(difficulty == "Hard")	hard = new ComputerHard();
		else veryhard = new ComputerVeryHard(fromBD);
		
		
		
		startNewGame();
		
	}

	
	public void startNewGame()	{
		aMsgArea.setText("Let's start this game!");
		textLvl.setText(difficulty);
		aGameOver = false;
		cpu_board.clearBoards();
		cpu_board.createRandomBoard();	

		
		if (difficulty == "Easy")	 easy.newGame();
		else if (difficulty == "Normal")	normal.newGame();
		else if(difficulty == "Hard") hard.newGame();
		else veryhard.newGame();
		
		shipHitCoordinate = -1;
		
		for (int i=0; i<aBoardButtons.length;i++)	{
			aBoardButtons[i].setEnabled(true);
			aBoardButtons[i].setImageResource(R.drawable.cell);
			aBoardButtons[i].setOnClickListener(new ImageButtonClickListener(i));
		}
		
		for (int i=0; i<playerBoardButtons.length;i++)	{
			if (player_board.getState(i) != null)		createCellShip(player_board.getState(i),i);
			else	playerBoardButtons[i].setImageResource(R.drawable.cell);
		}
		
		
		
	}
	
	public void showBoard()	{
		for (int i=0; i<aBoardButtons.length;i++)	{
			if (cpu_board.getState(i)!=null)			
				aBoardButtons[i].setImageResource(R.drawable.cell_hit);			
			}
	}
	
	private void setMove (int location) {                  //Ход игрока по полю компьютера
		ArrayList<Integer> deadCells = new ArrayList<Integer>();
		int move = cpu_board.setMove(location);
		aBoardButtons[location].setEnabled(false);
		

		if (move==0)	{
			aBoardButtons[location].setImageResource(R.drawable.cell_miss);
			if (sound == "On")	mpMiss.start();
			aMsgArea.setText("You missed!");
			aBoardButtons[location].setEnabled(false);
			turn = 1;
		}
		else	{
			aBoardButtons[location].setImageResource(R.drawable.cell_hit);
			if (cpu_board.getState(location).shipStatus() == 1)	{			//if ship was sunk, deactivate it's neighbors
				aMsgArea.setText("Ship was sunk!");	
				aBoardButtons[location].setEnabled(false);
				if (vibration == "On")	v.vibrate(600);
				if (sound == "On")	mpSunk.start();
				sunkShip(cpu_board.getShip(location),aBoardButtons);
				deadCells = cpu_board.getState(location).getNeighbors();
				for (int i=0; i<deadCells.size();i++)	{
					if (aBoardButtons[deadCells.get(i)].isEnabled())
						aBoardButtons[deadCells.get(i)].setImageResource(R.drawable.cell_miss);
					aBoardButtons[deadCells.get(i)].setEnabled(false);
				}
				
			}
			else	{
				aMsgArea.setText("You hit! Shoot again!");
				if (vibration == "On")	v.vibrate(300);
				if (sound == "On")	mpHit.start();
				aBoardButtons[location].setEnabled(false);
			}
			turn = 0;
		}
		if (turn == 1)		computerMove();
	}
	
	private void computerMove () 	{			//normal level
		lockBoard();
		if (difficulty == "Easy")	{
			computerMoveEasy();
			return;			
		}
		else	computerMoveAdvanced(difficulty);
		
	}
	
	

	private void computerMoveEasy()	{
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
		int move = easy.getMove();
			
			public void run() {
				ArrayList<Integer> deadCells = new ArrayList<Integer>();
				if (player_board.setMove(move) == 1)	{	
						if (vibration == "On")	v.vibrate(300);
						if (sound == "On")	mpHit.start();
						int winnerCpu = player_board.checkForWinner();
						if (winnerCpu == 1)	{
							if (sound == "On")	mpCompWin.start();
							aMsgArea.setText("Game over!!! Computer WON!");
							aGameOver=true;
						}
						playerBoardButtons[move].setImageResource(R.drawable.cell_hit);
						if (player_board.getState(move).shipStatus() == 1)	{			//if ship was sunk, deactivate it's neighbors
							if (vibration == "On")	v.vibrate(600);
							if (sound == "On")	mpSunk.start();
							sunkShip(player_board.getShip(move),playerBoardButtons);	// fill cells of sunk ship with proper images
							deadCells = player_board.getState(move).getNeighbors();
							easy.getDeadCells(deadCells);
							for (int i=0; i<deadCells.size();i++)	{
									playerBoardButtons[deadCells.get(i)].setImageResource(R.drawable.cell_miss);
							}
						}
						computerMoveEasy();
					}
				else	{
					if (!aGameOver)				{
							playerBoardButtons[move].setImageResource(R.drawable.cell_miss);
							turn = 0;
							unlockBoard();
						}
					}
				}
		} , 1000);		
	}
		

	private void computerMoveAdvanced(final String level)	{
		final Timer t = new Timer();
		t.scheduleAtFixedRate( new TimerTask() {
			
			public void run() {
				
				runOnUiThread (new Runnable()	{
					
				public void run()	{
					
					if (aGameOver)	{
						t.cancel();
						return;
					}
					ArrayList<Integer> deadCells = new ArrayList<Integer>();
					int move;
					if (level == "Normal")	{
						if (shipHitCoordinate == -1)		move = normal.getMove();
						else								move = normal.getMove(shipHitCoordinate);
					}
					else if(level == "VeryHard")	{
						if (shipHitCoordinate == -1)		move = veryhard.getMove();
						else								move = veryhard.getMove(shipHitCoordinate);
					}
					else	{
						if (shipHitCoordinate == -1)		move = hard.getMove();
						else								move = hard.getMove(shipHitCoordinate);
					}


					
					if (player_board.setMove(move) != 0)	{	
						shipHitCoordinate = move;
						int winnerCpu = player_board.checkForWinner();
						if (winnerCpu == 1)	{
							aMsgArea.setText("Game over!! Computer WON!");
							if (sound == "On")	mpCompWin.start();
							playerBoardButtons[move].setImageResource(R.drawable.cell_hit);	
							aGameOver=true;
						}
						playerBoardButtons[move].setImageResource(R.drawable.cell_hit);			
						if (player_board.getState(move).shipStatus() == 1)	{			//if ship was sunk, deactivate it's neighbors
							if (vibration == "On")	v.vibrate(600);
							if (sound == "On")	mpSunk.start();
							sunkShip(player_board.getShip(move),playerBoardButtons);	// fill cells of sunk ship with proper images
							deadCells = player_board.getState(move).getNeighbors();
							if (level == "Normal")	normal.getDeadCells(deadCells);
							if (level == "VeryHard") veryhard.getDeadCells(deadCells);
							else	hard.getDeadCells(deadCells);
							for (int i=0; i<deadCells.size();i++)	{
									playerBoardButtons[deadCells.get(i)].setImageResource(R.drawable.cell_miss);
							}
							shipHitCoordinate = -1;
						}
						else	if (sound == "On")	mpHit.start();
					}
					else	{
						if (!aGameOver)				{
								playerBoardButtons[move].setImageResource(R.drawable.cell_miss);
								turn = 0;
								t.cancel();
								unlockBoard();
							}
						else	t.cancel();	
					}
				}
				});
			}
		} , 1500,1500);
	}
	
		
		private void ComputerMoveHard()	{
			Handler mHandler = new Handler();
			mHandler.postDelayed(new Runnable() {
				
				public void run() {
						ArrayList<Integer> deadCells = new ArrayList<Integer>();
						int move;
						if (shipHitCoordinate == -1)		move = hard.getMove();
						else								move = shipHitCoordinate;
						
						while (player_board.setMove(move) != 0)	{
							shipHitCoordinate = move;
							int winnerCpu = player_board.checkForWinner();
							if (winnerCpu == 1)	{
								aMsgArea.setText("Game over!! Computer WON!");
								if (sound == "On")	mpCompWin.start();
								playerBoardButtons[move].setImageResource(R.drawable.cell_hit);	
								aGameOver=true;
								break;
							}
							playerBoardButtons[move].setImageResource(R.drawable.cell_hit);			
							if (player_board.getState(move).shipStatus() == 1)	{			//if ship was sunk, deactivate it's neighbors
								if (vibration == "On")	v.vibrate(600);
								sunkShip(player_board.getShip(move),playerBoardButtons);	// fill cells of sunk ship with proper images
								deadCells = player_board.getState(move).getNeighbors();
								hard.getDeadCells(deadCells);
								for (int i=0; i<deadCells.size();i++)	{
										playerBoardButtons[deadCells.get(i)].setImageResource(R.drawable.cell_miss);
								}
								move = hard.getMove();
								shipHitCoordinate = -1;
							}
							else			move = hard.getMove(move);		//ship hit but not sunk		
						}
						if (!aGameOver)				{
							playerBoardButtons[move].setImageResource(R.drawable.cell_miss);
							turn = 0;
					}
			}
			} , 1000);
		}

	private void ComputerMoveVeryHard()	{
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {

			public void run() {
				ArrayList<Integer> deadCells = new ArrayList<Integer>();
				int move;
				if (shipHitCoordinate == -1)		move = veryhard.getMove();
				else								move = shipHitCoordinate;

				while (player_board.setMove(move) != 0)	{
					shipHitCoordinate = move;
					int winnerCpu = player_board.checkForWinner();
					if (winnerCpu == 1)	{                                //Если компьютер выйграл
						aMsgArea.setText("Game over!! Computer WON!");
						if (sound == "On")	mpCompWin.start();
						playerBoardButtons[move].setImageResource(R.drawable.cell_hit);
						aGameOver=true;
						break;
					}
					playerBoardButtons[move].setImageResource(R.drawable.cell_hit);
					if (player_board.getState(move).shipStatus() == 1)	{			//if ship was sunk, deactivate it's neighbors
						if (vibration == "On")	v.vibrate(600);
						sunkShip(player_board.getShip(move),playerBoardButtons);	// fill cells of sunk ship with proper images
						deadCells = player_board.getState(move).getNeighbors();
						veryhard.getDeadCells(deadCells);
						for (int i=0; i<deadCells.size();i++)	{
							playerBoardButtons[deadCells.get(i)].setImageResource(R.drawable.cell_miss);
						}
						move = veryhard.getMove();
						shipHitCoordinate = -1;
					}
					else			move = veryhard.getMove(move);		//ship hit but not sunk
				}
				if (!aGameOver)				{
					playerBoardButtons[move].setImageResource(R.drawable.cell_miss);
					turn = 0;
				}
			}
		} , 1000);
	}

	private void createCellShip (Ship ship, int index)	{
		if (ship.shipSize() == 1)	playerBoardButtons[index].setImageResource(R.drawable.patrol);
		else if (ship.shipSize() == 2)	{
			if (ship.shipType() == 0)	{
				if (index == ship.getCoordinates()[0])	playerBoardButtons[index].setImageResource(R.drawable.corvette_split_0_1);
				else	playerBoardButtons[index].setImageResource(R.drawable.corvette_split_0_2);
			}
			else	{
				if (index == ship.getCoordinates()[0])	playerBoardButtons[index].setImageResource(R.drawable.corvette_split_1_1);
				else	playerBoardButtons[index].setImageResource(R.drawable.corvette_split_1_2);
			}
		}
		else if (ship.shipSize() == 3)	{
			if (ship.shipType() == 0)	{
				if (index == ship.getCoordinates()[0])	playerBoardButtons[index].setImageResource(R.drawable.cruiser_split_0_1);
				else if (index == ship.getCoordinates()[1])	playerBoardButtons[index].setImageResource(R.drawable.cruiser_split_0_2);
				else	playerBoardButtons[index].setImageResource(R.drawable.cruiser_split_0_3);
			}
			else	{
				if (index == ship.getCoordinates()[0])	playerBoardButtons[index].setImageResource(R.drawable.cruiser_split_1_1);
				else if (index == ship.getCoordinates()[1])	playerBoardButtons[index].setImageResource(R.drawable.cruiser_split_1_2);
				else	playerBoardButtons[index].setImageResource(R.drawable.cruiser_split_1_3);	
				}
		}
		else	{
			if (ship.shipType() == 0)	{
				if (index == ship.getCoordinates()[0])	playerBoardButtons[index].setImageResource(R.drawable.aircarrier_0_1);
				else if (index == ship.getCoordinates()[1])	playerBoardButtons[index].setImageResource(R.drawable.aircarrier_0_2);
				else if (index == ship.getCoordinates()[2])	playerBoardButtons[index].setImageResource(R.drawable.aircarrier_0_3);
				else	playerBoardButtons[index].setImageResource(R.drawable.aircarrier_0_4);
			}
			else	{
				if (index == ship.getCoordinates()[0])	playerBoardButtons[index].setImageResource(R.drawable.aircarrier_1_1);
				else if (index == ship.getCoordinates()[1])	playerBoardButtons[index].setImageResource(R.drawable.aircarrier_1_2);
				else if (index == ship.getCoordinates()[2])	playerBoardButtons[index].setImageResource(R.drawable.aircarrier_1_3);
				else	playerBoardButtons[index].setImageResource(R.drawable.aircarrier_1_4);
			}	
			
		}
	}
	

	private class ImageButtonClickListener implements View.OnClickListener
	{
		int location;
		
		public ImageButtonClickListener(int location)	
		{
			this.location = location;
		}
		
		public void onClick (View view)	{
			if (!aGameOver)	{
				if (aBoardButtons[location].isEnabled()==true)	{
					setMove(location);			
					
					int winnerPlayer = cpu_board.checkForWinner();			
					if (winnerPlayer == 1)	{
						aMsgArea.setText("Game over!!! You WON!");
						if (sound == "On")	mpUserWin.start();
						aGameOver=true;
					}					
				}
			}
		}
	}

	
	/**
	 * Function that gets Ship and Board on ImageButtons to fill cells for ship that has been sunk
	 * @param ship
	 * @param ib
	 */
	
	private void sunkShip (Ship ship, ImageButton [] ib)	{
		int coordinates[] = new int[4];
		coordinates = ship.getCoordinates();
		if (ship.shape == 0)	{					//change images to _VERTICAL_ ship that has been sunk
			if (ship.size == 4)	{
				ib[coordinates[0]-110].setImageResource(R.drawable.aircarrier_sunk_0_1);		
				ib[coordinates[1]-110].setImageResource(R.drawable.aircarrier_sunk_0_2);	
				ib[coordinates[2]-110].setImageResource(R.drawable.aircarrier_sunk_0_3);	
				ib[coordinates[3]-110].setImageResource(R.drawable.aircarrier_sunk_0_4);	}
			else if (ship.size == 3)	{
				ib[coordinates[0]-110].setImageResource(R.drawable.cruiser_sunk_0_1);		
				ib[coordinates[1]-110].setImageResource(R.drawable.cruiser_sunk_0_2);	
				ib[coordinates[2]-110].setImageResource(R.drawable.cruiser_sunk_0_3);	
			}
			else if (ship.size == 2)	{
				ib[coordinates[0]-110].setImageResource(R.drawable.corvette_sunk_0_1);		
				ib[coordinates[1]-110].setImageResource(R.drawable.corvette_sunk_0_2);	
			}
			else	ib[coordinates[0]-110].setImageResource(R.drawable.patrolboat_sunk_0_1);
		}
		else	{					//change images to _HORIZONTALL_ ship that has been sunk
			if (ship.size == 4)	{
				ib[coordinates[0]-110].setImageResource(R.drawable.aircarrier_sunk_1_1);		
				ib[coordinates[1]-110].setImageResource(R.drawable.aircarrier_sunk_1_2);	
				ib[coordinates[2]-110].setImageResource(R.drawable.aircarrier_sunk_1_3);	
				ib[coordinates[3]-110].setImageResource(R.drawable.aircarrier_sunk_1_4);	}
			else if (ship.size == 3)	{
				ib[coordinates[0]-110].setImageResource(R.drawable.cruiser_sunk_1_1);		
				ib[coordinates[1]-110].setImageResource(R.drawable.cruiser_sunk_1_2);	
				ib[coordinates[2]-110].setImageResource(R.drawable.cruiser_sunk_1_3);	
			}
			else if (ship.size == 2)	{
				ib[coordinates[0]-110].setImageResource(R.drawable.corvette_sunk_1_1);		
				ib[coordinates[1]-110].setImageResource(R.drawable.corvette_sunk_1_2);	
			}
			else	ib[coordinates[0]-110].setImageResource(R.drawable.patrolboat_sunk_0_1);
		}
	}

	private void lockBoard()	{	for (int i=0; i<100; i++)	aBoardButtons[i].setClickable(false);	}
	private void unlockBoard()	{	for (int i=0; i<100; i++)	aBoardButtons[i].setClickable(true);	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.in_game_menu, menu);
		return true;
	}
	
	
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item)	{
		switch(item.getItemId())	{
		case R.id.menuNewGame:
			Intent back = new Intent(this, ShipPlacement.class);
			startActivity(back);
			break;
		case R.id.showBoard:
			showBoard();
			aGameOver = true;
			break;
		case R.id.exitGame:
			AlertDialog.Builder exit = new AlertDialog.Builder(this);
			exit.setTitle("Back to main menu");
			exit.setMessage("You are about to leave a game. Are you sure?");
			exit.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent mmenu = new Intent(getApplicationContext(),Main.class);
					startActivity(mmenu);					
				}
			});
			
			exit.setNegativeButton("No, back to the game!", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();					
				}
			});
			exit.show();
			break;
		}
	return true;
	}

}
