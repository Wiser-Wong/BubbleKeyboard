package com.wiser.bubblekeyboard.keyboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.wiser.bubblekeyboard.R;

/**
 * @author Wiser
 * 
 *         切换键盘和录音控件
 */
public class SwitchKeyboardAndVoiceView extends AppCompatImageView implements View.OnClickListener {

	private boolean								isKeyboard	= true;

	private boolean								recordInitKeyboard;					// 记录初始isKeyboard值

	private OnSwitchKeyboardAndVoiceListener	onSwitchKeyboardAndVoiceListener;

	private int									drawableKeyboardId;					// 键盘图片id

	private int									drawableVoiceId;					// 语音图片id

	public SwitchKeyboardAndVoiceView(Context context) {
		super(context);
		init(null);
	}

	public SwitchKeyboardAndVoiceView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	private void init(AttributeSet attrs) {

		TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SwitchKeyboardAndVoiceView);
		recordInitKeyboard = isKeyboard = ta.getBoolean(R.styleable.SwitchKeyboardAndVoiceView_skavv_keyboard_default_show, true);
		drawableKeyboardId = ta.getResourceId(R.styleable.SwitchKeyboardAndVoiceView_skavv_keyboard_drawable, -1);
		drawableVoiceId = ta.getResourceId(R.styleable.SwitchKeyboardAndVoiceView_skavv_voice_drawable, -1);
		ta.recycle();

		if (drawableKeyboardId != -1 && isKeyboard) {
			setBackgroundResource(drawableKeyboardId);
		} else if (drawableVoiceId != -1) {
			setBackgroundResource(drawableVoiceId);
		}
		setOnClickListener(this);
	}

	public boolean isKeyboard() {
		return isKeyboard;
	}

	public void setOnSwitchKeyboardAndVoiceListener(OnSwitchKeyboardAndVoiceListener onSwitchKeyboardAndVoiceListener) {
		this.onSwitchKeyboardAndVoiceListener = onSwitchKeyboardAndVoiceListener;
	}

	// 重置
	public void reset() {
		isKeyboard = recordInitKeyboard;
		if (recordInitKeyboard) {
			if (drawableKeyboardId != -1) setBackgroundResource(drawableKeyboardId);
		} else {
			if (drawableVoiceId != -1) setBackgroundResource(drawableVoiceId);
		}
	}

	@Override public void onClick(View v) {
		updateSwitch();
		if (onSwitchKeyboardAndVoiceListener != null) onSwitchKeyboardAndVoiceListener.onSwitch(v, isKeyboard);
	}

	// 更新切换
	public void updateSwitch() {
		if (isKeyboard) {
			if (drawableVoiceId != -1) setBackgroundResource(drawableVoiceId);
			isKeyboard = false;
		} else {
			if (drawableKeyboardId != -1) setBackgroundResource(drawableKeyboardId);
			isKeyboard = true;
		}
	}
}
