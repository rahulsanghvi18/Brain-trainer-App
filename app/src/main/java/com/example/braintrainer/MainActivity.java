package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.telephony.mbms.StreamingServiceInfo;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView timerView;
    TextView questionView;
    TextView ansView;
    TextView scoreBoard;
    TextView timer;
    TextView finalScore;
    Button playButton;
    CountDownTimer mytimer;
    Random random = new Random();
    GridLayout mygrid;
    MediaPlayer audio;
    ArrayList<TextView> myArray = new ArrayList<TextView>();
    int ans = 0, scores = 0, tries = 0;
    boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timerView = (TextView)findViewById(R.id.timerView);
        questionView = (TextView)findViewById(R.id.questions);
        ansView = (TextView) findViewById(R.id.finalScoreView);
        timer = (TextView)findViewById(R.id.timerView);
        finalScore = (TextView)findViewById(R.id.playAgainScreen);
        finalScore.setVisibility(View.INVISIBLE);
        scoreBoard = (TextView)findViewById(R.id.scroreView);
        scoreBoard.setText("0 / 0");
        mygrid = (GridLayout)findViewById(R.id.gridLayout);
        playButton = (Button)findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                getNumbers();
            }
        });
        myArray.add((TextView)findViewById(R.id.textView1));
        myArray.add((TextView)findViewById(R.id.textView2));
        myArray.add((TextView)findViewById(R.id.textView3));
        myArray.add((TextView)findViewById(R.id.textView4));
        for(TextView abc: myArray) abc.setClickable(false);
        mytimer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long l) {
                timer.setText(String.valueOf((int)(l/1000)%60) + " : 00" );
                ansView.setText("");
                Log.i("Timer", String.valueOf(l));
            }

            @Override
            public void onFinish() {
                playButton.setEnabled(true);
                playButton.setText("Play Again");
                for(TextView abc: myArray) abc.setClickable(false);
                mygrid.setVisibility(View.INVISIBLE);
                finalScore.setVisibility(View.VISIBLE);
                finalScore.setText("CONGRATULATIONS!!!\nFinal score\n"+String.valueOf(scores)+ " / " + String.valueOf(tries));
                scoreBoard.setText(String.valueOf(scores) + " / " + String.valueOf(tries));
                ansView.setText("Press play to Start Playing");
                tries = 0;
                scores = 0;
            }
        } ;
        audio = MediaPlayer.create(MainActivity.this, R.raw.audio);
        audio.setLooping(true);
        audio.start();
    }

    public void clicked(View view){
        if(flag){
            tries++;
            if(view.getTag().toString() == String.valueOf(ans)) {
                ansView.setText("Correct !");
                scores++;
            }else{
                ansView.setText("Incorrect");
            }
            scoreBoard.setText(String.valueOf(scores) + " / " + String.valueOf(tries));
        }
        flag = false;
        getNumbers();
    }

    public void getNumbers(){
        if(tries == 0){
            mytimer.start();
            for(TextView abc: myArray) abc.setClickable(true);
            finalScore.setVisibility(View.INVISIBLE);
            mygrid.setVisibility(View.VISIBLE);
            scoreBoard.setText(String.valueOf(scores) + " / " + String.valueOf(tries));
        }
        int a = random.nextInt(50);
        int b = random.nextInt(50);
        ans = a + b;
        questionView.setText(Integer.toString(a) + " + " + Integer.toString(b));
        ArrayList<Integer> ansList = new ArrayList<Integer>();
        ansList.add(a+b);
        for(int i = 1 ; i <= 3 ; i++){
           int num = random.nextInt(100);
           while(num == (a+b)) num = random.nextInt(100);
           ansList.add(num);
        }
        Collections.shuffle(ansList);
        for(int i = 0 ; i < 4 ; i++) {
            myArray.get(i).setTag(String.valueOf(ansList.get(i)));
            myArray.get(i).setText(String.valueOf(ansList.get(i)));
        }
        flag = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        audio.stop();
        audio.release();
    }
}
