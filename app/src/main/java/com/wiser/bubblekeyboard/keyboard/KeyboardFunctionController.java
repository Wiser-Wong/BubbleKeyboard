package com.wiser.bubblekeyboard.keyboard;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.wiser.bubblekeyboard.R;
import com.wiser.bubblekeyboard.bubble.BubbleData;
import com.wiser.bubblekeyboard.bubble.BubbleSelectView;
import com.wiser.bubblekeyboard.bubble.OnCheckBubbleListener;
import com.wiser.bubblekeyboard.helper.KeyboardHelper;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Wiser
 * 
 *         键盘功能控件
 */
public class KeyboardFunctionController extends FrameLayout {

	private static final int			MAX_INPUT_LIMIT	= 200;		// 最大可输入数量

	private FrameLayout					flInputBackground;			// 输入框背景 用于气泡背景添加

	private InputEditTextView			etInput;					// 输入框

	private SwitchKeyboardAndVoiceView	switchKeyboardAndVoiceView;	// 键盘和语音切换按钮

	private AppCompatTextView			tvSurplusNum;				// 剩余可输入数字

	private IssueTextView				tvIssue;					// 发布按钮

	private LinearLayout				llKeyboardRoot;				// 键盘根布局

	private RelativeLayout				rlKeyboardRoot;				// 切换语音和键盘以及发布的根布局

	private FrameLayout					flVoiceRoot;				// 录音根布局

	private ConstraintLayout			clKeyboardInit;				// 初始时键盘父布局

	private AppCompatTextView			tvKeyboardInit;				// 初始时键盘控件

	private AppCompatImageView			ivVoiceInit;				// 初始时语音图标

	private BubbleSelectView			bubbleSelectView;			// 气泡选择列表控件

	private KeyboardHelper				keyboardHelper;

	private int							keyboardHeightInPx;

	private boolean						isClickSwitch;				// 是否点击键盘和录音切换

	private boolean						isClickVoiceForKeyboard;	// 是否点击录音按钮弹出的第一次键盘

	public KeyboardFunctionController(Context context) {
		super(context);
		init();
	}

