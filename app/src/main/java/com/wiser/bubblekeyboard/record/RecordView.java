package com.wiser.bubblekeyboard.record;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wiser.bubblekeyboard.R;

import java.lang.ref.WeakReference;

/**
 * @author Wiser
 * 
 *         录音控件
 */
public class RecordView extends View {

	private static final int	UPDATE_PROGRESS		= 1000;			// 更新进度code

	private static final int	DELAY				= 10;			// 延迟时长

	private static final int	ORDER_TYPE			= 2000;			// 顺序

	private static final int	FLASHBACK_TYPE		= 2001;			// 倒叙

	private int					lineType			= ORDER_TYPE;	// 录音倒计时UI类型

	private Drawable			recordDrawable;						// 录音按钮

	private float				recordHeight		= dp2px(100);	// 录音高度

	private float				recordPadding		= 15;			// 录音与倒计时线边距

	private float				recordLineWidth		= 5;			// 录音倒计时线宽度

	private RectF				recordRect;							// 录音按钮矩阵

	private RectF				recordLineRect;						// 录音倒计时线矩阵

	private Paint				recordLinePaint;					// 录音倒计时线画笔

	private int					recordLineColor		= Color.RED;	// 录音倒计时颜色

	private float				currentProgress;					// 当前进度

	private float				maxProgressTime		= 5;			// 最大进度时间 单位（s）

	private float				currentProgressTime;				// 当前进度时间 单位（s）

	private float				lastProgressTime	= -1;			// 记录上一次时间

	private RecordHandler		recordHandler;

	private OnRecordListener	onRecordListener;

	public RecordView(@NonNull Context context) {
		super(context);
		init(null);
	}

	public RecordView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public void setOnRecordListener(OnRecordListener onRecordListener) {
		this.onRecordListener = onRecordListener;
	}

	@SuppressLint("UseCompatLoadingForDrawables") private void init(AttributeSet attrs) {
		TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.RecordView);
		// 录音按钮图片id
		int recordDrawableId = ta.getResourceId(R.styleable.RecordView_rv_drawable, -1);
		recordPadding = ta.getDimension(R.styleable.RecordView_rv_padding, recordPadding);
		recordLineWidth = ta.getDimension(R.styleable.RecordView_rv_line_width, recordLineWidth);
		recordLineColor = ta.getColor(R.styleable.RecordView_rv_line_color, recordLineColor);
		maxProgressTime = ta.getInteger(R.styleable.RecordView_rv_max_progress_time, (int) maxProgressTime);
		lineType = ta.getInt(R.styleable.RecordView_type, ORDER_TYPE);
		ta.recycle();

		if (recordDrawableId > 0) recordDrawable = getResources().getDrawable(recordDrawableId);
		if (recordDrawable != null) {
			recordHeight = Math.max(recordDrawable.getIntrinsicHeight(), recordDrawable.getIntrinsicWidth());
		}

		recordRect = new RectF();
		recordLineRect = new RectF();

		initRecordPaint();

		recordHandler = new RecordHandler(this);

