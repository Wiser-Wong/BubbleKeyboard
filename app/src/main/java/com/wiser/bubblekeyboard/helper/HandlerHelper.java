package com.wiser.bubblekeyboard.helper;

import java.util.concurrent.Executor;

import android.os.Handler;
import android.os.Looper;

/**
 * @author Wiser
 * @version 版本
 */
public class HandlerHelper implements Executor {

	private static final class HandlerHolder {

		private static final HandlerHelper instance = new HandlerHelper();
	}

	public static HandlerHelper mainLooper() {
		return HandlerHolder.instance;
	}

	private final Handler handler = new Handler(Looper.getMainLooper());

	@Override public void execute(Runnable runnable) {
		handler.post(runnable);
	}

	public void execute(Runnable runnable, long delayMillis) {
		handler.postDelayed(runnable, delayMillis);
	}

	public Handler getHandler() {
		return handler;
	}
}