package com.wiser.bubblekeyboard.bubble;

import java.lang.ref.WeakReference;

/**
 * @author Wiser
 * 
 *         气泡弹窗业务
 */
public class BubbleDialogBiz implements IBubbleDialogBiz {

	private WeakReference<BubbleDialogFragment> reference;

	public BubbleDialogBiz(BubbleDialogFragment ui) {
		reference = new WeakReference<>(ui);
	}

	@Override public void onDetach() {
		if (reference != null) reference.clear();
		reference = null;
	}
}

interface IBubbleDialogBiz {

	String KEYBOARD_HEIGHT_KEY = "KEYBOARD_HEIGHT_KEY";

	void onDetach();
}
