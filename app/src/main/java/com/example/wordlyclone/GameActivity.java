package com.example.wordlyclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    public final int WORD_LEN = 5;
    public final int ROWS_ATTEMPTS = 6;

    private TextView[][] wordsGrid;
    int num_attempts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        num_attempts = 0;
        wordsGrid = new TextView[ROWS_ATTEMPTS][WORD_LEN];
        initWordGrid();
        initCustomKeyboard();
    }

    public void initWordGrid () {
        GridLayout gridLayout = findViewById(R.id.grid_words);
        for (int i = 0; i < ROWS_ATTEMPTS; i++) {
            for (int j = 0; j < WORD_LEN; j++) {
                TextView textView = new TextView(this);
                customizeTextview(textView);
                gridLayout.addView(textView);
                wordsGrid[i][j] = textView;
            }
        }
        //init data structure

    }

    public void initCustomKeyboard () {
        String [] firstLine_letters = {"q","w","e","r","t","y","u","i","o","p"};
        String [] secondLine_letters = {"a","s","d","f","g","h","j","k","l"};
        String [] thirdLine_letters = {"back"+"z","x","c","v","b","n","m","k","l"+"enter"};

        LinearLayout keyboard_firstLine = findViewById(R.id.keyboard_firstline);
        LinearLayout keyboard_secondLine = findViewById(R.id.keyboard_secondline);
        LinearLayout keyboard_thirdLine = findViewById(R.id.keyboard_thirdline);

        for (int i = 0; i < firstLine_letters.length; i++) {
            Button key = new Button(this);
            customizeButton(key,firstLine_letters[i]);
            keyboard_firstLine.addView(key);
        }
        for (int i = 0; i < secondLine_letters.length; i++) {
            Button key = new Button(this);
            customizeButton(key,secondLine_letters[i]);
            keyboard_secondLine.addView(key);
        }
        for (int i = 0; i < thirdLine_letters.length; i++) {
            Button key = new Button(this);
            customizeButton(key,thirdLine_letters[i]);
            keyboard_thirdLine.addView(key);
        }
    }

    private void customizeTextview(TextView textView) {
        textView.setText("");
        // Set layout_gravity to fill
        textView.setLayoutParams(new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f)));
        // Set layout_columnWeight to 1
        GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) textView.getLayoutParams();
        layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        textView.setLayoutParams(layoutParams);
        //
        textView.setBackgroundResource(R.drawable.letter_box_textview);
        textView.setGravity(Gravity.CENTER);
    }

    private void customizeButton(Button button, String text) {
        button.setText(text);
        button.setHeight(500);
        // Set layout_weight to 1
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,100);
        layoutParams.weight = 1;
        layoutParams.rightMargin = 10;
        button.setLayoutParams(layoutParams);
        //
        button.setBackgroundResource(R.drawable.key_button);
        button.setGravity(Gravity.CENTER);
        //add listener
        button.setOnClickListener(view->{
            int position = 0;
            wordsGrid[num_attempts][position].setText(button.getText());
            //update layout
        });
    }
}