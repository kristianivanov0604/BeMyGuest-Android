package com.be.my.guest.adapters;
import com.be.my.guest.R;

import java.util.ArrayList;


import android.R.drawable;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter class for City ListView 
 *  
 */
public class BenefitListAdapter extends BaseAdapter {

	private ArrayList<String> items;	
	private LayoutInflater inflater;
	private int selectedPos = 0;
	private ADAPTER_MODE adpterMode = ADAPTER_MODE.BENEFIT;
	
	public static enum ADAPTER_MODE {
		BENEFIT, CONGRATULATION
	}


	public BenefitListAdapter(Context context, ADAPTER_MODE mode, ArrayList<String> items) {
		this.inflater = LayoutInflater.from(context);
		this.adpterMode = mode;
		this.items = items;
	}

	@Override
	public int getCount() {
		return 3;
//		return items.size();
	}

	@Override
	public String getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setSelected(int position) {
		selectedPos = position;
	}
	
	public int getSelected() {
		return selectedPos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.list_item_benefit, parent, false);
		}
		
		TextView tvTitle = (TextView) vi.findViewById(R.id.tv_title);
		TextView tvDetail = (TextView) vi.findViewById(R.id.tv_content1);
		TextView tvDetail2 = (TextView) vi.findViewById(R.id.tv_content2);
		if (adpterMode == ADAPTER_MODE.BENEFIT) {
			tvTitle.setText(R.string.Tamari_2_Por1);
			tvDetail.setText(R.string.Markis_Place_Barao);
			tvDetail2.setVisibility(View.GONE);
		} else {
			tvTitle.setText(R.string.sample_congratulation_title);
			tvDetail.setText(R.string.sample_congratulation_content1);
			tvDetail2.setText(R.string.sample_congratulation_content2);
		}
		ImageView icon = (ImageView)vi.findViewById(R.id.iv_icon);
		if (selectedPos == position) {
			icon.setImageResource(R.drawable.icon_green_option);
		} else {
			icon.setImageResource(R.drawable.icon_yellow_option);
		}
		return vi;
	}
	
}
