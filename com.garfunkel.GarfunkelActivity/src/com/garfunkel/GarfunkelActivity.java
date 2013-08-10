package com.garfunkel;

//import java.util.Timer;

import java.util.Random;

import android.app.Activity;
//import android.graphics.drawable.Drawable;
//import android.media.MediaPlayer;
import android.media.SoundPool;
//import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
//import android.graphics.drawable.*;
//import android.widget.Toast;

public class GarfunkelActivity extends Activity {

	// Maximum number of moves before game end
	static int max_rounds = 9;
	
	// Indexes the current round and move
	int current_round = 0; // how many rounds have been played
	int current_move = 0; // how many moves have been checked in the current round
	
	// Declare and initialize arrays for storing computer and player moves
	public int[] cpuMoves = new int[max_rounds + 1];
	public int[] playerMoves = new int[max_rounds + 1];
	
	// Declare and initialize array for translating player moves to View ID's and back again
	public int[] idMap = new int[4];
	
	// Create MediaPlayers
	// No, do not create MediaPlayers.  They do not work.
	/*MediaPlayer redTone = MediaPlayer.create(getApplicationContext(), R.raw.higha);
	MediaPlayer greenTone = MediaPlayer.create(getApplicationContext(), R.raw.e);
	MediaPlayer blueTone = MediaPlayer.create(getApplicationContext(), R.raw.csharp);
	MediaPlayer yellowTone = MediaPlayer.create(getApplicationContext(), R.raw.lowa); */
	
	// Create SoundPool
	int[] soundMap = new int[6];
	SoundPool soundpool = new SoundPool(4, 3, 0);
			
    @Override
    // Called on Activity creation
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Populate idMap with ID's from the layout
        idMap[0] = R.id.red;
        idMap[1] = R.id.green;
        idMap[2] = R.id.blue;
        idMap[3] = R.id.yellow;
        
        //Configure SoundPool
    	soundMap[0] = soundpool.load(getApplicationContext(), R.raw.red, 1);
        soundMap[1] = soundpool.load(getApplicationContext(), R.raw.green, 1);
        soundMap[2] = soundpool.load(getApplicationContext(), R.raw.blue, 1);
        soundMap[3] = soundpool.load(getApplicationContext(), R.raw.yellow, 1);
        soundMap[4] = soundpool.load(getApplicationContext(), R.raw.gameover, 1);
        soundMap[5] = soundpool.load(getApplicationContext(), R.raw.youwin, 1);

