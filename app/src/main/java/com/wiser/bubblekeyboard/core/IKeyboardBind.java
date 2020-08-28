package com.wiser.bubblekeyboard.core;

import android.content.Context;

import androidx.appcompat.widget.AppCompatImageView;

import com.wiser.bubblekeyboard.bubble.BubbleData;
import com.wiser.bubblekeyboard.bubble.BubbleSelectView;
import com.wiser.bubblekeyboard.bubble.OnCheckBubbleResultListener;

/**
 * @author Wiser
 * 
 *         暴露接口
 */
public interface IKeyboardBind {

	/**
	 * 发布语音
	 */
	void onActionIssueVoice();

	/**
	 * 发布文字内容
	 * 
	 * @param content
	 *            文字内容
	 */
	void onActionIssueText(String content);

	/**
	 * 播放语音文件
	 */
	void onActionPlayVoice();

	/**
	 * 开始录制
	 */
	void onActionRecordStart();

	/**
	 * 停止录制
	 */
	void onActionRecordStop();

	/**
	 * 重录
	 */
	void onActionAgainRecord();

	/**
	 * 气泡选择事件
	 * 
	 * @param context
	 * @param bubbleData
	 *            气泡数据
	 * @param onCheckBubbleResultListener
	 *            选择气泡结果监听器
	 */
	void onActionBubbleChecked(Context context, BubbleData bubbleData, OnCheckBubbleResultListener onCheckBubbleResultListener);

	/**
	 * 设置气泡列表数据
	 * 
	 * @param bubbleSelectView
	 *            气泡列表控件
	 */
	void initKeyboardBubbleData(BubbleSelectView bubbleSelectView);

	/**
	 * 设置输入框.9背景
	 * 
	 * @param ivInputBackground
	 *            图片控件
	 */
	void setInputBackground(AppCompatImageView ivInputBackground);

	/**
	 * 销毁
	 */
	void onDetach();

	IKeyboardBind defaultBind = new KeyboardBind();
}
