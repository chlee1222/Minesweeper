package com.example.minesweeper;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultPage extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);
        Intent intent = getIntent();
        int[] values = intent.getIntArrayExtra("message");
        TextView textbox = (TextView) findViewById(R.id.textbox);
        String resultstring;
        if(values[0] == 0){
            String time = Integer.toString(values[1]);
            resultstring = "Used " + time + " seconds. You lost!";
        }
        else{
            String time = Integer.toString(values[1]);
            resultstring = "Used " + time + " seconds. You won. Good Job!";
        }
        textbox.setText(resultstring);
    }

    public void onClickReset(View view){
        return;
    }
}
