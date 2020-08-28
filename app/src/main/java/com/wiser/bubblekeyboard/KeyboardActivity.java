package com.wiser.bubblekeyboard;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public class KeyboardActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

//        findViewById(R.id.tv_input_init).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                KeyboardBubbleActivity.showKeyboardDialog(KeyboardActivity.this, KeyboardConstants.KEYBOARD_TYPE);
//            }
//        });
//
//        findViewById(R.id.iv_voice_init).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                KeyboardBubbleActivity.showKeyboardDialog(KeyboardActivity.this, KeyboardConstants.VOICE_TYPE);
////                KeyboardDialogFragment.showKeyboardDialog().show(getSupportFragmentManager(),KeyboardDialogFragment.class.getSimpleName());
//            }
//        });
    }
}
