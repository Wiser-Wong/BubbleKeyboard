package com.wiser.bubblekeyboard.keyboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Wiser
 * 
 *         动画透明度背景变化控件
 */
public class AnimAlphaBackgroundView extends FrameLayout {

	private ObjectAnimator	animator1;

	private ObjectAnimator	animator2;

	public AnimAlphaBackgroundView(@NonNull Context context) {
		super(context);
		init();
	}

	public AnimAlphaBackgroundView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		animator1 = ObjectAnimator.ofFloat(this, "alpha", 0, 1);
		animator1.setDuration(200);
		animator2 = ObjectAnimator.ofFloat(this, "alpha", 1, 0);
		animator2.setDuration(200);
		
//		animator1.addListener(new AnimatorListenerAdapter() {
//			@Override
//			public void onAnimationStart(Animator animation) {
//				super.onAnimationStart(animation);
//				setVisibility(VISIBLE);
//			}
//		});
//
//		animator2.addListener(new AnimatorListenerAdapter() {
//			@Override
//			public void onAnimationEnd(Animator animation) {
//				super.onAnimationEnd(animation);
//				setVisibility(GONE);
//			}
//
//			@Override
//			public void onAnimationCancel(Animator animation) {
//				super.onAnimationCancel(animation);
//				setVisibility(GONE);
//			}
//		});
	}

	/**
	 * 设置透明动画
	 * 
	 * @param isShow
	 *            是否显示
	 */
	public void setAnimBackground(boolean isShow) {
		if (isShow) {
			animator1.start();
		} else {
			animator2.start();
		}
	}

	@Override protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (animator1 != null) animator1.cancel();
		animator1 = null;
		if (animator2 != null) animator2.cancel();
		animator2 = null;
	}
}
