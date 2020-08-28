package com.wiser.bubblekeyboard.bubble;

import android.graphics.Color;
import android.graphics.PixelFormat;
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

import com.wiser.bubblekeyboard.R;

/**
 * @author Wiser
 *
 *         气泡弹窗
 */
public class BubbleDialogFragment extends DialogFragment{

	private OnBubbleDialogDismiss onBubbleDialogDismiss;

	public static BubbleDialogFragment createBubbleDialog(OnBubbleDialogDismiss onBubbleDialogDismiss) {
		BubbleDialogFragment fragment = new BubbleDialogFragment();
		fragment.setOnBubbleDialogDismiss(onBubbleDialogDismiss);
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

		View view = inflater.inflate(R.layout.keyboard_bubble_dialog, container, false);

		// 遮挡状态栏
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		}

		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismissAllowingStateLoss();
			}
		});

		view.findViewById(R.id.tv_bubble_know).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismissAllowingStateLoss();
			}
		});

		return view;
	}

	@Override public void onStart() {
		if (getDialog() != null) {
			Window window = getDialog().getWindow();
			if (window != null && getActivity() != null) {
				WindowManager.LayoutParams wlp = window.getAttributes();
				wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
				wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
				wlp.gravity = Gravity.CENTER;
				wlp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
				wlp.format = PixelFormat.TRANSLUCENT;// 不设置这个弹出框的透明遮罩显示为黑色
				wlp.dimAmount = 0.6f;
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
					wlp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
				} else {
					wlp.type = WindowManager.LayoutParams.TYPE_PHONE;
				}
				window.setAttributes(wlp);
			}
		}
		super.onStart();
	}

	@Override public void onDetach() {
		super.onDetach();
		 if (onBubbleDialogDismiss != null) onBubbleDialogDismiss.onDismiss();
	}

	public void setOnBubbleDialogDismiss(OnBubbleDialogDismiss onBubbleDialogDismiss) {
		this.onBubbleDialogDismiss = onBubbleDialogDismiss;
	}

	public interface OnBubbleDialogDismiss {

		void onDismiss();
	}

}
