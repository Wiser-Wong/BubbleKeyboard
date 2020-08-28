package com.wiser.bubblekeyboard.bubble;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.wiser.bubblekeyboard.R;
import com.wiser.bubblekeyboard.base.BaseAdapter;
import com.wiser.bubblekeyboard.base.BaseViewHolder;

/**
 * @author Wiser
 * 
 *         气泡列表适配器
 */
public class BubbleAdapter extends BaseAdapter<BubbleData, BaseViewHolder> {

	private OnCheckBubbleListener	onCheckBubbleListener;

	private int						recordCurrentPosition	= 0;

	public BubbleAdapter(Context context) {
		super(context);
	}

	public void setOnCheckBubbleListener(OnCheckBubbleListener onCheckBubbleListener) {
		if (this.onCheckBubbleListener == null) this.onCheckBubbleListener = onCheckBubbleListener;
	}

	@Override public int getItemViewType(int position) {
		BubbleData bubbleData = getItem(position);
		if (bubbleData != null) return bubbleData.type;
		return super.getItemViewType(position);
	}

	@Override public BaseViewHolder newViewHolder(ViewGroup viewGroup, int type) {
		if (type == BubbleData.NO_BUBBLE_TYPE) {// 无气泡布局
			return new BubbleNoHolder(inflate(R.layout.keyboard_bubble_no_item, viewGroup));
		} else {
			return new BubbleHaveHolder(inflate(R.layout.keyboard_bubble_have_item, viewGroup));
		}
	}

	class BubbleHaveHolder extends BaseViewHolder<BubbleData> {

		AppCompatImageView ivBubbleChecked;

		public BubbleHaveHolder(@NonNull View itemView) {
			super(itemView);
			ivBubbleChecked = itemView.findViewById(R.id.iv_bubble_checked);
		}

		@Override public void bindData(final BubbleData bubbleData, final int position) {
			if (bubbleData == null) return;

			// 选择
			if (bubbleData.isCheck) {
				ivBubbleChecked.setVisibility(View.VISIBLE);
			} else {
				ivBubbleChecked.setVisibility(View.GONE);
			}
			itemView.setOnClickListener(new View.OnClickListener() {

				@Override public void onClick(View v) {
					if (bubbleData.isCheck) return;
					// 选择监听
					if (onCheckBubbleListener != null) onCheckBubbleListener.onCheckBubble(BubbleAdapter.this, itemView, bubbleData, position, bubbleData.isCheck, false);
				}
			});
		}
	}

	class BubbleNoHolder extends BaseViewHolder<BubbleData> {

		AppCompatImageView ivBubbleChecked;

		public BubbleNoHolder(@NonNull View itemView) {
			super(itemView);
			ivBubbleChecked = itemView.findViewById(R.id.iv_bubble_checked);
		}

		@Override public void bindData(final BubbleData bubbleData, final int position) {
			if (bubbleData == null) return;
			// 选择
			if (bubbleData.isCheck) {
				ivBubbleChecked.setVisibility(View.VISIBLE);
			} else {
				ivBubbleChecked.setVisibility(View.GONE);
			}

			itemView.setOnClickListener(new View.OnClickListener() {

				@Override public void onClick(View v) {
					if (bubbleData.isCheck) return;
					// 选择监听
					if (onCheckBubbleListener != null) onCheckBubbleListener.onCheckBubble(BubbleAdapter.this, itemView, bubbleData, position, bubbleData.isCheck, true);
				}
			});
		}
	}

	/**
	 * 更新选择数据
	 * 
	 * @param bubbleData
	 * @param position
	 */
	public void notifyData(BubbleData bubbleData, int position) {
		// 更新当前的选择
		bubbleData.isCheck = !bubbleData.isCheck;
		getItems().set(position, bubbleData);
		// 更新上一个被选择的
		BubbleData lastBubbleData = getItem(recordCurrentPosition);
		lastBubbleData.isCheck = !lastBubbleData.isCheck;
		getItems().set(recordCurrentPosition, lastBubbleData);
		notifyItemChanged(recordCurrentPosition);
		recordCurrentPosition = position;
		notifyDataSetChanged();
	}

}
