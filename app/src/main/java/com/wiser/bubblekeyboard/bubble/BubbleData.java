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

	public String			virtualId;								// 虚拟商品id

	public String			thumbnailImg;							// 气泡缩略图

	public String			voiceColor;								// 语音颜色值

	public String			backGroundImg;							// 气泡使用图

	public String			exchangeType;							// 兑换类型free-免费member-会员star-星星

	public String			exchangeCount;							// 兑换所需星星数量

	public String			expireDays;								// 有效期天是

	public String			hasPermissions;							// 是否有权限使用0-无权限1-有权限

	public String			alreadyBuy;								// 是否已购买0-未购买1-已购买

	public String			isOverdue;								// 是否过期0-未过期1-已过期

	public String			sort;									// 排序

	public boolean			isCheck;								// 是否选择

}
