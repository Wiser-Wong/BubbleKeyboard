package com.wiser.bubblekeyboard.helper;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Wiser
 * 
 *         软件盘监听帮助类
 */
public class KeyboardListenerHelper implements ViewTreeObserver.OnGlobalLayoutListener {

	public interface SoftKeyboardStateListener {

		void onSoftKeyboardOpened(int keyboardHeightInPx);

		void onSoftKeyboardClosed();
	}

	private final List<SoftKeyboardStateListener>	listeners	= new LinkedList<>();

	private final View								activityRootView;

	private boolean									isSoftKeyboardOpened;

	public KeyboardListenerHelper(View activityRootView) {
		this(activityRootView, false);
	}

	public KeyboardListenerHelper(View activityRootView, boolean isSoftKeyboardOpened) {
		this.activityRootView = activityRootView;
		this.isSoftKeyboardOpened = isSoftKeyboardOpened;
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
	}

	@Override public void onGlobalLayout() {
		final Rect r = new Rect();
		activityRootView.getWindowVisibleDisplayFrame(r);

		final int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top) - StatusBarHelper.getStatusBarHeight(activityRootView.getContext())
				- StatusBarHelper.getNavigationBarHeightRoom(scanForActivity(activityRootView.getContext()));
		if (!isSoftKeyboardOpened && heightDiff > 100) {
			isSoftKeyboardOpened = true;
			notifyOnSoftKeyboardOpened(heightDiff);
		} else if (isSoftKeyboardOpened && heightDiff < 100) {
			isSoftKeyboardOpened = false;
			notifyOnSoftKeyboardClosed();
		}
	}

	public void addSoftKeyboardStateListener(SoftKeyboardStateListener listener) {
		listeners.add(listener);
	}

	public void detach() {
		activityRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
	}

	private void notifyOnSoftKeyboardOpened(int keyboardHeightInPx) {
		for (SoftKeyboardStateListener listener : listeners) {
			if (listener != null) {
				listener.onSoftKeyboardOpened(keyboardHeightInPx);
			}
		}
	}

	private void notifyOnSoftKeyboardClosed() {
		for (SoftKeyboardStateListener listener : listeners) {
			if (listener != null) {
				listener.onSoftKeyboardClosed();
			}
		}
	}

	private Activity scanForActivity(Context cont) {
		if (cont == null) return null;
		else if (cont instanceof Activity) return (Activity) cont;
		else if (cont instanceof ContextWrapper) return scanForActivity(((ContextWrapper) cont).getBaseContext());

		return null;
	}

}
