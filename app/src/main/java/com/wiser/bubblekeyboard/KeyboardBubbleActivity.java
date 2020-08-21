package com.wiser.bubblekeyboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.wiser.bubblekeyboard.bubble.BubbleDialogFragment;
import com.wiser.bubblekeyboard.helper.HandlerHelper;
import com.wiser.bubblekeyboard.keyboard.KeyboardFunctionController;
import com.wiser.bubblekeyboard.utils.FragmentTool;

/**
 * @author Wiser
 * 
 *         键盘弹窗
 */
public class KeyboardBubbleActivity extends FragmentActivity implements OnKeyboardBubbleRecoverListener {

	private KeyboardFunctionController keyboardFunctionController;

	/**
	 * 显示Dialog Activity
	 * 
	 * @param activity
	 */
	public static void showKeyboardDialog(Activity activity) {
		if (activity == null) return;
		activity.startActivity(new Intent(activity, KeyboardBubbleActivity.class));
	}

	@Override protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.keyboard_dialog);

		keyboardFunctionController = findViewById(R.id.keyboard_controller);

		keyboardFunctionController.setOnKeyboardBubbleRecoverListener(this);
	}

	@Override public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			keyboardFunctionController.initInputClickStateShowUi();
		}
	}

	@Override public void onRecover() {
		if (!keyboardFunctionController.isDialogShowHaveKeyboard()) HandlerHelper.mainLooper().execute(new Runnable() {

			@Override public void run() {
				finish();
			}
		}, 300);
	}

	@Override public void onBackPressed() {
		BubbleDialogFragment fragment = (BubbleDialogFragment) FragmentTool.findFragment(this, BubbleDialogFragment.class.getSimpleName());
		if (fragment != null) fragment.dismissAllowingStateLoss();
		else super.onBackPressed();
	}
}
