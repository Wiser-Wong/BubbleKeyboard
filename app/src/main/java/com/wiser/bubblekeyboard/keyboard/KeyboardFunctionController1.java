package com.wiser.bubblekeyboard.keyboard;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import com.wiser.bubblekeyboard.R;
import com.wiser.bubblekeyboard.bubble.BubbleAdapter;
import com.wiser.bubblekeyboard.bubble.BubbleData;
import com.wiser.bubblekeyboard.bubble.BubbleDialogFragment;
import com.wiser.bubblekeyboard.bubble.BubbleSelectView;
import com.wiser.bubblekeyboard.bubble.OnCheckBubbleListener;
import com.wiser.bubblekeyboard.bubble.OnCheckBubbleResultListener;
import com.wiser.bubblekeyboard.helper.HandlerHelper;
import com.wiser.bubblekeyboard.helper.InputHelper;
import com.wiser.bubblekeyboard.helper.KeyboardHelper;
import com.wiser.bubblekeyboard.helper.KeyboardListenerHelper;
import com.wiser.bubblekeyboard.utils.FragmentTool;
import com.wiser.bubblekeyboard.utils.ScreenUtil;

import java.text.MessageFormat;

/**
 * @author Wiser
 *
 *         键盘功能控件
 */
public class KeyboardFunctionController1 extends FrameLayout {

	private static final int				MAX_INPUT_LIMIT				= 200;									// 最大可输入数量

	private FrameLayout						flInputRoot;														// 输入框根布局

	private AppCompatImageView				ivInputBackground;													// 输入框背景图片 用于气泡背景添加

	private InputEditTextView				etInput;															// 输入框

	private SwitchKeyboardAndVoiceView		switchKeyboardAndVoiceView;											// 键盘和语音切换按钮

	private AppCompatTextView				tvSurplusNum;														// 剩余可输入数字

	private IssueTextView					tvIssue;															// 发布按钮

	private LinearLayout					llKeyboardRoot;														// 键盘根布局

	private FrameLayout						flVoiceRoot;														// 录音根布局

	private ConstraintLayout				clKeyboardInit;														// 初始时键盘父布局

	private AppCompatTextView				tvKeyboardInit;														// 初始时键盘控件

	private AppCompatImageView				ivVoiceInit;														// 初始时语音图标

	private BubbleSelectView				bubbleSelectView;													// 气泡选择列表控件

	private View							viewPlaceholder;													// 录音布局上面的占位布局 为了使键盘高度大于录音布局时切换流畅

	private KeyboardListenerHelper			keyboardListenerHelper;												// 键盘显示隐藏监听帮助类

	private InputHelper						inputHelper;														// 键盘显示隐藏方法类

	private int								keyboardHeightInPx			= ScreenUtil.dp2px(getContext(), 250);	// 软键盘高度

	private int								recordVoiceDefaultHeight	= ScreenUtil.dp2px(getContext(), 200);	// 录音布局默认高度

	private boolean							isClickSwitch;														// 是否点击键盘和录音切换

	private boolean							isClickVoiceForKeyboard;											// 是否点击录音按钮弹出的第一次键盘

	private OnKeyboardBubbleRecoverListener	onKeyboardBubbleRecoverListener;

	public KeyboardFunctionController1(Context context) {
		super(context);
		init();
	}

	public KeyboardFunctionController1(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		Activity activity = scanForActivity(getContext());
		if (activity != null) activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		inputHelper = new InputHelper(getContext());

		LayoutInflater.from(getContext()).inflate(R.layout.keyboard_function_controller, this, true);

		initView();

		initListener();

		// 主要控制键盘显示隐藏改变动画
		setLayoutTransition(true);

		// getLayoutTransition().setAnimateParentHierarchy(false);
	}

	// 主要控制键盘显示隐藏改变动画
	private void setLayoutTransition(boolean isTransition) {
		if (isTransition) {
			// 主要控制键盘显示隐藏改变动画
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				// 设置给ViewGroup容器
				setLayoutTransition(new LayoutTransition());
				getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
				llKeyboardRoot.setLayoutTransition(new LayoutTransition());
				llKeyboardRoot.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
			}
		} else {
			setLayoutTransition(null);
			llKeyboardRoot.setLayoutTransition(null);
		}
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		flInputRoot = findViewById(R.id.fl_input_root);
		ivInputBackground = findViewById(R.id.iv_input_background);
		etInput = findViewById(R.id.et_input);
		switchKeyboardAndVoiceView = findViewById(R.id.keyboard_voice_view);
		tvSurplusNum = findViewById(R.id.tv_surplus_input_num);
		tvIssue = findViewById(R.id.tv_issue);
		llKeyboardRoot = findViewById(R.id.ll_keyboard_root);
		flVoiceRoot = findViewById(R.id.fl_voice_root);
		bubbleSelectView = findViewById(R.id.bubble_view);
		viewPlaceholder = findViewById(R.id.view_placeholder);
		clKeyboardInit = findViewById(R.id.cl_keyboard_init);
		tvKeyboardInit = findViewById(R.id.tv_input_init);
		ivVoiceInit = findViewById(R.id.iv_voice_init);