		// 线类型
		if (lineType == ORDER_TYPE) currentProgress = 0;
		else currentProgress = maxProgressTime;
	}

	private void initRecordPaint() {
		recordLinePaint = new Paint();
		recordLinePaint.setStyle(Paint.Style.STROKE);
		recordLinePaint.setStrokeCap(Paint.Cap.ROUND);
		recordLinePaint.setAntiAlias(true);
		recordLinePaint.setDither(true);
		recordLinePaint.setColor(recordLineColor);
		recordLinePaint.setTextAlign(Paint.Align.CENTER);
		recordLinePaint.setStrokeWidth(recordLineWidth);
	}

	@Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// 初始化矩阵
		recordRect.set(getPaddingLeft() + recordLineWidth + recordPadding, getPaddingTop() + recordLineWidth + recordPadding, getPaddingLeft() + recordLineWidth + recordPadding + recordHeight,
				getPaddingTop() + recordLineWidth + recordPadding + recordHeight);
		recordLineRect.set(recordRect.left - recordPadding - recordLineWidth / 2, recordRect.top - recordPadding - recordLineWidth / 2, recordRect.right + recordPadding + recordLineWidth / 2,
				recordRect.bottom + recordPadding + recordLineWidth / 2);
	}

	@Override protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();

		// 话录音按钮
		if (recordDrawable != null) {
			recordDrawable.setBounds((int) recordRect.left, (int) recordRect.top, (int) recordRect.right, (int) recordRect.bottom);
			recordDrawable.draw(canvas);
		}

		// 画倒计时线
		if (lineType == ORDER_TYPE) canvas.drawArc(recordLineRect, -90, 360 * currentProgress / maxProgressTime, false, recordLinePaint);
		else canvas.drawArc(recordLineRect, -90, -360 * currentProgress / maxProgressTime, false, recordLinePaint);

		canvas.restore();
	}

	@Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int height = (int) (heightMode == MeasureSpec.UNSPECIFIED ? (recordHeight + recordPadding * 2 + recordLineWidth * 2 + getPaddingTop() + getPaddingBottom())
				: heightMode == MeasureSpec.EXACTLY ? heightSize : Math.min(recordHeight + recordPadding * 2 + recordLineWidth * 2 + getPaddingTop() + getPaddingBottom(), heightSize));
		setMeasuredDimension(height, height);
	}

	public void setProgress(float progress) {
		this.currentProgress = progress;
		invalidate();
	}

	public void setCurrentProgress(float currentProgress) {
		this.currentProgress = currentProgress;
	}

	private void setCurrentProgressTime(float currentProgressTime) {
		this.currentProgressTime = currentProgressTime;
	}

	public float getCurrentProgressTime() {
		return currentProgressTime;
	}

	public void setMaxProgressTime(float maxProgressTime) {
		this.maxProgressTime = maxProgressTime;
	}

	public float getCurrentProgress() {
		return currentProgress;
	}

	public float getMaxProgressTime() {
		return maxProgressTime;
	}

	@Override public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:// 按下
				recordHandler.sendEmptyMessageDelayed(UPDATE_PROGRESS, DELAY);
				if (onRecordListener != null) onRecordListener.onStartRecord();
				break;
			case MotionEvent.ACTION_UP:// 抬起
				recordHandler.removeMessages(UPDATE_PROGRESS);
				if (lineType == ORDER_TYPE) setProgress(0);
				else setProgress(maxProgressTime);
				if (onRecordListener != null) onRecordListener.onFinishRecord();
				break;
		}
		return true;
	}

	private static class RecordHandler extends Handler {

		private WeakReference<RecordView> reference;

		public RecordHandler(RecordView recordView) {
			reference = new WeakReference<>(recordView);
		}

		@Override public void handleMessage(@NonNull Message msg) {
			super.handleMessage(msg);
			if (msg.what == UPDATE_PROGRESS) {
				if (reference != null && reference.get() != null) {
					if (reference.get().lineType == ORDER_TYPE) { // 顺序
						if (reference.get().currentProgress >= reference.get().maxProgressTime) {
							reference.get().currentProgress = 0;
							reference.get().lastProgressTime = 0;
							reference.get().setProgress(reference.get().currentProgress);
							removeMessages(UPDATE_PROGRESS);
							if (reference.get().onRecordListener != null) reference.get().onRecordListener.onFinishRecord();
						} else {
							reference.get().currentProgress = reference.get().currentProgress + (float) 1 / 60;
							reference.get().setProgress(reference.get().currentProgress);
							if (reference.get().lastProgressTime != (int) (reference.get().currentProgress)) {
								if (reference.get().onRecordListener != null) reference.get().onRecordListener.onProgress((int) (reference.get().currentProgress));
							}
							reference.get().lastProgressTime = (int) (reference.get().currentProgress);
							sendEmptyMessageDelayed(UPDATE_PROGRESS, DELAY);
						}
					} else { // 倒叙
						if (reference.get().currentProgress <= 0) {
							reference.get().currentProgress = reference.get().maxProgressTime;
							reference.get().lastProgressTime = reference.get().maxProgressTime;
							reference.get().setProgress(reference.get().currentProgress);
							removeMessages(UPDATE_PROGRESS);
							if (reference.get().onRecordListener != null) reference.get().onRecordListener.onFinishRecord();
						} else {
							reference.get().currentProgress = reference.get().currentProgress - (float) 1 / 60;
							reference.get().setProgress(reference.get().currentProgress);
							if (reference.get().lastProgressTime != Math.ceil(reference.get().currentProgress)) {
								if (reference.get().onRecordListener != null) reference.get().onRecordListener.onProgress((int) Math.ceil(reference.get().currentProgress));
							}
							reference.get().lastProgressTime = (float) Math.ceil(reference.get().currentProgress);
							sendEmptyMessageDelayed(UPDATE_PROGRESS, DELAY);
						}
					}
				}
			}
		}
	}

	@Override protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (recordHandler != null) recordHandler.removeCallbacksAndMessages(null);
		recordHandler = null;
		recordDrawable = null;
		recordRect = null;
		recordLineRect = null;
		recordLinePaint = null;
		onRecordListener = null;
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
	}
}
