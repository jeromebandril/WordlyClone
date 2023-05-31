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
    public final int[] DEFAULT_WORD_LEN = {5,6,7,8};
    public final int DEFAULT_ATTEMPTS_NUM = 6;

    private int levelMode;
    private TextView[][] wordsGrid;
    private int currentAttemptCount;
    private int currentColumnCount;
    private String word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        levelMode = 0;
        setGameStatus(0,0);
        initWordGrid();
        initCustomKeyboard();
    }

    public void initWordGrid () {
        int columns = DEFAULT_WORD_LEN[levelMode];
        int rows = DEFAULT_ATTEMPTS_NUM;
        GridLayout gridLayout = findViewById(R.id.grid_words);

        //init data structure
        wordsGrid = new TextView[rows][columns];
        //set columns and rows count
        gridLayout.setColumnCount(columns);
        gridLayout.setRowCount(rows);

        // make layout: fill with textViews
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                TextView tv = new TextView(this);
                wordsGrid[i][j] = tv;
                customizeTextview(tv);
                gridLayout.addView(tv);
            }
        }
    }

    public void initCustomKeyboard () {
        String[][] letters = {
                {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p"},
                {"a", "s", "d", "f", "g", "h", "j", "k", "l"},
                {"back", "z", "x", "c", "v", "b", "n", "m", "k", "l", "enter"}
        };

        LinearLayout[] keyboardLines = {
                findViewById(R.id.keyboard_firstline),
                findViewById(R.id.keyboard_secondline),
                findViewById(R.id.keyboard_thirdline)
        };

        for (int i = 0; i < letters.length; i++) {
            for (String letter : letters[i]) {
                Button key = new Button(this);
                customizeButton(key, letter);
                mapKey(key);
                keyboardLines[i].addView(key);
            }
        }
    }

    private void customizeTextview(TextView textView) {
        textView.setText("");
        textView.setHeight(100);
        textView.setWidth(100);
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

    private void setGameStatus (int updatedAttempt, int updatedColumn) {
        currentAttemptCount = updatedAttempt;
        currentColumnCount = updatedColumn;
    }

    private void customizeButton(Button button, String text) {
        button.setText(text);
        button.setHeight(500);

        // Set layout_weight to 1
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,100);
        layoutParams.weight = 1;
        layoutParams.rightMargin = 10;
        button.setLayoutParams(layoutParams);

        button.setBackgroundResource(R.drawable.key_button);
        button.setGravity(Gravity.CENTER);
    }

    private void mapKey(Button key) {
        key.setOnClickListener(view -> {
            String buttonText = key.getText().toString();
            TextView tv;

            if (buttonText.equals("back")) {
                if (currentColumnCount == 0) return;
                currentColumnCount -= 1;
                tv = wordsGrid[currentAttemptCount][currentColumnCount];
                tv.setText("");
                return;
            }

            if (buttonText.equals("enter")) {
                getResult(buttonText);
                return;
            }

            if (currentColumnCount < DEFAULT_WORD_LEN[levelMode]) {
                tv = wordsGrid[currentAttemptCount][currentColumnCount];
                tv.setText(buttonText);
                currentColumnCount += 1;
            }
        });
    }


    private void getResult (String guessedWord) {

    }
}