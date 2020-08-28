package com.wiser.bubblekeyboard.helper;

import com.wiser.bubblekeyboard.core.IKeyboardBind;

/**
 * @author Wiser
 * 
 *         键盘帮助类
 */
public class KeyboardHelper {

	private static IKeyboardBind iKeyboardBind = null;

	public static void inject(IKeyboardBind iKeyboardBind) {
		KeyboardHelper.iKeyboardBind = iKeyboardBind;
	}

	public static IKeyboardBind bind() {
		if (iKeyboardBind != null) return iKeyboardBind;
		return IKeyboardBind.defaultBind;
	}

	public static void onDetach() {
		KeyboardHelper.iKeyboardBind = null;
	}
}
