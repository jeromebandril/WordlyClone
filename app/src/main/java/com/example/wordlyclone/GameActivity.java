/**
 * TODO: fix win and lose dialogs
 * TODO: make grid word animations
 * TODO: implement restart game
 * TODO: add sounds effects
 * TODO: fix keyboard coloring: make oneway updates black -> orange -> green
 */

package com.example.wordlyclone;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class GameActivity extends AppCompatActivity {
    //CONSTANTS
    public final String DEFAULT_WIN_MESSAGE = "you won!";
    public final String DEFAULT_LOSE_MESSAGE = "try again!";
    public final String SOURCE_FILE_NAME = "five_letters_word.txt";
    public final int[] DEFAULT_WORD_LEN = {5,6,7,8}; //for future implementation of different word lengths
    public final int DEFAULT_ATTEMPTS_NUM = 6;

    //ATTRIBUTES
    private TextView[][] wordsGrid;
    private Hashtable<String,Button> keyboard;
    private ArrayList<String> dictionary;
    private String word;
    private int currentAttemptCount;
    private int currentColumnCount;
    private int levelMode;
    private Dialog endgameDialog;
    private Button btnStartgame;
    boolean isGameLost;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // activity settings
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //game initialization
        //load words list in a variable
        AssetManager am = this.getAssets();
        try {
            dictionary = Dictionary.readerToList(am.open(SOURCE_FILE_NAME));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //init
        currentAttemptCount = 0;
        currentColumnCount = 0;
        levelMode = 0;
        //make layouts
        makeGridWords();
        makeCustomKeyboard();
        makeEndgameDialog();
        //choose random word
        extractRandomWord();

        btnStartgame = findViewById(R.id.btn_startGame);
        btnStartgame.setOnClickListener(view -> {
            isGameLost = true;
            endgameDialog.show();
        });
    }

    private void restartGame () {
        //reset everything
        for (TextView[] row: wordsGrid) {
            for (TextView letter: row) {
                letter.setText("");
                letter.setBackgroundResource(R.drawable.letter_empty);
            }
        }

        Enumeration<String> e = keyboard.keys();
        while (e.hasMoreElements()) {
            keyboard.get(e.nextElement()).setBackgroundResource(R.color.light_gray);
        }

        //reset offset
        currentAttemptCount = 0;
        currentColumnCount = 0;

        extractRandomWord();
    }

    private void extractRandomWord () {
        int rand = (int) (Math.random() * dictionary.size());
        word = dictionary.get(rand);
        Log.d("extractedword",word);
    }

    private void makeGridWords() {
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

    private void makeCustomKeyboard() {

        keyboard = new Hashtable<>();

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
                customizeButton(key, letter);
                mapKey(key);
                keyboard.put(letter,key);
                keyboardLines[i].addView(key);
            }
        }
    }

    private void makeEndgameDialog () {
        endgameDialog = new Dialog(this) {
            @Override
            public void show() {
                TextView messageTextview = findViewById(R.id.dialog_message);
                TextView title = findViewById(R.id.dialog_title);
                Button btnStartgame = findViewById(R.id.btn_newGame);
                title.setText(isGameLost ? DEFAULT_LOSE_MESSAGE : DEFAULT_WIN_MESSAGE);
                messageTextview.setText("the word was: " + word);
                btnStartgame.setOnClickListener(view -> {
                    restartGame();
                    this.dismiss();
                });
                super.show();
            }
        };

        //customization;
        endgameDialog.setContentView(R.layout.custom_dialog);
        endgameDialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);
        endgameDialog.getWindow().setLayout(700, ViewGroup.LayoutParams.WRAP_CONTENT); //height =
    }

    private void customizeTextview(TextView textView) {
        //options
        int textSize = 35;
        int height = 100;
        int width = 100;

        //text settings
        textView.setText("");
        textView.setTextSize(textSize);
        textView.setTypeface(null,Typeface.BOLD);
        textView.setHeight(height);
        textView.setWidth(width);
        textView.setAllCaps(true);

        //set layout params
        textView.setLayoutParams(new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f)));
        // Set layout_columnWeight to 1
        GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) textView.getLayoutParams();
        layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        textView.setLayoutParams(layoutParams);

        //textview settings
        textView.setBackgroundResource(R.drawable.letter_state);
        textView.setGravity(Gravity.CENTER);
    }

    private void customizeButton(Button button, String text) {
        //options
        int width = 100;
        int height = 125;
        int margin = 10;

        //set layout params
        if (text.length()>1) width = 125;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        layoutParams.rightMargin = margin;
        button.setLayoutParams(layoutParams);

        //button settings
        button.setText(text);
        button.setBackgroundResource(R.drawable.key_button);
        button.setGravity(Gravity.CENTER);
    }

    private void mapKey(Button key) {
        key.setOnClickListener(view -> {
            String buttonText = key.getText().toString();
            int maxLength = DEFAULT_WORD_LEN[levelMode];
            TextView tv;

            if (buttonText.equals("back")) {
                //check if row is already empty then move index to -1 and  deletes the last letter inserted
                if (currentColumnCount == 0) return;
                currentColumnCount -= 1;
                tv = wordsGrid[currentAttemptCount][currentColumnCount];
                tv.setText("");
                return;
            }

            if (buttonText.equals("enter")) {
                //check if row is filled then get the guessed word and send it to getResult()
                if (currentColumnCount < maxLength) return;
                StringBuilder word = new StringBuilder();
                for (int i = 0; i < maxLength; i++) {
                    word.append(wordsGrid[currentAttemptCount][i].getText().toString());
                }
                //check if word is valid and exists
                if (!dictionary.contains(word.toString())) {
                    Toast toast = Toast.makeText(this,"Not a valid word", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    return;
                }
                getResult(word.toString());
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

    private void getResult (String guessedWord) {
        //check and make color feedback
        for (int i = 0; i < guessedWord.length(); i++) {
            TextView tv = wordsGrid[currentAttemptCount][i];
            boolean isPresent = false;

            for (int j = 0; j < word.length(); j++) {
                if (guessedWord.charAt(i) == word.charAt(j)) {
                    isPresent = true;
                }
            }

            //get corresponding key button
            Button key = keyboard.get(Character.toString(guessedWord.charAt(i)));

            //not present -> dark gray
            if (!isPresent) {
                tv.setBackgroundResource(R.drawable.letter_not_present);
                key.setBackgroundResource(R.color.dark_gray);
            }
            //is present but wrong position -> orange
            if (isPresent && guessedWord.charAt(i) != word.charAt(i)) {
                tv.setBackgroundResource(R.drawable.letter_wrong_position);
                key.setBackgroundResource(R.color.orange);
            }
            //is present and has correct position -> green
            if (isPresent && guessedWord.charAt(i) == word.charAt(i)) {
                tv.setBackgroundResource(R.drawable.letter_correct_position);
                key.setBackgroundResource(R.color.green);
            }

        }

        if (guessedWord.equals(word)) {
            isGameLost = false;
            endgameDialog.show();

        } else if (currentAttemptCount < DEFAULT_ATTEMPTS_NUM-1) {
            currentAttemptCount += 1;
            currentColumnCount = 0;

        } else {
            isGameLost = true;
            endgameDialog.show();
        }
    }
}