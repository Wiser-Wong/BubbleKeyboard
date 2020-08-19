package com.wiser.bubblekeyboard.bubble;

/**
 * @author Wiser
 * 
 *         气泡数据
 */
public class BubbleData {

	public final static int	NO_BUBBLE_TYPE		= 1000;

	public final static int	HAVE_BUBBLE_TYPE	= 1001;

	public int				type				= HAVE_BUBBLE_TYPE;

	public boolean			isVip;

	public boolean			isCheck;								// 是否选择

}
