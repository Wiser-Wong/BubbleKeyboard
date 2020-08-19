package com.wiser.bubblekeyboard.bubble;

import android.view.View;

/**
 * @author Wiser
 * 
 *         选择监听器
 */
public interface OnCheckBubbleListener {

	void onCheckBubble(View view, int position,boolean isChecked,boolean isNoBubble);

}
