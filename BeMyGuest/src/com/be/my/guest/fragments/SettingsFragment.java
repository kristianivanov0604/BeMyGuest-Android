package com.be.my.guest.fragments;

import android.R.menu;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.be.my.guest.R;
import com.be.my.guest.activities.MainActivity;
import com.be.my.guest.activities.base.BaseActivity.CUSTOM_ANIMATIONS;
import com.be.my.guest.adapters.MainListAdapter;
import com.be.my.guest.adapters.TicketListAdapter;
import com.be.my.guest.fragments.EventDetailFragment.DETAIL_MODE;
import com.be.my.guest.fragments.base.BaseFragment;
import com.be.my.guest.storage.LocalStorage;
import com.be.my.guest.ui.BmgDialog;
import com.be.my.guest.utility.BmgUtils;
import com.google.android.gms.internal.bn;
import com.google.android.gms.internal.de;

/**
 * Custom fragment class for Events list page
 * 
 * @author Abraham Sandbak
 * 
 */
public class SettingsFragment extends BaseFragment implements OnClickListener {

	// Holds activity delegate instance
	private MainActivity delegate;
	private View view;

	public static SettingsFragment newInstance() {
		return new SettingsFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_settings, container,
				false);
		BmgUtils.setTAG(this.getClass().getName());
		return this.view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		delegate = (MainActivity) getActivity();
		delegate.enableSlideMenu(true);

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

		// Set Custom ActionBar Icons and Handlers
		TextView tvTitle = (TextView) vi.findViewById(R.id.tv_title);
		tvTitle.setText(R.string.Settings);

		// Top left button
		ImageView menuBtn = (ImageView) vi.findViewById(R.id.iv_left_button);
		menuBtn.setOnClickListener(this);
		menuBtn.setImageResource(R.drawable.icon_menu);

		// Top right button
		ImageView mapBtn = (ImageView) vi.findViewById(R.id.iv_right_button);
		mapBtn.setOnClickListener(this);
		mapBtn.setImageResource(R.drawable.icon_checkin);

		// Name
		TextView tvName = (TextView) vi.findViewById(R.id.tv_name);
		tvName.setText(LocalStorage.authUser.profile.name);

		// Profile
		TextView profileItem = (TextView) vi.findViewById(R.id.item_profile)
				.findViewById(R.id.tv_item);
		profileItem.setText(R.string.Edit_Profile);
		vi.findViewById(R.id.item_profile).findViewById(R.id.tv_content)
				.setVisibility(View.GONE);
		vi.findViewById(R.id.item_profile).setOnClickListener(this);

		// About
		TextView aboutItem = (TextView) vi.findViewById(R.id.item_about)
				.findViewById(R.id.tv_item);
		aboutItem.setText(R.string.About);
		vi.findViewById(R.id.item_about).findViewById(R.id.tv_content)
				.setVisibility(View.GONE);
		vi.findViewById(R.id.item_about).setOnClickListener(this);

		// Copyright
		TextView copyrightItem = (TextView) vi
				.findViewById(R.id.item_copyright).findViewById(R.id.tv_item);
		copyrightItem.setText(R.string.Copyright);
		vi.findViewById(R.id.item_copyright).findViewById(R.id.tv_content)
				.setVisibility(View.GONE);
		vi.findViewById(R.id.item_copyright).setOnClickListener(this);

		// Signout
		TextView signoutItem = (TextView) vi.findViewById(R.id.item_signout)
				.findViewById(R.id.tv_item);
		signoutItem.setText(R.string.Sign_Out);
		vi.findViewById(R.id.item_signout).findViewById(R.id.tv_content)
				.setVisibility(View.GONE);
		vi.findViewById(R.id.item_signout).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.iv_left_button) {
			// Return to event list fragment
			delegate.toggleSlideMenu();

		} else if (v.getId() == R.id.iv_right_button) {
			// Open Map
			BmgUtils.showMessage("Right button clicked.");

		} else if (v.getId() == R.id.item_profile) {
			// Open Profile Page
			baseActivity.showFragment(R.id.fl_fragment_container,
					ProfileFragment.newInstance(), true,
					CUSTOM_ANIMATIONS.SLIDE_FROM_RIGHT);

		} else if (v.getId() == R.id.item_about) {
			// Open About Page
			BmgUtils.showMessage("Open about page.");

		} else if (v.getId() == R.id.item_copyright) {
			// Open Copyright Page
			BmgUtils.showMessage("Open copyright page.");

		} else if (v.getId() == R.id.item_signout) {
			// Signing Out
			delegate.signOut();
		}
	}
}
