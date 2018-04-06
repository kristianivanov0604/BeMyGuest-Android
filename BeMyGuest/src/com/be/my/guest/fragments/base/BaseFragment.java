package com.be.my.guest.fragments.base;

import com.actionbarsherlock.app.SherlockFragment;
import com.be.my.guest.activities.base.BaseActivity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends SherlockFragment {

	protected BaseActivity baseActivity;
	protected View rootView;
	private static BaseFragment fragment;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			baseActivity = (BaseActivity) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must extend BaseActivity");
		}
	}

	@Override
	public void onDestroyView() {
		baseActivity.hideKeyboard();
		super.onDestroyView();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragment = this;
	}
	
	public boolean backButtonPressed() {
		return true;
	}
	
	public static FragmentManager getBaseFragmentMagager() {
		return fragment.getFragmentManager();
	}

	/**
	 * 
	 * @param inflater
	 * @param container
	 * @param resource
	 * @return true if view was inflated
	 */
	protected boolean inflateViewIfNull(LayoutInflater inflater,
			ViewGroup container, int resource) {
		if (rootView == null) {
			rootView = inflater.inflate(resource, container, false);
			return true;
		} else {
			((ViewGroup) rootView.getParent()).removeView(rootView);
			return false;
		}
	}
}
