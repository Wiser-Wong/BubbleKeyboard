package com.wiser.bubblekeyboard.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class FragmentTool {

	/**
	 * @param activity
	 *            参数
	 * @param tagName
	 *            参数
	 * @return 返回值
	 */
	public static Fragment findFragment(FragmentActivity activity, String tagName) {
		if (tagName == null || "".equals(tagName)) return null;
		if (activity == null) return null;
		return activity.getSupportFragmentManager().findFragmentByTag(tagName);
	}

}
