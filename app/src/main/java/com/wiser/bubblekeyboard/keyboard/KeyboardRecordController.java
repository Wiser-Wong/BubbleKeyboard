package com.wiser.bubblekeyboard.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.wiser.bubblekeyboard.R;

/**
 * @author Wiser
 * 
 *         键盘录音默认控制器
 */
public class KeyboardRecordController extends ConstraintLayout {

	public KeyboardRecordController(Context context) {
		super(context);
		init();
	}

	public KeyboardRecordController(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.keyboard_default_view, this, true);

		findViewById(R.id.tv_input_init).setOnClickListener(new View.OnClickListener() {

			@Override public void onClick(View v) {
				new KeyboardPopWindow(getContext(), true).showKeyboardPop(true);
			}
		});

		findViewById(R.id.iv_voice_init).setOnClickListener(new View.OnClickListener() {

			@Override public void onClick(View v) {
				new KeyboardPopWindow(getContext(), false).showKeyboardPop(false);
			}
		});
	}
}
