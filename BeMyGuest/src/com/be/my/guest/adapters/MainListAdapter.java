package com.be.my.guest.adapters;

import com.be.my.guest.R;
import com.be.my.guest.adapters.BenefitListAdapter.ADAPTER_MODE;
import com.be.my.guest.api.domain.EVENT;
import com.be.my.guest.api.domain.PARTY;
import com.be.my.guest.api.domain.PLACE;
import com.be.my.guest.storage.LocalStorage;
import com.be.my.guest.ui.DynamicImageView;
import com.be.my.guest.utility.BmgUtils;

import java.util.ArrayList;
import java.util.Calendar;

import android.R.drawable;
import android.content.Context;
import android.content.res.TypedArray;
import android.location.Location;
import android.util.EventLog.Event;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter class for Events ListView
 * 
 */
public class MainListAdapter extends BaseAdapter {

	private ArrayList<Object> items;
	private LayoutInflater inflater;
	private ADAPTER_MODE adpterMode = ADAPTER_MODE.EVENT;

	public static enum ADAPTER_MODE {
		EVENT, LOCAL, PRODUCT
	}

	public MainListAdapter(Context context, ADAPTER_MODE mode,
			ArrayList<Object> items) {
		this.inflater = LayoutInflater.from(context);
		this.items = items;
		this.adpterMode = mode;
	}

	@Override
	public int getCount() {
		if (items == null) {
			return 5;
		}
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		if (items == null) {
			return null;
		}
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
			vi = inflater.inflate(R.layout.list_item_event, parent, false);
		}
		if (vi != null) {
			TextView tv1 = (TextView) vi.findViewById(R.id.tv_event_name);
			TextView tv2 = (TextView) vi.findViewById(R.id.tv_event_model);
			TextView tv3 = (TextView) vi.findViewById(R.id.tv_reward_points);
			TextView tv4 = (TextView) vi.findViewById(R.id.tv_event_clock);
			DynamicImageView image = (DynamicImageView) vi
					.findViewById(R.id.iv_event_image);
			String url = null;

			switch (adpterMode) {
			case EVENT:
				// Event List Item
				Object object = getItem(position);
				Calendar today = Calendar.getInstance();
				Calendar date;
				if (object.getClass().getName().equals(EVENT.class.getName())) {
					// Event
					EVENT event = (EVENT) object;
					PLACE place = event.getPlace();
					tv1.setText(event.name);
					tv2.setText(place.name);
					if (event.canRsvp) {
						tv3.setVisibility(View.VISIBLE);
						tv3.setText(String.format("+%d %s", event.rsvpReward,
								BmgUtils.getStringFromRes(R.string.pts)));
					} else {
						tv3.setVisibility(View.GONE);
					}
					date = event.getDate();
					url = place.getImageURL();
				} else {
					// Party
					PARTY party = (PARTY) object;
					tv1.setText(party.name);
					tv2.setText(party.location.name);
					if (party.sellingTickets) {
						tv3.setVisibility(View.VISIBLE);
						tv3.setText(String.format("+%d %s", party.rewordPoints,
								BmgUtils.getStringFromRes(R.string.pts)));
					} else {
						tv3.setVisibility(View.GONE);
					}
					date = party.getDate();
					url = party.getImageURL();
				}
				// show date time
				if (today.get(Calendar.DAY_OF_YEAR) == date
						.get(Calendar.DAY_OF_YEAR)) {
					// show interval time when the dates are same
					// long diff = date.getTimeInMillis()
					// - today.getTimeInMillis();
					// long dh = diff / (60 * 60 * 1000);
					// long dm = (diff / (60 * 1000)) % 60;
					tv4.setText(BmgUtils.calendarToString(date, "HH:mm"));
				} else if (today.get(Calendar.WEEK_OF_YEAR) == date
						.get(Calendar.WEEK_OF_YEAR)) {
					tv4.setText(BmgUtils.calendarToString(date, "EEE, HH:mm"));
				} else if (today.get(Calendar.MONTH) == date
						.get(Calendar.MONTH)) {
					tv4.setText(BmgUtils.calendarToString(date, "dd HH:mm"));
				} else {
					tv4.setText(BmgUtils.calendarToString(date, "dd MMM"));
				}

				// show picture
				image.loadImage(url);

				break;
			case LOCAL:
				// Place List Item
				PLACE place = (PLACE) getItem(position);
				// Place Image
				if (place.pictures.photos != null
						&& place.pictures.photos.size() > 0) {
					url = place.pictures.photos.get(0);
				}
				image.loadImage(url);
				
				// Place Name
				tv1.setText(place.name);
				
				// Distance between Place and User
				tv2.setText(String.format("%.1f %s",
						place.getDistance(LocalStorage.userLocation),
						BmgUtils.getStringFromRes(R.string.Km)));

				// CheckinReward points
				if (place.canCheckin) {
					tv3.setText(String.format("+%d %s", place.checkinReward,
							BmgUtils.getStringFromRes(R.string.pts)));
				} else {
					tv3.setVisibility(View.INVISIBLE);
				}
				tv4.setVisibility(View.INVISIBLE);
				break;
			case PRODUCT:
				// Check-in List Item
				tv1.setText(R.string.Tamari_2_Por1);
				tv2.setText(R.string.Markis_Place_Barao);
				tv2.setBackground(null);
				tv2.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
				tv2.setPadding(0, 0, 0, 0);
				image.setImageResource(R.drawable.product_image);
				tv4.setVisibility(View.INVISIBLE);
				break;

			default:
				break;
			}
		}
		return vi;
	}
}
