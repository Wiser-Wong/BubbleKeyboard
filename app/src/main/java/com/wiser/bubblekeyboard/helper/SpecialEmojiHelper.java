package com.wiser.bubblekeyboard.helper;

import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.EditText;

/**
 * @author Wiser
 * 
 *         特殊表情处理
 */
public class SpecialEmojiHelper {

	public static void setProhibitEmoji(EditText et, OnSpecialEmojiListener onSpecialEmojiListener) {
		if (et == null) return;
		InputFilter[] filters = { getInputFilterProhibitEmoji(onSpecialEmojiListener), getInputFilterProhibitSP(onSpecialEmojiListener) };
		et.setFilters(filters);
	}

	public static InputFilter getInputFilterProhibitEmoji(final OnSpecialEmojiListener onSpecialEmojiListener) {
		return new InputFilter() {

			@Override public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				StringBuffer buffer = new StringBuffer();
				for (int i = start; i < end; i++) {
					char codePoint = source.charAt(i);
					if (!getIsEmoji(codePoint)) {
						buffer.append(codePoint);
					} else {
						if (onSpecialEmojiListener != null) onSpecialEmojiListener.onSpecialEmoji(OnSpecialEmojiListener.THIRD_EMOJI,source);
						i++;
						break;
					}
				}
				if (source instanceof Spanned) {
					SpannableString sp = new SpannableString(buffer);
					TextUtils.copySpansFrom((Spanned) source, start, end, null, sp, 0);
					return sp;
				} else {
					return buffer;
				}
			}
		};
	}

	public static boolean getIsEmoji(char codePoint) {
		return codePoint != 0x0 && codePoint != 0x9 && codePoint != 0xA && codePoint != 0xD && (codePoint < 0x20 || codePoint > 0xD7FF) && (codePoint < 0xE000 || codePoint > 0xFFFD);
	}

	public static InputFilter getInputFilterProhibitSP(final OnSpecialEmojiListener onSpecialEmojiListener) {
		return new InputFilter() {

			@Override public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				StringBuffer buffer = new StringBuffer();
				for (int i = start; i < end; i++) {
					char codePoint = source.charAt(i);
					if (!getIsSp(codePoint)) {
						buffer.append(codePoint);
					} else {
						if (onSpecialEmojiListener != null) onSpecialEmojiListener.onSpecialEmoji(OnSpecialEmojiListener.SPECIAL_SYMBOL,source);
						i++;
						break;
					}
				}
				if (source instanceof Spanned) {
					SpannableString sp = new SpannableString(buffer);
					TextUtils.copySpansFrom((Spanned) source, start, end, null, sp, 0);
					return sp;
				} else {
					return buffer;
				}
			}
		};
	}

	public static boolean getIsSp(char codePoint) {
		return Character.getType(codePoint) > Character.LETTER_NUMBER;
	}

	public interface OnSpecialEmojiListener {

		int	THIRD_EMOJI		= 1000;	// 三方表情

		int	SPECIAL_SYMBOL	= 1001;	// 特殊符号

		void onSpecialEmoji(int code,CharSequence source);
	}
}
