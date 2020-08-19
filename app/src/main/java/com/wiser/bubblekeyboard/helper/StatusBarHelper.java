package com.wiser.bubblekeyboard.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.FragmentActivity;

import java.lang.reflect.Field;

/**
 * @author Wiser
 * 
 *         状态栏帮助类
 */
public class StatusBarHelper {

	/**
	 * 获取手机状态栏高度
	 */
	@SuppressLint("PrivateApi") public static int getStatusBarHeight(Context context) {
		Class<?> c;
		Object obj;
		Field field;
		int x, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return statusBarHeight;
	}

	/**
	 * 全透状态栏
	 */
	public static void setStatusBarFullTransparent(FragmentActivity activity) {
		if (activity == null) return;
		if (Build.VERSION.SDK_INT >= 21) {// 21表示5.0
			Window window = activity.getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
		} else if (Build.VERSION.SDK_INT >= 19) {// 19表示4.4
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 虚拟键盘也透明
			// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}

	/**
	 * 获取虚拟按键的高度 1. 全面屏下 1.1 开启全面屏开关-返回0 1.2 关闭全面屏开关-执行非全面屏下处理方式 2. 非全面屏下 2.1
	 * 没有虚拟键-返回0 2.1 虚拟键隐藏-返回0 2.2 虚拟键存在且未隐藏-返回虚拟键实际高度
	 */
	public static int getNavigationBarHeightRoom(Context context) {
		if (navigationGestureEnabled(context)) {
			return 0;
		}
		return getCurrentNavigationBarHeight(((Activity) context));
	}

	/**
	 * 全面屏（是否开启全面屏开关 0 关闭 1 开启）
	 *
	 * @param context
	 * @return
	 */
	public static boolean navigationGestureEnabled(Context context) {
		int val = 0;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
			val = Settings.Global.getInt(context.getContentResolver(), getDeviceInfo(), 0);
		}
		return val != 0;
	}

	/**
	 * 获取设备信息（目前支持几大主流的全面屏手机，亲测华为、小米、oppo、魅族、vivo都可以）
	 *
	 * @return
	 */
	public static String getDeviceInfo() {
		String brand = Build.BRAND;
		if (TextUtils.isEmpty(brand)) return "navigationbar_is_min";

		if (brand.equalsIgnoreCase("HUAWEI")) {
			return "navigationbar_is_min";
		} else if (brand.equalsIgnoreCase("XIAOMI")) {
			return "force_fsg_nav_bar";
		} else if (brand.equalsIgnoreCase("VIVO")) {
			return "navigation_gesture_on";
		} else if (brand.equalsIgnoreCase("OPPO")) {
			return "navigation_gesture_on";
		} else {
			return "navigationbar_is_min";
		}
	}

	/**
	 * 非全面屏下 虚拟键实际高度(隐藏后高度为0)
	 *
	 * @param activity
	 * @return
	 */
	public static int getCurrentNavigationBarHeight(Activity activity) {
		if (isNavigationBarShown(activity)) {
			return getNavigationBarHeight(activity);
		} else {
			return 0;
		}
	}

	/**
	 * 非全面屏下 虚拟按键是否打开
	 *
	 * @param activity
	 * @return
	 */
	public static boolean isNavigationBarShown(Activity activity) {
		// 虚拟键的view,为空或者不可见时是隐藏状态
		View view = activity.findViewById(android.R.id.navigationBarBackground);
		if (view == null) {
			return false;
		}
		int visible = view.getVisibility();
		if (visible == View.GONE || visible == View.INVISIBLE) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 非全面屏下 虚拟键高度(无论是否隐藏)
	 *
	 * @param context
	 * @return
	 */
	public static int getNavigationBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

}
