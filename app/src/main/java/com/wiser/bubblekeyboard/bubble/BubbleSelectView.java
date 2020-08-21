package com.wiser.bubblekeyboard.bubble;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wiser.bubblekeyboard.utils.ScreenUtil;
import com.wiser.bubblekeyboard.utils.SpaceItemDecoration;

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
		LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
		SpaceItemDecoration decoration = new SpaceItemDecoration(ScreenUtil.dp2px(getContext(),10),ScreenUtil.dp2px(getContext(),4),ScreenUtil.dp2px(getContext(),4));
		//设置第一个item没有左边距和最后一个item没有右边距
//		decoration.setPaddingEnd(false);
//		decoration.setPaddingStart(false);
		addItemDecoration(decoration);
		setLayoutManager(manager);
		setAdapter(bubbleAdapter = new BubbleAdapter(getContext()));
	}

	public void setItems(List<BubbleData> bubbleDataList) {
		if (bubbleAdapter != null) bubbleAdapter.setItems(bubbleDataList);
	}

	public void setOnCheckBubbleListener(OnCheckBubbleListener onCheckBubbleListener) {
		if (bubbleAdapter != null) bubbleAdapter.setOnCheckBubbleListener(onCheckBubbleListener);
	}
}
