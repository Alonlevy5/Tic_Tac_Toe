package com.example.tictactoeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView playerOneScore, playerTwoScore, playerStatus;
    private Button [] buttons = new Button[9];
    private Button resetGame;
    LottieAnimationView lottieAnimationView;
    private int playerOneScoreCount, playerTwoScoreCount, roundCount;
    boolean activePlayer;

    // p1 => 0
    // p2 => 1
    // empty =>2
    int [] gameState= {2,2,2,2,2,2,2,2,2};

    int [][] winningPositions = {
            {0,1,2}, {3,4,5} ,{6,7,8}, {0,3,6}, {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6}

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerOneScore = (TextView) findViewById(R.id.playerOneScore);
        playerTwoScore = (TextView) findViewById(R.id.playerTwoScore);
        playerStatus = (TextView) findViewById(R.id.playerStatus);
        lottieAnimationView = findViewById(R.id.animation);
        resetGame = (Button) findViewById(R.id.resetGame);
        lottieAnimationView.setRepeatCount(1);
        for(int i= 0; i<buttons.length; i++){

            String buttonID = "btn_" + i;
            int resourceID= getResources().getIdentifier(buttonID, "id", getPackageName());
            buttons[i]= (Button) findViewById(resourceID);
            buttons[i].setOnClickListener(this);

        }
        playerStatus.setText("");
        roundCount = 0;
        playerOneScoreCount = 0;
        playerTwoScoreCount = 0;
        activePlayer = true;


    }

    @Override
    public void onClick(View v) {
        //Log.i("test","button is clicked!");
        if(!((Button)v).getText().toString().equals("")){

            return;

        }
        String buttonID = v.getResources().getResourceEntryName(v.getId());
        int gameStatePointer = Integer.parseInt(buttonID.substring(buttonID.length()-1, buttonID.length()));

        if(activePlayer){
            ((Button)v).setText("X");
            ((Button)v).setTextColor(Color.parseColor("#ff1100"));
            gameState[gameStatePointer] = 0;
        } else {
            ((Button)v).setText("O");
            ((Button)v).setTextColor(Color.parseColor("#ffffff"));
            gameState[gameStatePointer] = 1;
        }
        roundCount++;

        if(checkWinner()){
            if(activePlayer){
                playerOneScoreCount++;
                updatePlayerScore();
                lottieAnimationView.playAnimation();
                Toast.makeText(this, "Player One Won(X)!", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("We Have A Winner!!!");
                builder.setMessage("Player One (X) Won!");
                builder.setNegativeButton("Play Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                PlayAgain();
            }else {
                playerTwoScoreCount++;
                updatePlayerScore();
                lottieAnimationView.playAnimation();
                Toast.makeText(this, "Player Two Won(O)!", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("We Have A Winner!!!");
                builder.setMessage("Player Two (O) Won!");
                builder.setNegativeButton("Play Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                PlayAgain();
            }

        }else if(roundCount==9){
            PlayAgain();
            Toast.makeText(this, "No Winner!", Toast.LENGTH_SHORT).show();

        }else {
            activePlayer = !activePlayer;
        }

        if(playerOneScoreCount > playerTwoScoreCount ){
            playerStatus.setText("Player One is Leading!");
        }else if (playerTwoScoreCount > playerOneScoreCount){
            playerStatus.setText("Player Two is Leading!");
        }else{
            playerStatus.setText("");
        }

        resetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayAgain();
                playerOneScoreCount = 0;
                playerTwoScoreCount = 0;
                playerStatus.setText("");
                updatePlayerScore();
                if(lottieAnimationView.isAnimating()){
                    lottieAnimationView.cancelAnimation();
                }
            }
        });



    }

    public boolean checkWinner(){
        boolean winnerResult = false;

        for(int[] winningPosition : winningPositions){
            if (gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                    gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                    gameState[winningPosition[0]] !=2) {
                winnerResult = true;

            }
        }
        return winnerResult;
    }

    public void updatePlayerScore(){
        playerOneScore.setText(Integer.toString(playerOneScoreCount));
        playerTwoScore.setText(Integer.toString(playerTwoScoreCount));
    }

    public void PlayAgain(){
        roundCount = 0;
        activePlayer = true;

        for(int i= 0 ; i< buttons.length; i++){
            gameState[i] = 2;
            buttons[i].setText("");

        }
    }
}