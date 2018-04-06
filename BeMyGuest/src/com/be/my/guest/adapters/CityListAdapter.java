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
public class CityListAdapter extends BaseAdapter {

	private ArrayList<String> items;	
	private LayoutInflater inflater;
	private int selectedPos = 0;

	public CityListAdapter(Context context, ArrayList<String> items) {
		this.inflater = LayoutInflater.from(context);
		this.items = items;
	}

	@Override
	public int getCount() {
		return items.size();
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
			vi = inflater.inflate(R.layout.list_item_city, parent, false);
		}
		
		TextView area = (TextView) vi.findViewById(R.id.tv_city);
		area.setText(getItem(position));

		ImageView icon = (ImageView)vi.findViewById(R.id.iv_check_icon);
		icon.setVisibility((getSelected() == position)? View.VISIBLE: View.INVISIBLE);
		return vi;
	}
	
}