		// 防止touch事件处理焦点
		setClickable(true);
		setFocusable(true);

		// 设置剩余输入数字
		setSurplusNum(MAX_INPUT_LIMIT);

		// 添加初始气泡数据
		if (KeyboardHelper.bind() != null) KeyboardHelper.bind().initKeyboardBubbleData(bubbleSelectView);

	}

	public void setOnKeyboardBubbleRecoverListener(OnKeyboardBubbleRecoverListener onKeyboardBubbleRecoverListener) {
		this.onKeyboardBubbleRecoverListener = onKeyboardBubbleRecoverListener;
	}

	// 初始化监听
	private void initListener() {
		// 初始化默认输入点击弹出输入框事件
		initInputClickListener();

		// 初始化默认录音按钮点击弹出录音UI事件
		initVoiceClickListener();

		// 初始化输入框监听
		initEditTextChangeListener();

		// 初始化输入框键盘返回监听
		initEditTextKeyboardBackListener();

		// 初始化键盘和语音按钮切换监听
		initSwitchKeyboardAndVoiceListener();

		// 初始化发布按钮点击监听
		initIssueClickListener();

		// 初始化软键盘监听
		initSoftKeyboardListener();

		// 初始化气泡点击事件
		initBubbleClickListener();
	}

	/**
	 * 根布局按下监听
	 */
	private void initRootTouchListener() {
		if (getParent() == null || getParent().getParent() == null) return;
		((ViewGroup) ((ViewGroup) getParent()).getParent()).setOnTouchListener(null);
		((ViewGroup) ((ViewGroup) getParent()).getParent()).setOnTouchListener(new OnTouchListener() {

			@Override public boolean onTouch(View v, MotionEvent event) {
				// 点击其他位置时切换软键盘为隐藏
				switchSoftKeyboardShowState(false);
				((ViewGroup) ((ViewGroup) KeyboardFunctionController1.this.getParent()).getParent()).performClick();
				// 重置UI
				resetUi();
				return false;
			}
		});
	}

	/**
	 * 初始化默认录音按钮点击弹出录音UI事件
	 */
	private void initVoiceClickListener() {
		ivVoiceInit.setOnClickListener(new OnClickListener() {

			@Override public void onClick(View v) {
				// 初始录音点击 显示UI
				initVoiceClickStateShowUi();
			}
		});
	}

	/**
	 * 初始化默认输入点击弹出输入框事件
	 */
	private void initInputClickListener() {
		tvKeyboardInit.setOnClickListener(new OnClickListener() {

			@Override public void onClick(View v) {
				// 初始输入框点击显示UI
				initInputClickStateShowUi();
			}
		});
	}

	/**
	 * 初始化输入框改变监听
	 */
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

	/**
	 * 初始化输入框键盘返回监听
	 */
	private void initEditTextKeyboardBackListener() {
		etInput.setOnEditTextKeyboardBackListener(new InputEditTextView.OnEditTextKeyboardBackListener() {

			@Override public void onKeyboardBack() {
				final BubbleDialogFragment dialogFragment = (BubbleDialogFragment) FragmentTool.findFragment((FragmentActivity) getContext(), BubbleDialogFragment.class.getSimpleName());
				if (dialogFragment != null) {
					HandlerHelper.mainLooper().execute(new Runnable() {

						@Override public void run() {
							if (dialogFragment != null) dialogFragment.dismissAllowingStateLoss();
						}
					}, 100);
				} else { // 键盘显示时 返回处理
					switchSoftKeyboardShowState(false);
					resetUi();
				}
			}
		});
	}

	/**
	 * 初始化气泡选择点击监听
	 */
	private void initBubbleClickListener() {
		bubbleSelectView.setOnCheckBubbleListener(new OnCheckBubbleListener() {

			@Override public void onCheckBubble(final BubbleAdapter bubbleAdapter, View view, final BubbleData bubbleData, final int position, boolean isChecked, boolean isNoBubble) {
				if (isNoBubble) { // 无气泡选择
					// 切换气泡的时候更新输入框为气泡背景
					updateInputBackground(false);
					if (bubbleAdapter != null) bubbleAdapter.notifyData(bubbleData, position);
				} else { // 有气泡选择，需要先判断是否可用气泡
					if (KeyboardHelper.bind() != null) {
						KeyboardHelper.bind().onActionBubbleChecked(getContext(), bubbleData, new OnCheckBubbleResultListener() {

							@Override public void onCheckBubbleResult() {
								// 切换气泡的时候更新输入框为气泡背景
								updateInputBackground(true);
								if (bubbleAdapter != null) bubbleAdapter.notifyData(bubbleData, position);
							}
						});
					}
				}
			}
		});
	}

	/**
	 * 初始化键盘和语音按钮切换监听
	 */
	private void initSwitchKeyboardAndVoiceListener() {
		switchKeyboardAndVoiceView.setOnSwitchKeyboardAndVoiceListener(new OnSwitchKeyboardAndVoiceListener() {

			@Override public void onSwitch(View view, boolean isKeyboard) {
				// 赋值true说明点击了切换键盘和录音布局的按钮
				isClickSwitch = true;
				// 键盘图标显示时软键盘是处于收起状态
				if (isKeyboard) {
					// 切换到键盘图标的时候 切换软键盘为隐藏
					switchSoftKeyboardShowState(false);
					// 切换到键盘图标的时候 切换输入框为隐藏
					switchInputShowStateUi(false);
				} else { // 语音图标显示时软键盘是处于打开状态
					// 切换到录音图标的时候 切换输入框为显示
					switchInputShowStateUi(true);
					// 输入框重新获取焦点
					if (isClickVoiceForKeyboard) {
						HandlerHelper.mainLooper().execute(new Runnable() {

							@Override public void run() {
								// 由于先获取焦点，会出现软键盘弹出时整体布局向上顶一下，然后执行输入框显示动画，会跳动闪烁，所以加延迟获取焦点
								if (etInput != null) etInput.requestFocus();
//								// 切换到录音图标的时候 切换软键盘为显示
								switchSoftKeyboardShowState(true);
							}
						}, getLayoutTransition() != null ? getLayoutTransition().getDuration(LayoutTransition.CHANGING) : 300);

					} else {
						// 切换到录音图标的时候 切换软键盘为显示
						switchSoftKeyboardShowState(true);
					}
				}
			}
		});
	}

	/**
	 * 初始化发布按钮点击监听
	 */
	private void initIssueClickListener() {
		tvIssue.setOnIssueClickListener(new OnIssueClickListener() {

			@Override public void onIssueClick(View view, String content) {
				if (KeyboardHelper.bind() != null) KeyboardHelper.bind().onActionIssueText(content);
			}
		});
	}

	/**
	 * 初始化软键盘监听
	 */
	private void initSoftKeyboardListener() {
		keyboardListenerHelper = new KeyboardListenerHelper(scanForActivity(getContext()).findViewById(android.R.id.content));
		keyboardListenerHelper.addSoftKeyboardStateListener(new KeyboardListenerHelper.SoftKeyboardStateListener() {

			@Override public void onSoftKeyboardOpened(final int keyboardHeightInPx) {
				// 点击键盘和录音按钮切换的时候，不处理以下 或者第一次点击了初始按钮录音调起的录音布局，然后切换到键盘执行以下
				if (!isClickSwitch || isClickVoiceForKeyboard) {
					KeyboardFunctionController1.this.keyboardHeightInPx = keyboardHeightInPx;
					// 设置录音布局高度 并且添加动画显示
					setRecordLayoutHeight();
					// 隐藏默认布局 显示键盘布局
					switchInitLayoutAndKeyboardLayoutShowStateUi(false);
					// 显示键盘的时候要将键盘和录音图标恢复到键盘图标
					if (switchKeyboardAndVoiceView != null && switchKeyboardAndVoiceView.isKeyboard()) switchKeyboardAndVoiceView.updateSwitch();
					isClickVoiceForKeyboard = false;
				}
				isClickSwitch = false;

				HandlerHelper.mainLooper().execute(new Runnable() {

					@Override public void run() {
						if (etInput != null) etInput.requestFocus();
					}
				}, getLayoutTransition() != null ? getLayoutTransition().getDuration(LayoutTransition.CHANGING) : 300);
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
	 * 切换软键盘显示状态
	 *
	 * @param isShow
	 *            是否显示
	 */
	private void switchSoftKeyboardShowState(boolean isShow) {
		if (isShow) { // 显示软键盘
			if (inputHelper != null) inputHelper.showSoftInput(getRootView());
		} else { // 隐藏软键盘
			if (inputHelper != null) inputHelper.hideSoftInput(etInput);
		}
	}

	/**
	 * 更新输入框背景
	 *
	 * @param isShow
	 *            是否显示
	 */
	private void updateInputBackground(boolean isShow) {
		if (isShow) { // 切换气泡输入框背景
			// 图片背景显示
			if (ivInputBackground != null) {
				ivInputBackground.setVisibility(VISIBLE);
				// 图片背景设置选择的气泡背景框
				if (KeyboardHelper.bind() != null) KeyboardHelper.bind().setInputBackground(ivInputBackground);
			}
			if (etInput != null) {
				// 输入框背景去掉
				etInput.setBackgroundResource(0);
				// 输入框背景边距设置
				etInput.setPadding(ScreenUtil.dp2px(getContext(), 24), ScreenUtil.dp2px(getContext(), 20), ScreenUtil.dp2px(getContext(), 24), ScreenUtil.dp2px(getContext(), 20));
			}
		} else { // 恢复默认输入框背景
			if (ivInputBackground != null) {
				// 图片背景隐藏
				ivInputBackground.setVisibility(GONE);
				// 图片背景去掉
				ivInputBackground.setBackgroundResource(0);
			}
			if (etInput != null) {
				// 输入框背景恢复默认
				etInput.setBackgroundResource(R.drawable.keyboard_shape_round19_eeeeee_bg);
				// 输入框边距恢复默认
				etInput.setPadding(ScreenUtil.dp2px(getContext(), 17), ScreenUtil.dp2px(getContext(), 10), ScreenUtil.dp2px(getContext(), 17), ScreenUtil.dp2px(getContext(), 10));
			}
		}
	}

	/**
	 * 切换输入框显示状态 输入框显示时发布和剩余可输入数量都显示 输入框隐藏时发布和剩余可输入数量都隐藏
	 *
	 * @param isShow
	 *            是否显示输入框
	 */
	private void switchInputShowStateUi(final boolean isShow) {
		if (isShow) {
			// 输入框父布局显示
			if (flInputRoot != null && flInputRoot.getVisibility() == GONE) flInputRoot.setVisibility(VISIBLE);
			// 发布评论按钮显示
			if (tvIssue != null && tvIssue.getVisibility() == GONE) tvIssue.setVisibility(VISIBLE);
			// 剩余输入数量控件显示
			if (tvSurplusNum != null && tvSurplusNum.getVisibility() == GONE) tvSurplusNum.setVisibility(VISIBLE);
			// 录音上面占位布局，切换键盘和录音的时候根据变化显示隐藏
			if (viewPlaceholder != null) viewPlaceholder.setVisibility(VISIBLE);
		} else {
			// 输入框父布局隐藏
			if (flInputRoot != null && flInputRoot.getVisibility() == VISIBLE) flInputRoot.setVisibility(GONE);
			// 发布评论按钮隐藏
			if (tvIssue != null && tvIssue.getVisibility() == VISIBLE) tvIssue.setVisibility(GONE);
			// 剩余输入数量控件隐藏
			if (tvSurplusNum != null && tvSurplusNum.getVisibility() == VISIBLE) tvSurplusNum.setVisibility(GONE);
			// 录音上面占位布局，切换键盘和录音的时候根据变化显示隐藏
			if (viewPlaceholder != null) viewPlaceholder.setVisibility(GONE);
		}
	}

	/**
	 * 设置剩余可输入数量
	 *
	 * @param surplusNum
	 *            剩余可输入数量
	 */
	private void setSurplusNum(int surplusNum) {
		if (tvSurplusNum != null) tvSurplusNum.setText(MessageFormat.format("{0}", surplusNum));
	}

	/**
	 * 设置录音布局高度 并且添加动画显示
	 */
	private void setRecordLayoutHeight() {
		// 录音布局不与键盘高度相同时设置录音布局高度
		if (flVoiceRoot != null) {
			// 录音布局默认高度是0 设置与键盘相同高度
			final MarginLayoutParams params = (MarginLayoutParams) flVoiceRoot.getLayoutParams();
			// 不是第一次录音按钮点击进来的
			if (!isClickVoiceForKeyboard) {
				// 键盘高度大于录音布局高度 计算录音布局高度并重新设置
				if (keyboardHeightInPx > params.height) {
				} else { // 否则键盘高度小于等于录音高度，则等于键盘高度
					params.height = keyboardHeightInPx;
				}
			} else {
				params.height = recordVoiceDefaultHeight;
			}
			flVoiceRoot.setLayoutParams(params);

			// 设置占位布局高度
			if (viewPlaceholder != null && keyboardHeightInPx > 0 && keyboardHeightInPx > params.height && params.height > 0) {
				MarginLayoutParams placeholderParams = (MarginLayoutParams) viewPlaceholder.getLayoutParams();
				placeholderParams.height = keyboardHeightInPx - params.height;
				viewPlaceholder.setLayoutParams(placeholderParams);
			}
		}
	}

	/**
	 * 初始输入框点击 显示UI
	 */
	public void initInputClickStateShowUi() {
		// 切换输入框为显示
		switchInputShowStateUi(true);
		// 输入框获取焦点
		if (etInput != null) etInput.requestFocus();
		// 软键盘显示
		switchSoftKeyboardShowState(true);
	}

	/**
	 * 初始录音点击 显示UI
	 */
	public void initVoiceClickStateShowUi() {
		// 点击初始录音按钮 记录点击，防止无法执行软键盘显示逻辑
		isClickVoiceForKeyboard = true;
		// 切换输入框为隐藏
		switchInputShowStateUi(false);
		// 切换初始布局隐藏和键盘布局显示
		switchInitLayoutAndKeyboardLayoutShowStateUi(false);
		// 改变键盘和录音按钮
		if (switchKeyboardAndVoiceView != null) switchKeyboardAndVoiceView.updateSwitch();
		// 设置录音布局高度
		setRecordLayoutHeight();
	}

	/**
	 * 恢复默认样式
	 */
	public void resetUi() {
		// 恢复最初显示隐藏状态
		switchInitLayoutAndKeyboardLayoutShowStateUi(true);
		// 键盘和录音图标切换恢复最初状态
		if (switchKeyboardAndVoiceView != null) switchKeyboardAndVoiceView.reset();
		// 恢复点击语音调起的布局
		isClickVoiceForKeyboard = false;

		if (onKeyboardBubbleRecoverListener != null) onKeyboardBubbleRecoverListener.onRecover();
	}

	/**
	 * 切换初始布局和键盘整体根布局显示UI
	 *
	 * @param isResetInitLayoutShow
	 *            是否显示键盘整体根布局
	 */
	private void switchInitLayoutAndKeyboardLayoutShowStateUi(boolean isResetInitLayoutShow) {
		if (isResetInitLayoutShow) {
			// 初始布局 显示
			if (clKeyboardInit != null && clKeyboardInit.getVisibility() == GONE) clKeyboardInit.setVisibility(VISIBLE);
			// 键盘整体布局隐藏
			if (llKeyboardRoot != null && llKeyboardRoot.getVisibility() == VISIBLE) llKeyboardRoot.setVisibility(GONE);
		} else {
			// 初始布局 隐藏
			if (clKeyboardInit != null && clKeyboardInit.getVisibility() == VISIBLE) clKeyboardInit.setVisibility(GONE);
			// 键盘整体布局显示
			if (llKeyboardRoot != null && llKeyboardRoot.getVisibility() == GONE) llKeyboardRoot.setVisibility(VISIBLE);
		}
	}

	@Override protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		// 初始化根布局Touch事件
		initRootTouchListener();
	}

	/**
	 * 销毁内存
	 */
	@Override protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (keyboardListenerHelper != null) keyboardListenerHelper.detach();
		keyboardListenerHelper = null;
		if (inputHelper != null) inputHelper.detach();
		inputHelper = null;
		flInputRoot = null;
		etInput = null;
		clKeyboardInit = null;
		llKeyboardRoot = null;
		switchKeyboardAndVoiceView = null;
		tvKeyboardInit = null;
		tvSurplusNum = null;
		ivVoiceInit = null;
		flVoiceRoot = null;
		bubbleSelectView = null;
		onKeyboardBubbleRecoverListener = null;
		ivInputBackground = null;
		tvIssue = null;
	}

	private Activity scanForActivity(Context cont) {
		if (cont == null) return null;
		else if (cont instanceof Activity) return (Activity) cont;
		else if (cont instanceof ContextWrapper) return scanForActivity(((ContextWrapper) cont).getBaseContext());

		return null;
	}

}
