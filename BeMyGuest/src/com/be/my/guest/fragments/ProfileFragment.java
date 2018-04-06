package com.be.my.guest.fragments;

import java.util.Calendar;

import retrofit.Callback;
import retrofit.RetrofitError;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.be.my.guest.R;
import com.be.my.guest.activities.MainActivity;
import com.be.my.guest.api.APIClient;
import com.be.my.guest.api.domain.USER;
import com.be.my.guest.fragments.base.BaseFragment;
import com.be.my.guest.storage.LocalStorage;
import com.be.my.guest.ui.DynamicImageView;
import com.be.my.guest.utility.BmgUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Custom fragment class for Events list page
 * 
 * @author Abraham Sandbak
 * 
 */
public class ProfileFragment extends BaseFragment implements OnClickListener {

	// Holds activity delegate instance
	private MainActivity delegate;
	private View view;

	DynamicImageView ivAvatar;
	EditText etName;
	TextView tvEmail;
	TextView tvBirthday;
	EditText etPhone;
	EditText etOldPass = null;
	EditText etNewPass = null;
	EditText etCity;
	Calendar birthday;
	String newPassword;

	USER authUser;
	
	public static ProfileFragment newInstance() {
		return new ProfileFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fragment_profile, container,
				false);
		BmgUtils.setTAG(this.getClass().getName());
		return this.view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		delegate = (MainActivity) getActivity();

		// Disable sliding menu
		delegate.enableSlideMenu(false);

		// initialize
		authUser = LocalStorage.authUser;
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
		tvTitle.setText(R.string.Edit_Profile);

		ImageView menuBtn = (ImageView) vi.findViewById(R.id.iv_left_button);
		menuBtn.setOnClickListener(this);

		ImageView mapBtn = (ImageView) vi.findViewById(R.id.iv_right_button);
		mapBtn.setOnClickListener(this);

		// avatar
		ivAvatar = (DynamicImageView) vi.findViewById(R.id.iv_avatar);
		ivAvatar.loadRoundedImage(authUser.profile.pictureURL);
		vi.findViewById(R.id.iv_edit_avatar).setOnClickListener(this);

		// Profile contents
		etName = (EditText) vi.findViewById(R.id.et_name);
		etName.setText(authUser.profile.name);

		tvEmail = (TextView) vi.findViewById(R.id.tv_email);
		tvEmail.setText(authUser.email);

		tvBirthday = (TextView) vi.findViewById(R.id.tv_birthday);
		tvBirthday.setOnClickListener(this);
		tvBirthday.setText(null);
		if (authUser.profile.birthday != null) {
			birthday = BmgUtils.stringToCalendar(
					authUser.profile.birthday, getResources()
							.getString(R.string.BIRTHDAY_SAVE_FORMAT));
			tvBirthday.setText(BmgUtils.calendarToString(birthday,
					getResources().getString(R.string.BIRTHDAY_SHOW_FORMAT)));
		}

		etPhone = (EditText) vi.findViewById(R.id.et_telephone);
		etPhone.setText(authUser.profile.phone);

		if (authUser.isFBUser()) {
			vi.findViewById(R.id.rl_password_bg).setVisibility(View.GONE);
			vi.findViewById(R.id.rl_newpass_bg).setVisibility(View.GONE);
		} else {
			etOldPass = (EditText) vi.findViewById(R.id.et_password);
			etOldPass.setText("hdgriming0922");

			etNewPass = (EditText) vi.findViewById(R.id.et_newpass);
			etNewPass.setText("hdgriming0922");
		}

		etCity = (EditText) vi.findViewById(R.id.et_city);
		etCity.setText(authUser.profile.cityId);

