package com.wiser.bubblekeyboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.wiser.bubblekeyboard.bubble.BubbleDialogFragment;
import com.wiser.bubblekeyboard.helper.HandlerHelper;
import com.wiser.bubblekeyboard.helper.KeyboardHelper;
import com.wiser.bubblekeyboard.keyboard.KeyboardFunctionController1;
import com.wiser.bubblekeyboard.keyboard.OnKeyboardBubbleRecoverListener;
import com.wiser.bubblekeyboard.utils.FragmentTool;

import static com.wiser.bubblekeyboard.common.KeyboardConstants.KEYBOARD_OR_VOICE_KEY;
import static com.wiser.bubblekeyboard.common.KeyboardConstants.KEYBOARD_TYPE;

/**
 * @author Wiser
 * 
 *         键盘弹窗
 */
public class KeyboardBubbleActivity extends FragmentActivity implements OnKeyboardBubbleRecoverListener {

	private KeyboardFunctionController1 keyboardFunctionController;

	/**
	 * 显示Dialog Activity
	 * 
	 * @param activity
	 */
	public static void showKeyboardDialog(Activity activity, int type) {
		if (activity == null) return;
		Intent intent = new Intent(activity, KeyboardBubbleActivity.class);
		intent.putExtra(KEYBOARD_OR_VOICE_KEY, type);
		activity.startActivity(intent);
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
			if (getIntent() != null) {
				if (getIntent().getIntExtra(KEYBOARD_OR_VOICE_KEY, KEYBOARD_TYPE) == KEYBOARD_TYPE) {
					keyboardFunctionController.initInputClickStateShowUi();
				} else {
					keyboardFunctionController.initVoiceClickStateShowUi();
				}
			}
		}
	}

	@Override public void onRecover() {
		HandlerHelper.mainLooper().execute(new Runnable() {

			@Override public void run() {
				finish();
			}
		}, 300);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.activity_bottom_exit);
	}

	@Override public void onBackPressed() {
		// 弹窗显示时，返回处理
		BubbleDialogFragment fragment = (BubbleDialogFragment) FragmentTool.findFragment(this, BubbleDialogFragment.class.getSimpleName());
		if (fragment != null) fragment.dismissAllowingStateLoss();
		else {
			// 键盘未显示的时候，返回处理
			if (keyboardFunctionController != null) keyboardFunctionController.resetUi();
			onRecover();
		}
	}

	@Override protected void onDestroy() {
		if (KeyboardHelper.bind() != null) KeyboardHelper.bind().onDetach();
		super.onDestroy();
	}
}
