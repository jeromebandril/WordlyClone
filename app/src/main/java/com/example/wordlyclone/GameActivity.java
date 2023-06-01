/**
 * TODO: check if input is a valid word
 * TODO: show win and lose dialogs
 * TODO: make keyboard smoother, prettify its layout
 * TODO: make grid word animations
 * TODO: fix uppercase and lowercase words comparisons
 */

package com.example.wordlyclone;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    //CONSTANTS
    public final String SOURCE_FILE_NAME = "five_letters_word.txt";
    public final int[] DEFAULT_WORD_LEN = {5,6,7,8};
    public final int DEFAULT_ATTEMPTS_NUM = 6;

    //ATTRIBUTES
    private int levelMode;
    private TextView[][] wordsGrid;
    private char[] word;
    private int currentAttemptCount;
    private int currentColumnCount;
    private ArrayList<String> dictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //game initialization
        //load words
        AssetManager am = this.getAssets();
        try {
            dictionary = Dictionary.readerToList(am.open(SOURCE_FILE_NAME));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //init states
        currentAttemptCount = 0;
        currentColumnCount = 0;
        levelMode = 0;
        //make layout
        initWordGrid();
        initCustomKeyboard();
        //
        extractRandomWord();
    }

    private void extractRandomWord () {
        int rand = (int) (Math.random() * dictionary.size());
        word = dictionary.get(rand).toCharArray();
    }

    private void initWordGrid () {
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

    private void initCustomKeyboard () {
        String[][] letters = {
                {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p"},
                {"a", "s", "d", "f", "g", "h", "j", "k", "l"},
                {"back", "z", "x", "c", "v", "b", "n", "m", "enter"}
        };

        LinearLayout[] keyboardLines = {
                findViewById(R.id.keyboard_firstline),
                findViewById(R.id.keyboard_secondline),
                findViewById(R.id.keyboard_thirdline)
        };

        for (int i = 0; i < letters.length; i++) {
            for (String letter : letters[i]) {
                Button key = new Button(this);
                customizeButton(key, letter.toUpperCase());
                mapKey(key);
                keyboardLines[i].addView(key);
            }
        }
    }

    private void customizeTextview(TextView textView) {
        textView.setText("");
        textView.setTextSize(35);
        textView.setHeight(100);
        textView.setWidth(100);
        // Set layout_gravity to fill
        textView.setLayoutParams(new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f)));
        // Set layout_columnWeight to 1
        GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) textView.getLayoutParams();
        layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        textView.setLayoutParams(layoutParams);
        //
        textView.setBackgroundResource(R.drawable.letter_state);
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

        button.setBackgroundResource(R.drawable.key_button);
        button.setGravity(Gravity.CENTER);
    }

    private void mapKey(Button key) {
        key.setOnClickListener(view -> {
            String buttonText = key.getText().toString();
            int maxLength = DEFAULT_WORD_LEN[levelMode];
            TextView tv;

            if (buttonText.equalsIgnoreCase("back")) {
                //check if row is already empty then move index to -1 and  deletes the last letter inserted
                if (currentColumnCount == 0) return;
                currentColumnCount -= 1;
                tv = wordsGrid[currentAttemptCount][currentColumnCount];
                tv.setText("");
                return;
            }

            if (buttonText.equalsIgnoreCase("enter")) {
                //check if row is filled then get the guessed word as char sequence and send to getResult()
                if (currentColumnCount < maxLength) return;
                char[] word = new char[maxLength];
                for (int i = 0; i < maxLength; i++) {
                    word[i] = wordsGrid[currentAttemptCount][i].getText().toString().charAt(0);
                }
                getResult(word);
                return;
            }

            if (currentColumnCount < maxLength) {
                //insert letter and move index to next textView (letter box)
                tv = wordsGrid[currentAttemptCount][currentColumnCount];
                tv.setText(buttonText);
                currentColumnCount += 1;
            }
        });
    }

    private void getResult (char[] guessedWord) {
        //temporary quick fix for case comparison between chars
        word = new String(word).toUpperCase().toCharArray();


        //check and make color feedback
        for (int i = 0; i < guessedWord.length; i++) {
            TextView tv = wordsGrid[currentAttemptCount][i];
            boolean isPresent = false;

            for (int j = 0; j < word.length; j++) {
                if (guessedWord[i] == word[j]) {
                    isPresent = true;
                }
            }

            //is present but wrong position -> orange
            if (isPresent && guessedWord[i] != word[i]) tv.setBackgroundResource(R.drawable.letter_wrong_position);
            //is present and has correct position -> green
            if (isPresent && guessedWord[i] == word[i]) tv.setBackgroundResource(R.drawable.letter_correct_position);
            //not present -> dark gray
            if (!isPresent) tv.setBackgroundResource(R.drawable.letter_not_present);
        }

        if (guessedWord.toString().equals(word.toString())) {
            //TODO: show dialog for game won
            return;
        }

        if (currentAttemptCount < DEFAULT_ATTEMPTS_NUM-1) {
            currentAttemptCount += 1;
            currentColumnCount = 0;
        } else {
            //TODO: show dialog for game lost
        }
    }

}