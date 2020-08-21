package com.wiser.bubblekeyboard.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.wiser.bubblekeyboard.helper.SpecialEmojiHelper;

/**
 * @author Wiser
 * 
 *         输入控件
 */
public class InputEditTextView extends AppCompatEditText {

	private OnEditTextKeyboardBackListener onEditTextKeyboardBackListener;

	public InputEditTextView(@NonNull Context context) {
		super(context);
		init();
	}

	public InputEditTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		// initInputSpecialSymbolListener();
	}

	// 初始化三方表情和特殊符号过滤设置
	private void initInputSpecialSymbolListener() {
		SpecialEmojiHelper.setProhibitEmoji(this, new SpecialEmojiHelper.OnSpecialEmojiListener() {

			@Override public void onSpecialEmoji(int code, CharSequence source) {
				if (code == THIRD_EMOJI) {
					Toast.makeText(getContext(), "群组昵称不能含有第三方表情", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getContext(), "群组昵称不能含有特殊字符", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (onEditTextKeyboardBackListener != null) onEditTextKeyboardBackListener.onKeyboardBack();
		}
		return true;
	}

	public void setOnEditTextKeyboardBackListener(OnEditTextKeyboardBackListener onEditTextKeyboardBackListener) {
		this.onEditTextKeyboardBackListener = onEditTextKeyboardBackListener;
	}

	public interface OnEditTextKeyboardBackListener {

		void onKeyboardBack();
	}
}
