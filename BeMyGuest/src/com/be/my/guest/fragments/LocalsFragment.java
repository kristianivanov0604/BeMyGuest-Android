package com.be.my.guest.fragments;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.be.my.guest.R;
import com.be.my.guest.activities.MainActivity;
import com.be.my.guest.activities.base.BaseActivity.CUSTOM_ANIMATIONS;
import com.be.my.guest.adapters.MainListAdapter;
import com.be.my.guest.adapters.ModelListAdapter;
import com.be.my.guest.fragments.base.BaseFragment;
import com.be.my.guest.storage.LocalStorage;
import com.be.my.guest.utility.BmgUtils;

/**
 * Custom fragment class for Events list page
 * 
 * @author Abraham Sandbak
 * 
 */
public class LocalsFragment extends BaseFragment implements OnClickListener,
		OnTouchListener {

	// Holds activity delegate instance
	private MainActivity delegate;
	private View view;

	HorizontalListView hlvModelList;
	ListView lvLocalList;
	
	// Data members
	boolean needReload;
	int modelId;
	ArrayList<String> modelItems;
	ArrayList<Object> allPlacesArray;


	public static LocalsFragment newInstance() {
		return new LocalsFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		needReload = true;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_main_list, container,
				false);
		BmgUtils.setTAG(this.getClass().getName());
		return this.view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		delegate = (MainActivity) getActivity();
		delegate.enableSlideMenu(true);

		if (needReload) {
			modelId = 0;
			String[] items = getActivity().getResources().getStringArray(
					R.array.place_model_items);
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
		vi.findViewById(R.id.tv_city).setVisibility(View.GONE);
		vi.findViewById(R.id.lv_city_list).setVisibility(View.GONE);
		vi.findViewById(R.id.rl_select_date).setVisibility(View.GONE);

		// Set Custom ActionBar Icons and Handlers
		TextView tvTitle = (TextView) vi.findViewById(R.id.tv_title);
		tvTitle.setText(R.string.Local);

		ImageView menuBtn = (ImageView) vi.findViewById(R.id.iv_left_button);
		menuBtn.setOnClickListener(this);
		menuBtn.setImageResource(R.drawable.icon_menu);

		ImageView mapBtn = (ImageView) vi.findViewById(R.id.iv_right_button);
		mapBtn.setOnClickListener(this);
		mapBtn.setImageResource(R.drawable.icon_local);

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

		// Initialize Local ListView
		lvLocalList = (ListView) vi.findViewById(R.id.lv_main_list);
		loadMainList();
		needReload = false;
	}
	
	private void loadMainList() {
		allPlacesArray = LocalStorage.getPlacesByModel(modelId);
		lvLocalList.setAdapter(new MainListAdapter(getActivity(),
				MainListAdapter.ADAPTER_MODE.LOCAL, allPlacesArray));
		lvLocalList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Perform select an event
				EventDetailFragment fragment = EventDetailFragment.newInstance();
				fragment.setApplyMode(
						EventDetailFragment.DETAIL_MODE.MODE_LOCAL, lvLocalList
								.getAdapter().getItem(position));
				baseActivity.showFragment(R.id.fl_fragment_container,
						fragment, true, CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);
			}
		});
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.iv_left_button) {
			// Open Menu
			delegate.toggleSlideMenu();

		} else if (v.getId() == R.id.iv_right_button) {
			// Open Map

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
