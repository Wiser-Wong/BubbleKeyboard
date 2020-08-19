package com.wiser.bubblekeyboard.bubble;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * @author Wiser
 * 
 *         气泡选择控件
 */
public class BubbleSelectView extends RecyclerView {

	private BubbleAdapter bubbleAdapter;

	public BubbleSelectView(@NonNull Context context) {
		super(context);
		init();
	}

	public BubbleSelectView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BubbleSelectView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	// 初始化
	private void init() {
		setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		setAdapter(bubbleAdapter = new BubbleAdapter(getContext()));
	}

	public void setItems(List<BubbleData> bubbleDataList) {
		if (bubbleAdapter != null) bubbleAdapter.setItems(bubbleDataList);
	}

	public void setOnCheckBubbleListener(OnCheckBubbleListener onCheckBubbleListener) {
		if (bubbleAdapter != null) bubbleAdapter.setOnCheckBubbleListener(onCheckBubbleListener);
	}
}
