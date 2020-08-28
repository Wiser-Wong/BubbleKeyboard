package com.wiser.bubblekeyboard.keyboard;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.wiser.bubblekeyboard.R;
import com.wiser.bubblekeyboard.helper.HandlerHelper;

import java.lang.reflect.Field;

/**
 * @author Wiser
 * 
 *         键盘组件蒙层popwindow
 */
public class KeyboardPopWindow extends PopupWindow {

	private View						popView;

	private KeyboardFunctionController1	keyboardFunctionController;

	public KeyboardPopWindow(Context activity, boolean isKeyboardShow) {
		super(activity);
		if (activity == null) return;

//		if (isKeyboardShow) setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//		else setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//		setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

		popView = LayoutInflater.from(activity).inflate(R.layout.keyboard_pop, null, false);
		setContentView(popView);

		setFocusable(true);

		keyboardFunctionController = popView.findViewById(R.id.keyboard_controller);

		// 设置PopupWindow的背景
		setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7D000000")));
		// 设置进出动画
		setAnimationStyle(R.style.Pop_Animation);
		// 设置PopupWindow是否能响应外部点击事件
		setOutsideTouchable(true);
		// 设置PopupWindow是否能响应点击事件
		setTouchable(true);

		setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			try {
				Field mLayoutInScreen = PopupWindow.class.getDeclaredField("mLayoutInScreen");
				mLayoutInScreen.setAccessible(true);
				mLayoutInScreen.set(this, true);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		keyboardFunctionController.setOnKeyboardBubbleRecoverListener(new OnKeyboardBubbleRecoverListener() {

			@Override public void onRecover() {
				HandlerHelper.mainLooper().execute(new Runnable() {

					@Override public void run() {
						hideKeyboardPop();
					}
				}, 400);
			}
		});

//		getContentView().setOnKeyListener(new View.OnKeyListener() {
//
//			@Override public boolean onKey(View v, int keyCode, KeyEvent event) {
//				if (keyCode == KeyEvent.KEYCODE_BACK) {
//					HandlerHelper.mainLooper().execute(new Runnable() {
//
//						@Override public void run() {
//							hideKeyboardPop();
//						}
//					}, 400);
//					return true;
//				}
//				return false;
//			}
//		});
	}

	/**
	 * 显示键盘popWindow
	 * 
	 * @param isKeyboardShow
	 *            是否显示键盘 true 显示键盘 false 显示录音布局
	 */
	public void showKeyboardPop(final boolean isKeyboardShow) {
		if (!isShowing()) {
			if (popView != null) showAtLocation(popView.getRootView(), Gravity.BOTTOM, 0, 0);
			HandlerHelper.mainLooper().execute(new Runnable() {

				@Override public void run() {
					if (isKeyboardShow) { // 键盘
						if (keyboardFunctionController != null) keyboardFunctionController.initInputClickStateShowUi();
					} else { // 录音
						if (keyboardFunctionController != null) keyboardFunctionController.initVoiceClickStateShowUi();
					}
				}
			}, 200);
		}
	}

	/**
	 * 取消popWindow
	 */
	public void hideKeyboardPop() {
		this.popView = null;
		dismiss();
	}

}
