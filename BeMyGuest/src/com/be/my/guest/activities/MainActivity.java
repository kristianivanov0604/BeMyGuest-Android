package com.be.my.guest.activities;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

import com.actionbarsherlock.view.ActionMode;
import com.be.my.guest.R;
import com.be.my.guest.activities.base.BaseActivity;
import com.be.my.guest.api.APIClient;
import com.be.my.guest.api.domain.CITY;
import com.be.my.guest.api.domain.USER;
import com.be.my.guest.api.domain.USER.PROFILE;
import com.be.my.guest.fragments.CongratulationsFragment;
import com.be.my.guest.fragments.EventsFragment;
import com.be.my.guest.fragments.LocalsFragment;
import com.be.my.guest.fragments.MyMapFragment;
import com.be.my.guest.fragments.ProductsFragment;
import com.be.my.guest.fragments.SettingsFragment;
import com.be.my.guest.fragments.SideMenuFragment;
import com.be.my.guest.fragments.SideMenuFragment.SideMenuItemClickListener;
import com.be.my.guest.fragments.base.BaseFragment;
import com.be.my.guest.storage.LocalStorage;
import com.be.my.guest.utility.BmgUtils;
import com.google.gson.JsonObject;
import com.slidingmenu.lib.SlidingMenu;

/**
 * Holds main activity instance of the application
 * 
 * @author Abraham Sandbak
 * 
 */
public class MainActivity extends BaseActivity implements
		SideMenuItemClickListener {

	public static final int MENUITEM_EVENTS = 0, MENUITEM_LOCALS = 1,
			MENUITEM_STOREPOINTS = 2, MENUITEM_MAP = 3, MENUITEM_TICKETS = 4,
			MENUITEM_SETTINGS = 5;

	// Member variable which holds the instance of sliding menu
	private SlidingMenu sideMenu;
	private SideMenuFragment menuFragment;
	private boolean backPressed;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		// Initialize side menu and first fragment
		instantiateSlidingMenu();
		showFragment(R.id.fl_fragment_container, EventsFragment.newInstance(),
				true, CUSTOM_ANIMATIONS.FADE_IN);

	}

	/**
	 * Instantiates the sliding menu from SideMenuFragment
	 */
	private void instantiateSlidingMenu() {

		// initialize sliding menu
		sideMenu = new SlidingMenu(this);
		sideMenu.setMode(SlidingMenu.LEFT);
		sideMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sideMenu.setBehindOffsetRes(R.dimen.behind_offset);
		sideMenu.setFadeDegree(0.35f);
		sideMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		sideMenu.setMenu(R.layout.slide_menu_container);
		sideMenu.setSlidingEnabled(true);

	}

	/**
	 * Enable/Disable slide menu
	 */
	public void enableSlideMenu(boolean enable) {
		sideMenu.setSlidingEnabled(enable);
	}

	/**
	 * Set side menu fragment
	 */
	public void setMenuFragment(SideMenuFragment fragment) {
		menuFragment = fragment;
	}

	@Override
	public void onMenuItemSelected(int position) {
		selectMenu(position);
		toggleSlideMenu();
	}

	/**
	 * Select menu item and set new fragment
	 */
	public void selectMenu(int position) {
		menuFragment.setMenuSelected(position);
		BaseFragment fragment = null;
		switch (position) {
		case MENUITEM_EVENTS:
			// Select Event fragment
			fragment = EventsFragment.newInstance();
			break;
		case MENUITEM_LOCALS:
			// Select Locals fragment
			fragment = LocalsFragment.newInstance();
			break;
		case MENUITEM_STOREPOINTS:
			// Select Store Points fragment
			fragment = ProductsFragment.newInstance();
			break;
		case MENUITEM_MAP:
			// Select Map fragment
			fragment = MyMapFragment.newInstance();
			break;
		case MENUITEM_TICKETS:
			// Select Settings fragment
			fragment = CongratulationsFragment.newInstance();
			break;
		case MENUITEM_SETTINGS:
			// Select Settings fragment
			fragment = SettingsFragment.newInstance();
			break;
		}

		if (fragment != null) {
			showFragment(R.id.fl_fragment_container, fragment, true, true,
					CUSTOM_ANIMATIONS.FADE_IN);
		}
	}

	/**
	 * Toggle slide menu
	 */
	public void toggleSlideMenu() {
		sideMenu.toggle();
	}

	/**
	 * Sign out and navigate to Log in Activity
	 */
	public void signOut() {

		// Reset Local storage
		LocalStorage.resetStorage();

		// Navigate to login activity
		Intent intent = new Intent(MainActivity.this, OnboardingActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * Set and update user profile info in menu fragment
	 */
	public void updateUserPrile() {
		menuFragment.setUserProfile();
	}

	@Override
	public void onBackPressed() {
		int backStackCount = getSupportFragmentManager()
				.getBackStackEntryCount();
		Log.e("", String.format("%d", backStackCount));
		if (activeFragment instanceof EventsFragment
				|| activeFragment instanceof LocalsFragment
				|| activeFragment instanceof ProductsFragment
				|| activeFragment instanceof CongratulationsFragment
				|| activeFragment instanceof SettingsFragment
				|| (activeFragment instanceof MyMapFragment && backStackCount == 1)) {
			ConfirmFinish();
		} else {
			super.onBackPressed();
		}
	}

	/**
	 * Confirm to finish
	 */
	private void ConfirmFinish() {
		if (backPressed) {
			finish();
		} else {
			BmgUtils.showMessage(getResources().getString(
					R.string.Confirm_Finish));
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					backPressed = false;
				}
			}, 2000);
			backPressed = true;
		}
	}
}
