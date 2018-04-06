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
public class DescriptionListAdapter extends BaseAdapter {

	private ArrayList<String> items;	
	private Context context;
	private LayoutInflater inflater;
	private ADAPTER_MODE adpterMode = ADAPTER_MODE.DIALOG;
	
	public static enum ADAPTER_MODE {
		DIALOG, NONDLG
	}


	public DescriptionListAdapter(Context context, ADAPTER_MODE mode, ArrayList<String> items) {
		this.context = context;
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
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.list_item_description, parent, false);
		}
		
		ImageView ivIcon = (ImageView) vi.findViewById(R.id.iv_icon);
		TextView tvDescription = (TextView) vi.findViewById(R.id.tv_description);
		if (adpterMode == ADAPTER_MODE.DIALOG) {
			ivIcon.setImageResource(R.drawable.icon_item_green);
			tvDescription.setTextColor(context.getResources().getColor(R.color.white));
		} else {
			ivIcon.setImageResource(R.drawable.icon_item_grey);
			tvDescription.setTextColor(context.getResources().getColor(R.color.black));
		}
		return vi;
	}
	
}
