package com.wiser.bubblekeyboard.record;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.wiser.bubblekeyboard.R;

/**
 * @author Wiser
 * 
 *         录音语音播放页面
 */
public class RecordVoicePlayView extends ConstraintLayout implements View.OnClickListener {

	private OnRecordVoiceResultListener	onRecordVoiceResultListener;

	private AppCompatImageView			ivRecordPlay;

	public RecordVoicePlayView(Context context) {
		super(context);
		init();
	}

	public RecordVoicePlayView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.record_voice_play_view, this, true);
		ivRecordPlay = findViewById(R.id.iv_record_play_icon);

		findViewById(R.id.tv_record_again).setOnClickListener(this);
		findViewById(R.id.tv_record_issue).setOnClickListener(this);
		findViewById(R.id.iv_record_play_icon).setOnClickListener(this);
	}

	@Override public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_record_again:// 重录
				if (onRecordVoiceResultListener != null) onRecordVoiceResultListener.onAgainRecord();
				break;
			case R.id.tv_record_issue:// 发布
				if (onRecordVoiceResultListener != null) onRecordVoiceResultListener.onIssueRecord();
				break;
			case R.id.iv_record_play_icon:// 播放按钮
				if (onRecordVoiceResultListener != null) onRecordVoiceResultListener.onPlayRecord();
				break;
		}
	}

	/**
	 * 设置录音播放按钮颜色值
	 * 
	 * @param color
	 *            颜色
	 */
	public void setRecordPlayColor(String color) {
		if (ivRecordPlay != null) ivRecordPlay.setColorFilter(Color.parseColor("#ffff00"));
	}

	@Override protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		onRecordVoiceResultListener = null;
	}

	public void setOnRecordVoiceResultListener(OnRecordVoiceResultListener onRecordVoiceResultListener) {
		this.onRecordVoiceResultListener = onRecordVoiceResultListener;
	}

	public interface OnRecordVoiceResultListener {

		void onAgainRecord();

		void onIssueRecord();

		void onPlayRecord();
	}
}
