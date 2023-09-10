package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ArrayList<TextView> cell_list;

    private List<Integer> mines = setMines();

    private HashSet<Integer> seenCells = new HashSet<>();
    private HashSet<Integer> flagspots = new HashSet<>();

    private int seconds = 0;
    private boolean running = true;



    private static final int COLUMN_COUNT = 10;
    private boolean dig = true;


    private int dpToPixel(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cell_list = new ArrayList<TextView>();

        runTimer();

        GridLayout grid = (GridLayout) findViewById(R.id.gridLayout01);
        for (int i = 0; i<12; i++) {
            for (int j=0; j<10; j++) {
                TextView cell = new TextView(this);
                cell.setHeight( dpToPixel(32) );
                cell.setWidth( dpToPixel(32) );
                cell.setTextSize( 16 );//dpToPixel(32) );
                cell.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                cell.setTextColor(Color.parseColor("lime"));
                cell.setBackgroundColor(Color.parseColor("lime"));
                cell.setOnClickListener(this::onClick);

                GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                lp.setMargins(dpToPixel(2), dpToPixel(2), dpToPixel(2), dpToPixel(2));
                lp.rowSpec = GridLayout.spec(i);
                lp.columnSpec = GridLayout.spec(j);

                grid.addView(cell, lp);

                cell_list.add(cell);
            }
        }
        TextView pickorflag = (TextView) findViewById(R.id.pickflag);
        pickorflag.setOnClickListener(this::onClickPickOrFlag);
    }

    public void runTimer(){
        TextView timer = (TextView) findViewById(R.id.clockvariable);
        Handler handler = new Handler();
        handler.post(new Runnable(){
            @Override
            public void run(){
                int minutes = (seconds % 3600)/60;
                int secs = seconds % 60;
                String time;
                if(minutes == 0){
                    time = String.format("%02d", secs);
                }
                else {
                    time = String.format("%02d:%02d", minutes, secs);
                }
                timer.setText(time);
                if(running){
                    seconds += 1;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    private int findIndexOfCellTextView(TextView cell) {
        for (int n=0; n<cell_list.size(); n++) {
            if (cell_list.get(n) == cell)
                return n;
        }
        return -1;
    }

    private int getMineCount(TextView cell) {
        int mineCount = 0;
        int index = findIndexOfCellTextView(cell);
        int[] differences = {-11, -10, -9, -1, 1, 9, 10, 11};
        int n = findIndexOfCellTextView(cell);
        int i = n/COLUMN_COUNT;
        int j = n%COLUMN_COUNT;

        for(Integer difference : differences){
            if(i == 0){
                if(difference <= -10){
                    continue;
                }
            }
            if(i == 11){
                if(difference >= 10){
                    continue;
                }
            }
            if(j == 0){
                if(difference == -11 || difference == -1 || difference == 9){
                    continue;
                }
            }
            if(j == 9){
                if(difference == 1 || difference == 11 || difference == -9){
                    continue;
                }
            }
            int testindex = index + difference;
            if(mines.contains(testindex)){
                mineCount += 1;
            }
        }
        return mineCount;
    }

    private List<Integer> setMines() {
        Random random = new Random();
        List<Integer> minelocation = new ArrayList<>();

        while(minelocation.size() < 4){
            int randomNumber = random.nextInt(120);
            if(!minelocation.contains(randomNumber)){
                minelocation.add(randomNumber);
            }
        }
        return minelocation;
    }

    public void onClickPickOrFlag(View view){
        TextView icon = (TextView) view;

        if(dig){
            icon.setText("flag");
            dig = false;
        }
        else{
            icon.setText("pick");
            dig = true;
        }
    }

    public void showAllBombs(){
        running = false;
        for(Integer mine : mines) {
            TextView cell = cell_list.get(mine);
            cell.setBackgroundColor(Color.RED);
        }

        ConstraintLayout mainLayout = (ConstraintLayout) findViewById(R.id.mainLayout);
//        mainLayout.getRootView().setOnClickListener(this::onClickMain);
        mainLayout.setOnClickListener(this::onClickMain);
    }

    public void showAllBombsGood(){
        running = false;
        for(Integer mine : mines) {
            TextView cell = cell_list.get(mine);
            cell.setBackgroundColor(Color.YELLOW);
        }
        ConstraintLayout mainLayout = (ConstraintLayout) findViewById(R.id.mainLayout);
//        mainLayout.getRootView().setOnClickListener(this::onClickMain);
        mainLayout.setOnClickListener(this::onClickMain);
    }

    public void onClickMain(View view){
        for(Integer mine : mines) {
            TextView cell = cell_list.get(mine);
            cell.setBackgroundColor(Color.MAGENTA);
        }
    }

    public void onClick(View view){

        TextView cell = (TextView) view;
        int n = findIndexOfCellTextView(cell);
        int i = n/COLUMN_COUNT;
        int j = n%COLUMN_COUNT;
        if(seenCells.contains(n)){
            return;
        }


        if (dig == true){
            seenCells.add(n);

            if(seenCells.size() == 116){
                showAllBombsGood();
            }

            if(mines.contains(n)){
                showAllBombs();

                return;
            }
            int mineCount = getMineCount(cell);
            if(mineCount == 0){
                cell.setBackgroundColor(Color.LTGRAY);
                int[] differences = {-11, -10, -9, -1, 1, 9, 10, 11};
                for(Integer difference : differences) {
                    if(i == 0){
                        if(difference <= -10){
                            continue;
                        }
                    }
                    if(i == 11){
                        if(difference >= 10){
                            continue;
                        }
                    }
                    if(j == 0){
                        if(difference == -11 || difference == -1 || difference == 9){
                            continue;
                        }
                    }
                    if(j == 9){
                        if(difference == 1 || difference == 11 || difference == -9){
                            continue;
                        }
                    }
                    if(n+difference >= 0 && n+difference < 120 && !seenCells.contains(n+difference)){
                        TextView temp = cell_list.get(n+difference);
                        if(!mines.contains(n+difference)){
                            temp.performClick();
                        }
                    }
                }
            }
            else{
                cell.setBackgroundColor(Color.LTGRAY);
                cell.setTextColor(Color.GRAY);
                cell.setText(String.valueOf(mineCount));
            }
        }
        else{
            TextView flagvariable = (TextView) findViewById(R.id.flagvariable);
            String flagcountstring = (String) flagvariable.getText();
            Integer flagcountinteger = Integer.parseInt(flagcountstring);

            if(flagspots.contains(n)){
                flagcountinteger += 1;
                cell.setBackgroundColor(Color.parseColor("lime"));
                flagspots.remove(n);
            }
            else{
                flagcountinteger -= 1;
                cell.setBackgroundColor(Color.BLUE);
                flagspots.add(n);
            }
            seenCells.remove(n);
            String flagcountstring2 = String.valueOf(flagcountinteger);
            flagvariable.setText(flagcountstring2);

            if(seenCells.size() == 116){
                showAllBombsGood();
            }
        }
    }
}