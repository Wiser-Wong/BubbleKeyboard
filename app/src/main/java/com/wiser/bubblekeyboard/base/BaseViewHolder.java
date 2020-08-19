package com.wiser.bubblekeyboard.base;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Wiser
 * 
 *         Holder base
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

	private BaseAdapter adapter;

	public BaseViewHolder(@NonNull View itemView) {
		super(itemView);
	}

	public void setAdapter(BaseAdapter adapter) {
		this.adapter = adapter;
	}

	public BaseAdapter adapter() {
		return adapter;
	}

	public abstract void bindData(T t, int position);

}