	public KeyboardFunctionController(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		((Activity) getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		LayoutInflater.from(getContext()).inflate(R.layout.keyboard_function_view, this, true);

		initView();

		initListener();

		// 构建LayoutTransition
		LayoutTransition mTransition = new LayoutTransition();
		// 设置给ViewGroup容器
		setLayoutTransition(mTransition);
		getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
		//
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
		// llKeyboardRoot.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
		// }
	}

	// 初始化控件
	private void initView() {
		flInputBackground = findViewById(R.id.fl_input_background);
		etInput = findViewById(R.id.et_input);
		switchKeyboardAndVoiceView = findViewById(R.id.keyboard_voice_view);
		tvSurplusNum = findViewById(R.id.tv_surplus_input_num);
		tvIssue = findViewById(R.id.tv_issue);
		llKeyboardRoot = findViewById(R.id.ll_keyboard_root);
		rlKeyboardRoot = findViewById(R.id.rl_keyboard_root);
		flVoiceRoot = findViewById(R.id.fl_voice_root);
		bubbleSelectView = findViewById(R.id.bubble_view);

		clKeyboardInit = findViewById(R.id.cl_keyboard_init);
		tvKeyboardInit = findViewById(R.id.tv_input_init);
		ivVoiceInit = findViewById(R.id.iv_voice_init);

		// 设置剩余输入数字
		setSurplusNum(MAX_INPUT_LIMIT);

		bubbleSelectView.setItems(getData());

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

	// 初始化监听
	private void initListener() {
		// 初始化默认输入点击弹出输入框事件
		initInputClickListener();

		// 初始化默认录音按钮点击弹出录音UI事件
		initVoiceClickListener();

		// 初始化输入框监听
		initEditTextChangeListener();

		// 初始化输入框点击监听
		initEditTextClickListener();

		// 初始化键盘和语音按钮切换监听
		initSwitchKeyboardAndVoiceListener();

		// 初始化发布按钮点击监听
		initIssueClickListener();

		// 初始化软键盘监听
		initSoftKeyboardListener();

		// 初始化气泡点击事件
		initBubbleClickListener();
	}

	// 根布局按下监听
	private void initRootTouchListener() {
		if (getParent() == null || getParent().getParent() == null) return;
		((ViewGroup) ((ViewGroup) getParent()).getParent()).setOnTouchListener(null);
		((ViewGroup) ((ViewGroup) getParent()).getParent()).setOnTouchListener(new OnTouchListener() {

			@Override public boolean onTouch(View v, MotionEvent event) {
				InputHelper.getInstance(KeyboardFunctionController.this.getContext()).hideSoftInput(etInput);
				((ViewGroup) ((ViewGroup) KeyboardFunctionController.this.getParent()).getParent()).performClick();
				resetUi();
				return false;
			}
		});
	}

	// 初始化默认录音按钮点击弹出录音UI事件
	private void initVoiceClickListener() {
		ivVoiceInit.setOnClickListener(new OnClickListener() {

			@Override public void onClick(View v) {
				isClickVoiceForKeyboard = true;
				clKeyboardInit.setVisibility(GONE);
				llKeyboardRoot.setVisibility(VISIBLE);
				flInputBackground.setVisibility(GONE);
				switchKeyboardAndVoiceView.updateSwitch();
				setRecordViewHeight();
			}
		});
	}

	// 初始化默认输入点击弹出输入框事件
	private void initInputClickListener() {
		tvKeyboardInit.setOnClickListener(new OnClickListener() {

			@Override public void onClick(View v) {
				flInputBackground.setVisibility(VISIBLE);
				etInput.requestFocus();
				InputHelper.getInstance(getContext()).showSoftInput(etInput);
			}
		});
	}

	// 初始化输入框改变监听
	private void initEditTextChangeListener() {
		etInput.addTextChangedListener(new TextWatcher() {

			@Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override public void afterTextChanged(Editable s) {
				// 改变发送按钮颜色变化
				if (s != null && !TextUtils.isEmpty(s.toString()) && s.toString().trim().length() > 0) {
					tvIssue.updateIssueButtonUi(true);
				} else {
					tvIssue.updateIssueButtonUi(false);
				}

				// 设置剩余可输入数量
				setSurplusNum(s != null && !TextUtils.isEmpty(s.toString()) ? MAX_INPUT_LIMIT - s.length() : MAX_INPUT_LIMIT);

				if (s != null && !TextUtils.isEmpty(s.toString()) && s.length() > MAX_INPUT_LIMIT) {
					Toast.makeText(getContext(), "限制" + MAX_INPUT_LIMIT + "个字符。", Toast.LENGTH_SHORT).show();
					if (etInput.getText() != null) etInput.getText().delete(MAX_INPUT_LIMIT, s.length());
					etInput.setSelection(etInput.length());
				}
			}
		});
	}

	// 初始化输入框点击监听
	private void initEditTextClickListener() {
		etInput.setOnClickListener(new OnClickListener() {

			@Override public void onClick(View v) {
				updateSoftKeyboardState(true);
			}
		});

		etInput.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					// InputHelper.getInstance(getContext()).showSoftInput(etInput);
				}
			}
		});
	}

	// 初始化气泡选择点击监听
	private void initBubbleClickListener() {
		bubbleSelectView.setOnCheckBubbleListener(new OnCheckBubbleListener() {

			@Override public void onCheckBubble(View view, int position, boolean isChecked, boolean isNoBubble) {
				updateInputBackground(!isNoBubble);
			}
		});
	}

	// 初始化键盘和语音按钮切换监听
	private void initSwitchKeyboardAndVoiceListener() {
		switchKeyboardAndVoiceView.setOnSwitchKeyboardAndVoiceListener(new OnSwitchKeyboardAndVoiceListener() {

			@Override public void onSwitch(View view, boolean isKeyboard) {
				isClickSwitch = true;
				// 键盘图标显示时软键盘是处于收起状态
				if (isKeyboard) {
					inputShowUi(false);
					updateSoftKeyboardState(false);
				} else { // 语音图标显示时软键盘是处于打开状态
					inputShowUi(true);
					etInput.requestFocus();
					updateSoftKeyboardState(true);
				}
			}
		});
	}

	// 初始化发布按钮点击监听
	private void initIssueClickListener() {
		tvIssue.setOnIssueClickListener(new OnIssueClickListener() {

			@Override public void onIssueClick(View view, String content) {

			}
		});
	}

	// 初始化软键盘监听
	private void initSoftKeyboardListener() {
		keyboardHelper = new KeyboardHelper(getRootView());
		keyboardHelper.addSoftKeyboardStateListener(new KeyboardHelper.SoftKeyboardStateListener() {

			@Override public void onSoftKeyboardOpened(final int keyboardHeightInPx) {
				// 点击键盘和录音按钮切换的时候，不处理以下
				if (!isClickSwitch || isClickVoiceForKeyboard) {
					KeyboardFunctionController.this.keyboardHeightInPx = keyboardHeightInPx;
					// 隐藏默认布局 显示键盘布局
					if (clKeyboardInit != null && clKeyboardInit.getVisibility() == VISIBLE) clKeyboardInit.setVisibility(GONE);
					if (llKeyboardRoot != null && llKeyboardRoot.getVisibility() == GONE) llKeyboardRoot.setVisibility(VISIBLE);
					if (switchKeyboardAndVoiceView.isKeyboard()) switchKeyboardAndVoiceView.updateSwitch();
					// 设置录音布局高度 并且添加动画显示
					setRecordViewHeight();
					isClickVoiceForKeyboard = false;
				} else {
					isClickSwitch = false;
				}
			}

			@Override public void onSoftKeyboardClosed() {
				// 点击键盘和录音按钮切换的时候，不处理以下
				if (!isClickSwitch) {
					// 重置UI
					resetUi();
				} else {
					isClickSwitch = false;
				}
			}
		});
	}

	/**
	 * 更新软键盘
	 * 
	 * @param isShow
	 *            是否显示
	 */
	private void updateSoftKeyboardState(final boolean isShow) {
		if (isShow) {
			InputHelper.getInstance(getContext()).showSoftInput(etInput);
		} else {
			InputHelper.getInstance(getContext()).hideSoftInput(etInput);
		}
	}

	/**
	 * 更新输入框背景
	 * 
	 * @param isShow
	 */
	private void updateInputBackground(boolean isShow) {
		if (isShow) {
			flInputBackground.setBackgroundResource(R.drawable.bubble_frame_bg);
			etInput.setBackgroundResource(0);
			etInput.setPadding(0, 0, 0, 0);
		} else {
			flInputBackground.setBackgroundResource(0);
			etInput.setBackgroundResource(R.drawable.shape_round15_grey_bg);
			etInput.setPadding((int) dp2px(12), (int) dp2px(10), (int) dp2px(12), (int) dp2px(10));
			flInputBackground.setPadding(0, 0, 0, 0);
		}
	}

	// 输入框显示隐藏
	private void inputShowUi(final boolean isShow) {
		if (isShow) {
			flInputBackground.setVisibility(VISIBLE);
		} else {
			flInputBackground.setVisibility(GONE);
		}
	}

	// 设置剩余可输入数量
	private void setSurplusNum(int surplusNum) {
		tvSurplusNum.setText(MessageFormat.format("{0}", surplusNum));
	}

	// 设置录音布局高度 并且添加动画显示
	private void setRecordViewHeight() {
		if (flVoiceRoot.getMeasuredHeight() != keyboardHeightInPx) {
			// 录音布局默认高度是0 设置与键盘相同高度
			MarginLayoutParams params = (MarginLayoutParams) flVoiceRoot.getLayoutParams();
			params.height = keyboardHeightInPx;
			flVoiceRoot.setLayoutParams(params);
		}

		if (isClickVoiceForKeyboard && flVoiceRoot.getMeasuredHeight() != keyboardHeightInPx) {
			// 为了解决每次弹出键盘整体布局都是从上往下滑动显示
			ObjectAnimator.ofFloat(llKeyboardRoot, "translationY", keyboardHeightInPx, 0).setDuration(100).start();
		}
	}

	// 恢复默认样式
	private void resetUi() {
		// 重置高度
		// MarginLayoutParams params = (MarginLayoutParams)
		// flVoiceRoot.getLayoutParams();
		// params.height = 0;
		// flVoiceRoot.setLayoutParams(params);
		clKeyboardInit.setVisibility(VISIBLE);
		llKeyboardRoot.setVisibility(GONE);
		etInput.clearFocus();
		switchKeyboardAndVoiceView.reset();
		isClickVoiceForKeyboard = false;
	}

	@Override protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		initRootTouchListener();
	}

	@Override protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (keyboardHelper != null) keyboardHelper.detach();
		keyboardHelper = null;
		flInputBackground = null;
		etInput = null;
		clKeyboardInit = null;
		llKeyboardRoot = null;
		rlKeyboardRoot = null;
		switchKeyboardAndVoiceView = null;
		tvKeyboardInit = null;
		tvSurplusNum = null;
		ivVoiceInit = null;
		flVoiceRoot = null;
	}

	/**
	 * dp转换px
	 *
	 * @param dp
	 *            dp值
	 * @return 转换后的px值
	 */
	private float dp2px(float dp) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
	}

}
