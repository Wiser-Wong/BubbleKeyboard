package com.wiser.bubblekeyboard.helper;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @author wangxy
 * @version 版本
 */
public class InputHelper {

	private InputMethodManager manager;

	public InputHelper(Context context) {
		if (manager == null) manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	/**
	 * 隐藏软键盘
	 */
	public void hideSoftInput(View view) {
		if (view == null || manager == null) return;
		manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/**
	 * 显示软键盘
	 */
	public void showSoftInput(EditText editText) {
		if (editText == null || manager == null) return;
		manager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
	}

	/**
	 * 强制打开键盘
	 * @param rootView
	 */
	public void showSoftInput(View rootView) {
		if (rootView == null || manager == null) return;
		if (!manager.isActive(rootView)) {
			manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public void detach() {
		manager = null;
	}
}
