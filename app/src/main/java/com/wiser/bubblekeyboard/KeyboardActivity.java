package com.wiser.bubblekeyboard;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public class KeyboardActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        findViewById(R.id.cl_keyboard_init).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardBubbleActivity.showKeyboardDialog(KeyboardActivity.this);
            }
        });
    }
}
