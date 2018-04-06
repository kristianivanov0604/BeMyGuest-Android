package com.be.my.guest.fragments;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.be.my.guest.R;
import com.be.my.guest.activities.OnboardingActivity;
import com.be.my.guest.api.APIClient;
import com.be.my.guest.api.domain.USER;
import com.be.my.guest.fragments.base.BaseFragment;
import com.be.my.guest.storage.LocalStorage;
import com.be.my.guest.utility.BmgUtils;
import com.google.gson.JsonObject;

/**
 * Custom fragment class for register page
 * 
 * @author Abraham Sandbak
 * 
 */
public class RegisterFragment extends BaseFragment implements OnClickListener,
		TextWatcher {

	// Holds activity delegate instance
	private OnboardingActivity delegate;
	private View view;

	private EditText etName;
	private EditText etEmail;
	private EditText etPassword;
	private EditText etConfirmPw;
	private ImageView ivCheckName;
	private ImageView ivCheckEmail;
	private ImageView ivDeletePassword;
	private ImageView ivDeleteConfirmPw;

	private boolean registered;

	public static RegisterFragment newInstance() {
		return new RegisterFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_register, container,
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
		etName = (EditText) vi.findViewById(R.id.et_register_name);
		etName.addTextChangedListener(this);
		etEmail = (EditText) vi.findViewById(R.id.et_register_mail);
		etEmail.addTextChangedListener(this);
		etPassword = (EditText) vi.findViewById(R.id.et_register_password);
		etPassword.addTextChangedListener(this);
		etConfirmPw = (EditText) vi.findViewById(R.id.et_confirm_password);
		etConfirmPw.addTextChangedListener(this);

		ivCheckName = (ImageView) vi.findViewById(R.id.iv_name_checked);
		ivCheckName.setVisibility(View.INVISIBLE);
		ivCheckEmail = (ImageView) vi.findViewById(R.id.iv_mail_checked);
		ivCheckEmail.setVisibility(View.INVISIBLE);
		ivDeletePassword = (ImageView) vi.findViewById(R.id.iv_password_delete);
		ivDeletePassword.setVisibility(View.INVISIBLE);
		ivDeletePassword.setOnClickListener(this);
		ivDeleteConfirmPw = (ImageView) vi.findViewById(R.id.iv_confirm_delete);
		ivDeleteConfirmPw.setVisibility(View.INVISIBLE);
		ivDeleteConfirmPw.setOnClickListener(this);

		vi.findViewById(R.id.iv_close_button).setOnClickListener(this);
		vi.findViewById(R.id.tv_facebook_login).setOnClickListener(this);
		vi.findViewById(R.id.tv_register_button).setOnClickListener(this);
		vi.findViewById(R.id.iv_terms_button).setOnClickListener(this);

		// Set test data
		etName.setText("My Name");
		etEmail.setText("mytestemail4@hotmail.com");
		etPassword.setText("hdgriming0922");
		etConfirmPw.setText("hdgriming0922");

	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.iv_close_button) {
			// TODO: Return to startup fragment
			delegate.onBackPressed();
		} else if (v.getId() == R.id.tv_register_button) {
			// TODO: Register and goto main activity
			signupWithEmail();
		} else if (v.getId() == R.id.tv_facebook_login) {
			// TODO: Perform Facebook login
			delegate.loginWithFacebook();
		} else if (v.getId() == R.id.iv_terms_button) {
			// TODO: Show Terms and Conditions
		} else if (v == ivDeletePassword) {
			// TODO: Delete password
			etPassword.setText("");
		} else if (v == ivDeleteConfirmPw) {
			// TODO: Delete confirm password
			etConfirmPw.setText("");
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

		ivCheckName
				.setVisibility((checkName(etName.getText().toString())) ? View.VISIBLE
						: View.INVISIBLE);
		ivCheckEmail
				.setVisibility((checkEmail(etEmail.getText().toString())) ? View.VISIBLE
						: View.INVISIBLE);
		ivDeletePassword.setVisibility((etPassword.getText().toString()
				.length() > 0) ? View.VISIBLE : View.INVISIBLE);
		ivDeleteConfirmPw.setVisibility((etConfirmPw.getText().toString()
				.length() > 0) ? View.VISIBLE : View.INVISIBLE);
	}

	/**
	 * Check name is valid
	 */
	private boolean checkName(String name) {
		int minNameLen = getResources().getInteger(R.integer.MIN_NAME_LEN);

		if (name.length() >= minNameLen) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Check email is valid
	 */
	private boolean checkEmail(String email) {
		return BmgUtils.validateEmail(email);
	}

	/**
	 * Check password is valid
	 */
	private boolean checkPassword(String password) {
		int minPassLen = getResources().getInteger(R.integer.MIN_PASSWORD_LEN);

		if (password.length() >= minPassLen) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Confirm password
	 */
	private boolean confirmPassword(String confirmPw) {
		boolean valid = false;
		if (checkPassword(etPassword.getText().toString())
				&& confirmPw.equals(etPassword.getText().toString())) {
			valid = true;
		}
		return valid;
	}

	/**
	 * Check the account data input
	 * 
	 * @return
	 */
	private boolean checkInputData() {
		String message = getResources().getString(R.string.Please_review);

		if (!checkName(etName.getText().toString())) {
			BmgUtils.showMessage(message);
			etName.requestFocus();
			return false;
		}

		if (!checkEmail(etEmail.getText().toString())) {
			BmgUtils.showMessage(message);
			etEmail.requestFocus();
			return false;
		}

		if (!checkPassword(etPassword.getText().toString())) {
			BmgUtils.showMessage(message);
			etPassword.requestFocus();
			return false;
		}

		if (!confirmPassword(etConfirmPw.getText().toString())) {
			BmgUtils.showMessage(message);
			etConfirmPw.requestFocus();
			return false;
		}

		return true;
	}

	/**
	 * Perform Sign up
	 */
	private void signupWithEmail() {

		if (!checkInputData()) {
			return;
		}
		String name = etName.getText().toString();
		final String email = etEmail.getText().toString();
		final String password = etPassword.getText().toString();

		final JsonObject newUser = new JsonObject();
		newUser.addProperty("email", email);
		newUser.addProperty("password", password);

		JsonObject profile = new JsonObject();
		profile.addProperty("name", name);
		newUser.add("profile", profile);

		BmgUtils.showProgDlg();
		BmgUtils.printLog("Signup with Email.");

		// Background SignUp Request
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					// Send Sign up request
					JsonObject json = APIClient.getInstance(delegate)
							.getApiService().signupWithEmail(newUser);
					if (json != null) {
						registered = true;
					} else {
						registered = false;
					}
				} catch (RetrofitError e) {
					// SignUp with email failure
					String reason = (e.getResponse().getReason() != null) ? e
							.getResponse().getReason().toString() : null;
					BmgUtils.printLog(reason);
					registered = false;
				}
				return null;
			}

			protected void onPostExecute(Void result) {
				if (registered) {
					// Send login request with registered email and password
					// to get access token
					BmgUtils.printLog(R.string.Signup_Success);
					APIClient.getInstance(delegate).getApiService()
							.loginWithEmail(email, password, loginCallback);
				} else {
					// SignUp with email failure
					BmgUtils.hideProgDlg();
					BmgUtils.showMessage(R.string.Signup_Failure);
				}
			};
		}.execute();

	}

	/*********************************
	 * login callback
	 *********************************/
	private Callback<JsonObject> loginCallback = new Callback<JsonObject>() {

		@Override
		public void failure(RetrofitError error) {
			// Login with email failure
			BmgUtils.hideProgDlg();
			BmgUtils.showMessage(R.string.Signup_Failure);
		}

		@Override
		public void success(JsonObject userJson, Response response) {
			// Login with email success
			BmgUtils.printLog(R.string.Signup_Success);
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
