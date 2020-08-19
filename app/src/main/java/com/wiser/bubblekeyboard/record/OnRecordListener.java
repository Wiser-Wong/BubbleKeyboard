package com.wiser.bubblekeyboard.record;

/**
 * @author Wiser
 * 
 *         录制监听
 */
public interface OnRecordListener {

	void onStartRecord();

	void onProgress(int time);

	void onFinishRecord();

}