        initGame(); // initialize and begin the game
    	return;
    }
    
    // Play the computer's moves through the current move
    public void playMoves() {
    	for (int i = 0; i <= current_round; i++) {
    		final View v = (View) findViewById(idMap[cpuMoves[i]]);
    		Handler handler = new Handler();
            handler.postDelayed(new Runnable() { 
                 public void run() {
                	 flashBeep(v);
                 }
            }, 500 * (i + 1));
    	}
    	//current_round++;
    	return;
    }
    
    // Check the player's current move
    public void checkMove(final View v) {
    	
    	// Convert the View ID into an int for comparison to cpuMoves[current_move]
    	int thisId = v.getId();
    	int this_move = -1;
    	switch (thisId) {
    		case R.id.red: this_move = 0;
    			break;
    		case R.id.green: this_move = 1;
    			break;
    		case R.id.blue: this_move = 2;
    			break;
    		case R.id.yellow: this_move = 3;
    			break;
    		default: System.out.println("switch failed in checkMove() on thisId = " + thisId);
    			return;
    	}
    	
    	// Game over if the current move doesn't match the computer's move at the current index
    	if ( this_move != cpuMoves[current_move] ) {
    		gameOver();
    		return; 
    	}
    	
    	// Check whether we've finished the current round
    	if ( current_move == current_round ) {
    		// If it's the last round, the player has won
    		if ( current_round == max_rounds ) {
    			youWin();
    			return;
    		}
    		// Initialize the next round and delay before the next round
    		current_move = 0;
    		current_round++;
    		Handler handler = new Handler();
            handler.postDelayed(new Runnable() { 
                 public void run() {
                	 playMoves();
                 }
            }, 500);
    		return;
    	}
    	
    	// Increment the current move
    	current_move++;
    	return;
    }
    
    // Game over, man.  Game over.
    public void gameOver() {
    	System.out.println("GAME OVER");
    	soundpool.play(soundMap[4], 1, 1, 1, 0, 1);
    	Handler handler = new Handler();
        handler.postDelayed(new Runnable() { 
             public void run() {
            	initGame();
             }
        }, 1000);
    	return;
    }
    
    // A wiener is you
    public void youWin() {
    	System.out.println("YOU WIN");
    	soundpool.play(soundMap[5], 1, 1, 1, 0, 1);
    	Handler handler = new Handler();
        handler.postDelayed(new Runnable() { 
             public void run() {
            	 initGame();
             }
        }, 1000);
    	return;
    }
    
    public void initGame() {
    	
        // Generate the computer's moves
        Random r = new Random();
    	for (int i = 0; i < cpuMoves.length; i++) {
    		cpuMoves[i] = r.nextInt(4);
    	}
    	
    	current_round = 0;
    	current_move = 0;
    	playMoves();
    	return;
    }
    
    // Flash and beep the View
    public void flashBeep(final View v) {
    	//final Drawable background = v.getBackground();
    	final int flashColor = 0xFFFFFFFF; // Color to flash
    	final int delay = 100; // how long to flash in milliseconds
    	//final int originalColor;
    	v.setBackgroundColor(flashColor);
    	int thisId = v.getId();
    	switch (thisId) {
    		case R.id.red:
    			//originalColor = 0xFFFF0000;
    			//redTone.start();
    			soundpool.play(soundMap[0], 1, 1, 1, 0, 1);
    			break;
    		case R.id.green:
    			//originalColor = 0xFF00FF00;
    			//greenTone.start();
    			soundpool.play(soundMap[1], 1, 1, 1, 0, 1);
    			break;
    		case R.id.blue:
    			//originalColor = 0xFF0000FF;
    			//blueTone.start();
    			soundpool.play(soundMap[2], 1, 1, 1, 0, 1);
    			break;
    		case R.id.yellow:
    			//originalColor = 0xFFFFFF00;
    			//yellowTone.start();
    			soundpool.play(soundMap[3], 1, 1, 1, 0, 1);
    			break;
    		default:
    			System.out.println("switch failed in flashBeep() on this_id = " + thisId);
    			return;
    	}
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() { 
             public void run() { 
                 //v.setBackgroundColor(originalColor);
            	 View red = (View) findViewById(R.id.red);
            	 View green = (View) findViewById(R.id.green);
            	 View blue = (View) findViewById(R.id.blue);
            	 View yellow = (View) findViewById(R.id.yellow);
            	 
            	 red.setBackgroundColor(0xFFFF0000);
            	 green.setBackgroundColor(0xFF00FF00);
            	 blue.setBackgroundColor(0xFF0000FF);
            	 yellow.setBackgroundColor(0xFFFFFF00);
             }
        }, delay);
        //mediaPlayer.reset();
        //MediaPlayer mediaPlayer = MediaPlayer.create(v.getContext(), R.raw.beep);
        //mediaPlayer.start();
        return;
    }
    
    public void onClick(final View v) {
        //Context context = v.getContext();
        //CharSequence text = "ID NUM " + v.getId();
        //int duration = Toast.LENGTH_SHORT;

        //Toast toast = Toast.makeText(context, text, duration);
        //toast.show();
    	
    	/* final Drawable background = v.getBackground();
    	v.setBackgroundColor(000000);
        Handler handler = new Handler(); 
        handler.postDelayed(new Runnable() { 
             public void run() { 
                  v.setBackgroundDrawable(background); 
             } 
        }, 100);
        MediaPlayer mediaPlayer = MediaPlayer.create(v.getContext(), R.raw.beep);
        mediaPlayer.start(); */
    	//v.setBackgroundDrawable(background);
    	flashBeep(v);
    	checkMove(v);
    	return;
    }
}