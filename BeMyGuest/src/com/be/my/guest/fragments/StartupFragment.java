package com.be.my.guest.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.be.my.guest.R;
import com.be.my.guest.activities.OnboardingActivity;
import com.be.my.guest.activities.base.BaseActivity.CUSTOM_ANIMATIONS;
import com.be.my.guest.fragments.base.BaseFragment;
import com.be.my.guest.utility.BmgUtils;

/**
 * Custom fragment class which let the user to select login/register
 * 
 * @author Abraham Sandbak
 * 
 */
public class StartupFragment extends BaseFragment implements OnClickListener {

	// Holds activity delegate instance
	private OnboardingActivity delegate;

	private View view;

	public static StartupFragment newInstance() {
		return new StartupFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_startup, container,
				false);
		BmgUtils.setTAG(this.getClass().getName());
		return this.view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		delegate = (OnboardingActivity) getActivity();

		// initialize
		initViewAndClassMembers();
	}

	/**
	 * Initialize view elements and class members
	 */
	private void initViewAndClassMembers() {

		View vi = this.view;

		// Map view elements to event handlers
		vi.findViewById(R.id.tv_facebook_login).setOnClickListener(this);
		vi.findViewById(R.id.tv_login_button).setOnClickListener(this);
		vi.findViewById(R.id.tv_signup_button).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tv_facebook_login) {
			// TODO: Perform FaceBook login
			delegate.loginWithFacebook();

		} else if (v.getId() == R.id.tv_login_button) {
			// TODO: Goto login fragment
			baseActivity.showFragment(R.id.fl_fragment_container,
					LoginFragment.newInstance(), true, CUSTOM_ANIMATIONS.FADE_IN);
		} else if (v.getId() == R.id.tv_signup_button) {
			// TODO: Goto register fragment
			baseActivity.showFragment(R.id.fl_fragment_container,
					RegisterFragment.newInstance(), true, CUSTOM_ANIMATIONS.FADE_IN);
		}
	}

}
