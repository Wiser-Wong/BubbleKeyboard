package com.wiser.bubblekeyboard.core;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentActivity;

import com.wiser.bubblekeyboard.R;
import com.wiser.bubblekeyboard.bubble.BubbleData;
import com.wiser.bubblekeyboard.bubble.BubbleDialogFragment;
import com.wiser.bubblekeyboard.bubble.BubbleSelectView;
import com.wiser.bubblekeyboard.bubble.OnCheckBubbleResultListener;
import com.wiser.bubblekeyboard.helper.KeyboardHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wiser
 * 
 *         键盘绑定
 */
public class KeyboardBind implements IKeyboardBind {

	/**
	 * 发布语音
	 */
	@Override public void onActionIssueVoice() {

	}

	/**
	 * 发布文字内容
	 *
	 * @param content
	 *            文字内容
	 */
	@Override public void onActionIssueText(String content) {

	}

	/**
	 * 播放语音文件
	 */
	@Override public void onActionPlayVoice() {

	}

	/**
	 * 开始录制
	 */
	@Override public void onActionRecordStart() {

	}

	/**
	 * 停止录制
	 */
	@Override public void onActionRecordStop() {

	}

	/**
	 * 重录
	 */
	@Override public void onActionAgainRecord() {

	}

	/**
	 * 气泡选择事件
	 *
	 * @param context
	 * @param bubbleData
	 *            气泡数据
	 */
	@Override public void onActionBubbleChecked(Context context, BubbleData bubbleData, final OnCheckBubbleResultListener onCheckBubbleResultListener) {
		if (context instanceof FragmentActivity) {
			if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(context)) {
				Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				scanForActivity(context).startActivityForResult(intent, 1);
			} else {
				BubbleDialogFragment.createBubbleDialog(new BubbleDialogFragment.OnBubbleDialogDismiss() {

					@Override public void onDismiss() {
						if (onCheckBubbleResultListener != null) onCheckBubbleResultListener.onCheckBubbleResult();
					}
				}).show(((FragmentActivity) context).getSupportFragmentManager(), BubbleDialogFragment.class.getSimpleName());
			}
		}
	}

	/**
	 * 设置气泡列表数据
	 *
	 * @param bubbleSelectView
	 *            气泡列表组件
	 */
	@Override public void initKeyboardBubbleData(BubbleSelectView bubbleSelectView) {
		if (bubbleSelectView != null) bubbleSelectView.setItems(getData());
	}

	/**
	 * 设置输入框.9背景
	 *
	 * @param ivInputBackground
	 *            图片控件
	 */
	@Override public void setInputBackground(AppCompatImageView ivInputBackground) {
		if (ivInputBackground != null) ivInputBackground.setBackgroundResource(R.drawable.keyboard_bubble_frame_bg);
	}

	/**
	 * 销毁
	 */
	@Override public void onDetach() {
		KeyboardHelper.onDetach();
	}

	private Activity scanForActivity(Context cont) {
		if (cont == null) return null;
		else if (cont instanceof Activity) return (Activity) cont;
		else if (cont instanceof ContextWrapper) return scanForActivity(((ContextWrapper) cont).getBaseContext());

		return null;
	}

	private List<BubbleData> getData() {
		List<BubbleData> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			BubbleData bubbleData = new BubbleData();
			if (i == 0) {
				bubbleData.type = BubbleData.NO_BUBBLE_TYPE;
				bubbleData.isCheck = true;
			} else bubbleData.type = BubbleData.HAVE_BUBBLE_TYPE;
			list.add(bubbleData);
		}
		return list;
	}
}
