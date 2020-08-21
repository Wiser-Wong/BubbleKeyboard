package com.wiser.bubblekeyboard.record;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.wiser.bubblekeyboard.R;
import com.wiser.bubblekeyboard.helper.HandlerHelper;

import java.text.MessageFormat;

/**
 * @author Wiser
 * 
 *         录音SVGA动画
 */
public class RecordAnimView extends LinearLayout implements View.OnTouchListener {

	private static final int		MAX_RECORD_TIME	= 60;	// 最大录制时间

	private SVGAParser				parser;					// SVGA解析器

	private SVGAImageView			svgaImageView;			// SVGA图片控件

	private TextView				tvTime;					// 录制时间控件

	private int						time;					// 记录时长

	private OnRecordActionListener	onRecordActionListener;

	public RecordAnimView(@NonNull Context context) {
		super(context);
		init();
	}

	public RecordAnimView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	@SuppressLint("ClickableViewAccessibility") private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.record_anim_view, this, true);
		setOrientation(LinearLayout.VERTICAL);
		svgaImageView = findViewById(R.id.record_svga_image);
		tvTime = findViewById(R.id.tv_record_time);

		svgaImageView.setOnTouchListener(this);

		parser = new SVGAParser(getContext());
		parser.decodeFromAssets("record_playing.svga", new SVGAParser.ParseCompletion() {

			@Override public void onComplete(@NonNull SVGAVideoEntity svgaVideoEntity) {
				if (svgaImageView != null) {
					svgaImageView.setVideoItem(svgaVideoEntity);
					svgaImageView.stepToFrame(0, false);
				}
			}

			@Override public void onError() {

			}
		});
	}

	public void start() {
		if (svgaImageView != null) svgaImageView.stepToFrame(0, true);
	}

	public void stop() {
		if (svgaImageView != null) svgaImageView.stepToFrame(0, false);
	}

	public int getTime() {
		return time;
	}

	@Override protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		HandlerHelper.mainLooper().getHandler().removeCallbacks(runnable);
		parser = null;
		svgaImageView = null;
		tvTime = null;
		time = 0;
		onRecordActionListener = null;
	}

	@Override public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (onRecordActionListener != null) onRecordActionListener.onStartRecord();
				start();
				HandlerHelper.mainLooper().execute(runnable, 1000);
				break;
			case MotionEvent.ACTION_UP:
				if (onRecordActionListener != null) onRecordActionListener.onStopRecord();
				stop();
				HandlerHelper.mainLooper().getHandler().removeCallbacks(runnable);
				time = 0;
				if (tvTime != null) tvTime.setText(MessageFormat.format("{0}s", time));
				break;
		}
		return true;
	}

	private Runnable runnable = new Runnable() {

		@Override public void run() {
			if (time >= MAX_RECORD_TIME) {
				if (onRecordActionListener != null) onRecordActionListener.onStopRecord();
				time = 0;
				stop();
				HandlerHelper.mainLooper().getHandler().removeCallbacks(runnable);
				return;
			}
			time++;
			if (onRecordActionListener != null) onRecordActionListener.onRecordTime(time);
			if (tvTime != null) tvTime.setText(MessageFormat.format("{0}s", time));
			HandlerHelper.mainLooper().execute(runnable, 1000);
		}
	};

	public void setOnRecordActionListener(OnRecordActionListener onRecordActionListener) {
		this.onRecordActionListener = onRecordActionListener;
	}

	public interface OnRecordActionListener {

		void onStartRecord();

		void onRecordTime(int time);

		void onStopRecord();
	}
}
