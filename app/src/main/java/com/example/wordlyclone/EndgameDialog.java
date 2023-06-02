package com.example.wordlyclone;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class EndgameDialog extends Dialog{
    public final String DEFAULT_WIN_MESSAGE = "you won!";
    public final String DEFAULT_LOSE_MESSAGE = "try again!";

    TextView message;

    public EndgameDialog(@NonNull Context context) {
        super(context);
        message = findViewById(R.id.dialog_message);
        //customization
        this.setContentView(R.layout.custom_dialog);
        this.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);
        this.getWindow().setLayout(500,ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void showWin () {
        message.setText(DEFAULT_WIN_MESSAGE);
        this.show();
    }

    public void showLost () {
        message.setText(DEFAULT_LOSE_MESSAGE);
        this.show();
    }
}