		// Save Button
		TextView saveButton = (TextView) vi.findViewById(R.id.bottom_button);
		saveButton.setOnClickListener(this);
		saveButton.setText(R.string.DONE_CHANGES);

	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.iv_left_button) {
			// Open Menu
			delegate.onBackPressed();

		} else if (v.getId() == R.id.iv_right_button) {
			// Open Map

		} else if (v.getId() == R.id.iv_edit_avatar) {
			// Edit avatar

		} else if (v.getId() == R.id.tv_birthday) {
			// Edit Birthday
			showDatePicker();
		} else if (v.getId() == R.id.bottom_button) {
			// Save and Close
			saveProfile();
		}

		delegate.hideKeyboard();
	}

	/**
	 * Save new profile info
	 */
	private void saveProfile() {
		String message = getResources().getString(R.string.Please_review);
		int minNameLen = getResources().getInteger(R.integer.MIN_NAME_LEN);
		int minPassLen = getResources().getInteger(R.integer.MIN_PASSWORD_LEN);
		int phoneLen = getResources().getInteger(R.integer.PHOEN_LEN);

		String name = etName.getText().toString();
		String phone = etPhone.getText().toString();
		String oldpass = null;
		String newpass = null;
		String city = etCity.getText().toString();

		// Check name
		if (name.length() < minNameLen) {
			BmgUtils.showMessage(message);
			etName.requestFocus();
			return;
		}

		// Check Phone
		if (phone.length() > 0 && !BmgUtils.validatePhone(phone, phoneLen)) {
			BmgUtils.showMessage(message);
			etPhone.requestFocus();
			return;
		}

		// Check Password
		if (!authUser.isFBUser()) {
			oldpass = etOldPass.getText().toString();
			newpass = etNewPass.getText().toString();

			// Check old password
			if (!oldpass.equals(authUser.password)) {
				BmgUtils.showMessage(message);
				etOldPass.requestFocus();
				return;
			}

			// Check new password
			if (newpass.length() < minPassLen) {
				BmgUtils.showMessage(message);
				etNewPass.requestFocus();
				return;
			}
		}

		if (city.length() > 0 && !LocalStorage.citiesArray.contains(city)) {
			BmgUtils.showMessage(message);
			etCity.requestFocus();
			return;
		}

		// Set Request JsonObject
		JsonObject editUser = new JsonObject();
		if (!authUser.isFBUser()) {
			JsonObject password = new JsonObject();
			password.addProperty("oldPass", oldpass);
			password.addProperty("newPass", newpass);
			newPassword = newpass;
			// new password to update
			editUser.add("password", password);
		}
		
		JsonObject profile = new JsonObject();
		if (name.length() > 0) {
			profile.addProperty("name", name);
		}
		if (city.length() > 0) {
			profile.addProperty("city", city);
		}
		if (phone.length() > 0)	{
			profile.addProperty("phone", phone);
		}
		if (birthday != null) {
			profile.addProperty("birthday", BmgUtils.calendarToString(birthday,
					getResources().getString(R.string.BIRTHDAY_SAVE_FORMAT)));
		}
		// new profile element to update
		editUser.add("profile", profile);

		// Send edit profile request
		BmgUtils.showProgDlg();
		APIClient
				.getInstance(delegate)
				.getApiService()
				.editUser(LocalStorage.accessToken, authUser.id, editUser, editProfileCallback);
	}

	/**
	 * {@link USER} Authorized User model
	 */
	private Callback<JsonObject> editProfileCallback = new Callback<JsonObject>() {

		@Override
		public void failure(RetrofitError error) {
			// Edit profile failure
			BmgUtils.hideProgDlg();
			BmgUtils.showMessage(R.string.EditProfile_Failure);
		}

		@Override
		public void success(JsonObject auth, retrofit.client.Response response) {
			// Edit profile success
			BmgUtils.hideProgDlg();
			BmgUtils.printLog(R.string.EditProfile_Success);
			BmgUtils.printLog(auth.toString());

			// Save authorized user info and access token
			USER user = USER.fromJson(auth.get("user"));
			user.password = newPassword;
			LocalStorage.saveAccessToken(user.accessToken.token, user.id, user.password);
			LocalStorage.authUser = user;
			delegate.updateUserPrile();

			// Navigate setting
			delegate.onBackPressed();
		}
	};

	private void showDatePicker() {

		Calendar date = Calendar.getInstance();
		if (birthday != null) {
			date = birthday;
		}
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);
		int day = date.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog dialog = new DatePickerDialog(delegate,
				datePickerListener, year, month, day);
		dialog.show();
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			birthday = Calendar.getInstance();
			birthday.set(Calendar.YEAR, selectedYear);
			birthday.set(Calendar.MONTH, selectedMonth);
			birthday.set(Calendar.DAY_OF_MONTH, selectedDay);
			tvBirthday.setText(BmgUtils.calendarToString(birthday,
					getResources().getString(R.string.BIRTHDAY_SHOW_FORMAT)));
		}
	};

}
