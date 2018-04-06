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
 * Adapter class for Side Menu
 * 
 */
public class SideMenuAdapter extends BaseAdapter {

	private ArrayList<String> items;
	private int[] icons = { R.drawable.icon_calendar, R.drawable.icon_local,
			R.drawable.icon_store, R.drawable.icon_map,
			R.drawable.icon_tickets, R.drawable.icon_setting };

	private int[] icons_selected = { R.drawable.icon_red_calendar,
			R.drawable.icon_red_local, R.drawable.icon_red_store,
			R.drawable.icon_red_map, R.drawable.icon_red_tickets,
			R.drawable.icon_red_setting };

	private Context context = null;
	private LayoutInflater inflater;
	private int selectedPos = 0;

	public SideMenuAdapter(Context context, ArrayList<String> items) {
		this.context = context;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.list_item_side_menu, parent, false);
		}
		ImageView icon = (ImageView) vi
				.findViewById(R.id.iv_sidemenu_item_icon);
		TextView title = (TextView) vi
				.findViewById(R.id.tv_sidemenu_item_title);

		title.setText(getItem(position));

		if (getSelected() == position) {
			icon.setImageResource(icons_selected[position]);
			title.setTextColor(context.getResources()
					.getColor(R.color.bgm_pink));
		} else {
			icon.setImageResource(icons[position]);
			title.setTextColor(context.getResources().getColor(R.color.white));
		}
		return vi;
	}

	public void setSelected(int position) {
		selectedPos = position;
	}

	public int getSelected() {
		return selectedPos;
	}

}
