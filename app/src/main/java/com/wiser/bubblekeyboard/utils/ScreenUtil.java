package com.wiser.bubblekeyboard.utils;

import android.content.Context;
import android.util.TypedValue;

public class ScreenUtil {

    public static int dp2px(Context context,int dp) {
        if (context == null) return 0;
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

}
