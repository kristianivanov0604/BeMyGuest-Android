package com.be.my.guest.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.FutureTask;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.EventLog.Event;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.devsmart.android.ui.HorizontalListView;
import com.google.gson.JsonObject;

import com.be.my.guest.R;
import com.be.my.guest.activities.MainActivity;
import com.be.my.guest.activities.OnboardingActivity;
import com.be.my.guest.activities.base.BaseActivity.CUSTOM_ANIMATIONS;
import com.be.my.guest.adapters.CityListAdapter;
import com.be.my.guest.adapters.MainListAdapter;
import com.be.my.guest.adapters.ModelListAdapter;
import com.be.my.guest.adapters.SideMenuAdapter;
import com.be.my.guest.api.domain.EVENT;
import com.be.my.guest.fragments.base.BaseFragment;
import com.be.my.guest.storage.LocalStorage;
import com.be.my.guest.utility.BmgUtils;

/**
 * Custom fragment class for Events list page
 * 
 * @author Abraham Sandbak
 * 
 */
public class EventsFragment extends BaseFragment implements OnClickListener,
		OnTouchListener {

	// Holds activity delegate instance
	private MainActivity delegate;

	// View members
	private View view;
	TextView tvCity;
	TextView tvDate;
	HorizontalListView hlvModelList;
	ListView lvCityList;
	ListView lvEventList;

	// Data members
	boolean needReload;
	int cityId;
	int modelId;
	ArrayList<String> cityItems;
	ArrayList<String> modelItems;
	ArrayList<Object> allEventsArray;

	public static EventsFragment newInstance() {
		return new EventsFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		needReload = true;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		BmgUtils.setTAG(this.getClass().getName());
		this.view = inflater.inflate(R.layout.fragment_main_list, container,
				false);
		return this.view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		delegate = (MainActivity) getActivity();
		delegate.enableSlideMenu(true);

		if (needReload) {
			cityId = LocalStorage.nCurrentCity;
			cityItems = LocalStorage.cityNames;
			modelId = 0;
			String[] items = getActivity().getResources().getStringArray(
					R.array.event_model_items);
			modelItems = new ArrayList<String>();
			Collections.addAll(modelItems, items);
		}

		// initialize
		initViewAndClassMembers();
	}

	/**
	 * Initialize view elements and class members
	 */
	private void initViewAndClassMembers() {

		View vi = this.view;

		// Hiding unused views
		vi.findViewById(R.id.tv_title).setVisibility(View.GONE);
		vi.findViewById(R.id.rl_select_date).setVisibility(View.GONE);

		// Set Custom ActionBar Icons and Handlers
		ImageView menuBtn = (ImageView) vi.findViewById(R.id.iv_left_button);
		menuBtn.setOnClickListener(this);
		menuBtn.setImageResource(R.drawable.icon_menu);

		ImageView filterBtn = (ImageView) vi.findViewById(R.id.iv_right_button);
		filterBtn.setOnClickListener(this);
		filterBtn.setImageResource(R.drawable.icon_filter);

		// Initialize City ListView
		tvCity = (TextView) vi.findViewById(R.id.tv_city);
		tvCity.setText(cityItems.get(cityId));
		tvCity.setOnClickListener(this);
		lvCityList = (ListView) vi.findViewById(R.id.lv_city_list);
		lvCityList.setVisibility(View.INVISIBLE);
		final CityListAdapter areaListAdapter = new CityListAdapter(
				getActivity(), cityItems);
		lvCityList.setAdapter(areaListAdapter);
		lvCityList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Perform select an area
				if (position != cityId) {
					areaListAdapter.setSelected(position);
					areaListAdapter.notifyDataSetChanged();
					tvCity.setText(cityItems.get(position));
					cityId = position;
					LocalStorage.nCurrentCity = cityId;
				}
				lvCityList.setVisibility(View.INVISIBLE);
			}
		});

		// Initialize Date Selection Rect
		vi.findViewById(R.id.iv_calendar_button).setOnClickListener(this);
		tvDate = (TextView) vi.findViewById(R.id.tv_date);

		// Initialize Event Model ListView
		hlvModelList = (HorizontalListView) vi.findViewById(R.id.lv_model_list);
		final ModelListAdapter modelListAdapter = new ModelListAdapter(
				getActivity(), modelItems);
		hlvModelList.setAdapter(modelListAdapter);
		hlvModelList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Perform select an area
				if (position != modelListAdapter.getSelected()) {
					modelId = position;
					modelListAdapter.setSelected(position);
					modelListAdapter.notifyDataSetChanged();
					loadMainList();
				}
			}
		});
		hlvModelList.setOnTouchListener(this);
		modelListAdapter.setSelected(modelId);
		modelListAdapter.notifyDataSetChanged();

		// Initialize Event ListView
		lvEventList = (ListView) vi.findViewById(R.id.lv_main_list);
		loadMainList();
		needReload = false;
	}

	private void loadMainList() {
		allEventsArray = LocalStorage.getEventsByModel(modelId);
		lvEventList.setAdapter(new MainListAdapter(getActivity(),
				MainListAdapter.ADAPTER_MODE.EVENT, allEventsArray));
		lvEventList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Perform select an event
				EventDetailFragment fragment = EventDetailFragment
						.newInstance();
				Object obj = lvEventList.getAdapter().getItem(position);
				if (obj.getClass().getName().equals(EVENT.class.getName())) {
					// Event
					fragment.setApplyMode(
							EventDetailFragment.DETAIL_MODE.MODE_EVENT, obj);
				} else {
					// Party
					fragment.setApplyMode(
							EventDetailFragment.DETAIL_MODE.MODE_PARTY, obj);
				}
				baseActivity.showFragment(R.id.fl_fragment_container, fragment,
						true, CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
			}
		});
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.iv_left_button) {
			// Open Menu
			delegate.toggleSlideMenu();

		} else if (v.getId() == R.id.iv_right_button) {
			// Perform Filter

		} else if (v.getId() == R.id.tv_city) {
			// Open places list
			lvCityList
					.setVisibility((lvCityList.getVisibility() == View.VISIBLE) ? View.INVISIBLE
							: View.VISIBLE);

		} else if (v.getId() == R.id.iv_calendar_button) {
			// Open calendar
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (v == hlvModelList) {
			// Handle ListView touch events.
			int action = event.getAction();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				// Disallow ScrollView to intercept touch events.
				v.getParent().requestDisallowInterceptTouchEvent(true);
				break;

			case MotionEvent.ACTION_UP:
				// Allow ScrollView to intercept touch events.
				v.getParent().requestDisallowInterceptTouchEvent(false);
				break;
			}
			v.onTouchEvent(event);
			return true;
		}

		return false;
	}
}
