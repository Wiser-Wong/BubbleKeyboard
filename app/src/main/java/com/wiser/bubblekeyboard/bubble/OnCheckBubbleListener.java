package com.wiser.bubblekeyboard.bubble;

import android.view.View;

/**
 * @author Wiser
 * 
 *         选择监听器
 */
public interface OnCheckBubbleListener {

	void onCheckBubble(BubbleAdapter bubbleAdapter,View view, BubbleData bubbleData, int position,boolean isChecked,boolean isNoBubble);

}
