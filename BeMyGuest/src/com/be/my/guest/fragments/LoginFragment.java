package com.be.my.guest.fragments;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

import com.be.my.guest.R;
import com.be.my.guest.activities.OnboardingActivity;
import com.be.my.guest.activities.base.BaseActivity.CUSTOM_ANIMATIONS;
import com.be.my.guest.api.APIClient;
import com.be.my.guest.api.domain.USER;
import com.be.my.guest.fragments.base.BaseFragment;
import com.be.my.guest.storage.LocalStorage;
import com.be.my.guest.utility.BmgUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Custom fragment class for login page
 * 
 * @author Abraham Sandbak
 * 
 */
public class LoginFragment extends BaseFragment implements OnClickListener {

	// Holds activity delegate instance
	private OnboardingActivity delegate;

	private View view;

	private EditText etEmail;
	private EditText etPassword;

	public static LoginFragment newInstance() {
		return new LoginFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_login, container, false);
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
		vi.findViewById(R.id.iv_close_button).setOnClickListener(this);
		vi.findViewById(R.id.tv_facebook_login).setOnClickListener(this);
		vi.findViewById(R.id.tv_login_button).setOnClickListener(this);
		vi.findViewById(R.id.tv_forgot_button).setOnClickListener(this);
		vi.findViewById(R.id.tv_register_button).setOnClickListener(this);

		etEmail = (EditText) vi.findViewById(R.id.et_login_mail);
		etPassword = (EditText) vi.findViewById(R.id.et_login_password);

		// Set test data
		etEmail.setText("abrahamsandbak@hotmail.com");
		etPassword.setText("hdgriming0922");

	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.iv_close_button) {

			// Navigate to Startup screen
			delegate.onBackPressed();
		} else if (v.getId() == R.id.tv_login_button) {

			// Perform login with email
			loginWithEmail();
		} else if (v.getId() == R.id.tv_facebook_login) {

			// Perform login with FaceBook
			delegate.loginWithFacebook();
		} else if (v.getId() == R.id.tv_forgot_button) {

			// Perform reset password

		} else if (v.getId() == R.id.tv_register_button) {

			// Navigate to Signup screen
			baseActivity.showFragment(R.id.fl_fragment_container,
					RegisterFragment.newInstance(), true,
					CUSTOM_ANIMATIONS.FADE_IN);
		}

	}

	/**
	 * Check the account data input
	 * 
	 * @return
	 */
	private boolean checkInputData() {
		String message = getResources().getString(R.string.Please_review);

		if (etEmail.getText().toString().length() == 0) {
			BmgUtils.showMessage(message);
			etEmail.requestFocus();
			return false;
		}

		if (etPassword.getText().toString().length() == 0) {
			BmgUtils.showMessage(message);
			etPassword.requestFocus();
			return false;
		}

		return true;
	}

	/**
	 * Perform login with email
	 */
	private void loginWithEmail() {

		if (checkInputData()) {
			String email = etEmail.getText().toString();
			String password = etPassword.getText().toString();
			BmgUtils.showProgDlg();

			// Send login request
			BmgUtils.printLog("Login with Email.");
			APIClient.getInstance(delegate).getApiService()
					.loginWithEmail(email, password, loginCallback);

		}

	}

	/**
	 * login callback
	 */
	private Callback<JsonObject> loginCallback = new Callback<JsonObject>() {

		@Override
		public void failure(RetrofitError error) {
			// Login with email failure
			BmgUtils.hideProgDlg();
			BmgUtils.showMessage(R.string.Login_Failure);
		}

		@Override
		public void success(JsonObject userJson, Response response) {
			// Login with email success
			BmgUtils.printLog(R.string.Login_Success);
			BmgUtils.printLog(userJson.toString());

			// Save authorized user info and access token
			USER user = USER.fromJson(userJson.get("user"));
			user.password = etPassword.getText().toString();
			LocalStorage.saveAccessToken(user.accessToken.token, user.id,
					user.password);
			LocalStorage.authUser = user;

			// Load Application Data
			delegate.loadAppData();
		}

	};


}
