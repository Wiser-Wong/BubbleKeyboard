package com.wiser.bubblekeyboard.keyboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.wiser.bubblekeyboard.R;

/**
 * @author Wiser
 * 
 *         发布按钮控件
 */
public class IssueTextView extends AppCompatTextView implements View.OnClickListener {

	private int						drawableDfId;

	private int						drawableStId;

	private int						colorDf;

	private int						colorSt;

	private OnIssueClickListener	onIssueClickListener;

	private String					content;				// 发布的内容

	public IssueTextView(Context context) {
		super(context);
		init(null);
	}

	public IssueTextView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public void setOnIssueClickListener(OnIssueClickListener onIssueClickListener) {
		this.onIssueClickListener = onIssueClickListener;
	}

	private void init(AttributeSet attrs) {

		TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.IssueTextView);
		drawableDfId = ta.getResourceId(R.styleable.IssueTextView_itv_drawable_df_id, -1);
		drawableStId = ta.getResourceId(R.styleable.IssueTextView_itv_drawable_st_id, -1);
		colorDf = ta.getColor(R.styleable.IssueTextView_itv_df_color, Color.WHITE);
		colorSt = ta.getColor(R.styleable.IssueTextView_itv_st_color, Color.WHITE);
		ta.recycle();

		setOnClickListener(this);
		updateIssueButtonUi(false);
	}

	/**
	 * 更新发布按钮UI
	 * 
	 * @param isCanIssue
	 *            是否可以发布
	 */
	public void updateIssueButtonUi(boolean isCanIssue) {
		if (isCanIssue) {
			if (drawableStId != -1) setBackgroundResource(drawableStId);
			setTextColor(colorSt);
		} else {
			if (drawableDfId != -1) setBackgroundResource(drawableDfId);
			setTextColor(colorDf);
		}
	}

	/**
	 * 设置发布内容
	 * 
	 * @param content
	 *            发布的内容
	 */
	public void setIssueContent(String content) {
		this.content = content;
	}

	@Override public void onClick(View v) {
		if (onIssueClickListener != null) onIssueClickListener.onIssueClick(v, content);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		onIssueClickListener = null;
	}
}
