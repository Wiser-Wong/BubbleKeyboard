package com.wiser.bubblekeyboard;

import com.wiser.bubblekeyboard.keyboard.KeyboardFunctionController;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

/**
 * @author Wiser
 * 
 *         键盘弹窗
 */
public class KeyboardDialogFragment extends DialogFragment {

	public static KeyboardDialogFragment showKeyboardDialog() {
		KeyboardDialogFragment fragment = new KeyboardDialogFragment();
		return fragment;
	}

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, R.style.BubbleDialogTheme);
	}

	@Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (getDialog() != null) {
			// 去除标题栏
			getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
			// 设置点击空白处是否关闭Dialog
			getDialog().setCanceledOnTouchOutside(true);
			// 设置背景透明 显示弹窗弧度
			if (getDialog().getWindow() != null) {
				getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			}
		}

		View view = inflater.inflate(R.layout.keyboard_dialog, container, false);
		KeyboardFunctionController keyboardFunctionController = view.findViewById(R.id.keyboard_controller);
		keyboardFunctionController.initInputClickStateShowUi();

		// 遮挡状态栏
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		}
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
		// view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
		// View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
		// View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		// }

		return view;
	}

	@Override public void onStart() {
		super.onStart();
		if (getDialog() != null) {
			Window window = getDialog().getWindow();
			if (window != null && getActivity() != null) {
				WindowManager.LayoutParams wlp = window.getAttributes();
				wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
				wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
				wlp.gravity = Gravity.BOTTOM;
				window.setAttributes(wlp);
			}
		}
	}
}
