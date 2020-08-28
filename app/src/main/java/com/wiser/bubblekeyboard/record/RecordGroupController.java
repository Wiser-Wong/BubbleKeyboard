package com.wiser.bubblekeyboard.record;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wiser.bubblekeyboard.R;
import com.wiser.bubblekeyboard.helper.KeyboardHelper;

/**
 * @author Wiser
 * 
 *         录音组合控件
 */
public class RecordGroupController extends FrameLayout {

	private RecordAnimView		recordAnimView;

	private RecordVoicePlayView	recordVoicePlayView;

	public RecordGroupController(@NonNull Context context) {
		super(context);
		init();
	}

	public RecordGroupController(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.keyboard_record_group_view, this, true);
		recordAnimView = findViewById(R.id.record_view);
		recordVoicePlayView = findViewById(R.id.record_play_view);

		initListener();
	}

	// 监听
	private void initListener() {

		initRecordListener();

		initRecordResultListener();

	}

	// 录制监听
	private void initRecordListener() {
		recordAnimView.setOnRecordActionListener(new RecordAnimView.OnRecordActionListener() {

			@Override public void onStartRecord() {// 开始录制
				if (KeyboardHelper.bind() != null) KeyboardHelper.bind().onActionRecordStart();
			}

			@Override public void onRecordTime(int time) {

			}

			@Override public void onStopRecord() {// 停止录制
				if (KeyboardHelper.bind() != null) KeyboardHelper.bind().onActionRecordStop();
				switchRecordLayoutUi(false);
			}
		});
	}

	// 录制结果监听
	private void initRecordResultListener() {
		recordVoicePlayView.setOnRecordVoiceResultListener(new RecordVoicePlayView.OnRecordVoiceResultListener() {

			@Override public void onAgainRecord() { // 重录
				if (KeyboardHelper.bind() != null) KeyboardHelper.bind().onActionAgainRecord();
				switchRecordLayoutUi(true);
			}

			@Override public void onIssueRecord() { // 发布
				if (KeyboardHelper.bind() != null) KeyboardHelper.bind().onActionIssueVoice();
			}

			@Override public void onPlayRecord() { // 播放本地录音
				if (KeyboardHelper.bind() != null) KeyboardHelper.bind().onActionPlayVoice();
			}
		});
	}

	/**
	 * 切换录制布局
	 * 
	 * @param isShowRecordView
	 *            是否显示录音View
	 */
	private void switchRecordLayoutUi(boolean isShowRecordView) {
		if (isShowRecordView) {
			if (recordAnimView != null && recordAnimView.getVisibility() == GONE) recordAnimView.setVisibility(VISIBLE);
			if (recordVoicePlayView != null && recordVoicePlayView.getVisibility() == VISIBLE) recordVoicePlayView.setVisibility(GONE);
		} else {
			if (recordAnimView != null && recordAnimView.getVisibility() == VISIBLE) recordAnimView.setVisibility(GONE);
			if (recordVoicePlayView != null && recordVoicePlayView.getVisibility() == GONE) recordVoicePlayView.setVisibility(VISIBLE);
		}
	}

	@Override protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		recordAnimView = null;
		recordVoicePlayView = null;
	}
}
